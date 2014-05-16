package freebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;

    private PrintWriter out;
    private BufferedReader in;
    private ClientProcessor processor;

    private String command;
    private Client client;
    private int state;

    public ClientThread(Client client, int state, Socket socket, String command) throws IOException {
        this.client = client;
        this.state = state;
        this.command = command;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        processor = new ClientProcessor();
    }

    @Override
    public void run() {
        String serverInput;
        try {
            if ((serverInput = in.readLine()) != null && !serverInput.equals("Bye.")) {
                switch (state) {
                    case FILM:
                        processor.setFilmState();
                        break;
                    case TV:
                        processor.setTvState();
                        break;
                    case LOADING:
                        processor.setLoadingState();
                        break;
                    default:
                        break;
                }

                processor.processServer(serverInput);

                if (command.equals("quit") && processor.isFinished()) {
                    client.setOutputList(processor.getOutputList());
                }

                out.println(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

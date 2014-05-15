package freebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

    private PrintWriter out;
    private BufferedReader in;
    private ClientProcessor processor;

    private String command;
    private Client client;

    public ClientThread(Client client, Socket socket, String command) throws IOException {
        this.command = command;
        this.client = client;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        processor = new ClientProcessor();
    }

    @Override
    public void run() {
        String serverInput;
        try {
            if ((serverInput = in.readLine()) != null && !serverInput.equals("Bye.")) {
                processor.processServer(serverInput);

                if (command.equals("quit") && processor.isFinished()) {
                    client.setOutputList(processor.getOutputList());
                }

                out.println(processor.processUser(command));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

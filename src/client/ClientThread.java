package client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import watchlistpro.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientThread implements Runnable {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;

    private int state;
    private Client client;
    private String command;

    private PrintWriter out;
    private BufferedReader in;
    private List<String> outputList;

    public ClientThread(Client client, int state, Socket socket, String command) throws IOException {
        this.state = state;
        this.client = client;
        this.command = command;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputList = new ArrayList<>();
    }

    @Override
    public void run() {
        String input;
        try {
            if ((input = in.readLine()) != null && !input.equals("Bye.")) {
                String[] output = input.split("");

                TopicHandler handler = new TopicHandler();
                if (output[0].equals("{")) {
                    switch (state) {
                        case FILM:
                            outputList = handler.filmOutput((JSONObject) JSONValue.parse(input));
                            break;
                        case TV:
                            outputList = handler.tvOutput((JSONObject) JSONValue.parse(input));
                            break;
                        case LOADING:
                            FileIO io = new FileIO();
                            String[] splitInput = input.split("//");
                            List<String> inputList = Arrays.asList(splitInput);
                            io.save(io.load(inputList), client.getFile());
                            break;
                        default:
                            break;
                    }
                }

                if (command.equals("quit")) {
                    client.setOutputList(outputList);
                }
                out.println(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import controller.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ClientThread implements Runnable {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;
    private static final int GETTOPIC = 3;
    private static final int GETSAVES = 4;

    private int state;
    private Client client;
    private String command;

    private PrintWriter out;
    private BufferedReader in;
    private JSONObject jsonOutput;

    public ClientThread(Client client, int state, Socket socket, String command) throws IOException {
        this.state = state;
        this.client = client;
        this.command = command;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String input;
        try {
            if ((input = in.readLine()) != null && !input.equals("Bye.")) {
                String[] output = input.split("");

                System.out.println("Server says: " + input);

                // Handle input from server.
                // TODO refactor b/c FILM, TV, and GETTOPIC all do the same thing
                if (output[0].equals("{")) {
                    switch (state) {
                        case FILM:
                            jsonOutput = (JSONObject) JSONValue.parse(input);
                            break;
                        case TV:
                            jsonOutput = (JSONObject) JSONValue.parse(input);
                            break;
                        case LOADING:
                            FileIO io = new FileIO();
                            String[] splitInput = input.split("//");
                            List<String> inputList = Arrays.asList(splitInput);
                            io.save(io.load(inputList), client.getFile());
                            break;
                        case GETTOPIC:
                            jsonOutput = (JSONObject) JSONValue.parse(input);
                        case GETSAVES:
                            Gson gson = new Gson();
                            Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                            HashMap<String, String> savesMap = gson.fromJson(input, mapType);
                            String saves = savesMap.get("saves");
                            String[] inputArray = saves.split("-=-");
                            client.setSaveArray(inputArray);
                        default:
                            break;
                    }
                }

                if (command.equals("quit")) {
                    client.setTopic(jsonOutput);
                }

                // Send command to server
                out.println(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

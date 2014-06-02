package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import controller.FileIO;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A client thread used to communicate with the WatchlistPro Server.
 */
public class ClientThread implements Runnable {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;
    private static final int GETTOPIC = 3;
    private static final int GETSAVES = 4;
    private static final int LOGIN = 5;
    private static final int LOGOUT = 6;
    private static final int ADD = 7;

    private int state;
    private Client client;
    private String command;

    private PrintWriter out;
    private BufferedReader in;
    private JSONObject jsonOutput;

    /**
     * Constructor.
     * @param client is the client that initialized the thread.
     * @param state is the current state of the client/client thread system.
     * @param socket is the socket through which communication occurs.
     * @param command is the command to send to the server.
     * @throws IOException
     */
    public ClientThread(Client client, int state, Socket socket, String command) throws IOException {
        this.state = state;
        this.client = client;
        this.command = command;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Runs the main thread loop which communicates with the server.
     */
    @Override
    public void run() {
        String input;
        try {
            if ((input = in.readLine()) != null && !input.equals("Bye.")) {
                String[] output = input.split("");

                // Handle input from server.
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

                            File file = client.getFile();
                            io.save(io.load(inputList), file);
                            break;
                        case GETTOPIC:
                            jsonOutput = (JSONObject) JSONValue.parse(input);
                            break;
                        case GETSAVES:
                            Gson gson = new Gson();
                            Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                            HashMap<String, String> savesMap = gson.fromJson(input, mapType);
                            String saves = savesMap.get("saves");
                            String[] inputArray = saves.split("-=-");
                            client.setSaveArray(inputArray);
                            break;
                        default:
                            break;
                    }
                }

                if (input.equals("true") || input.equals("false")) {
                    switch (state) {
                        case LOGIN:
                            if (input.equals("false")) {
                                client.setLoggedIn(false);
                            } else {
                                client.setLoggedIn(true);
                            }
                            break;
                        case LOGOUT:
                            client.setLoggedIn(false);
                            break;
                        case ADD:
                            if (input.equals("false")) {
                                client.setAccountCreated(false);
                            } else {
                                client.setAccountCreated(true);
                            }
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

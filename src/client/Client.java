package client;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String HOST = "hkhamm.com"; // "128.223.4.21";
    private static final int PORT = 1981;

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;
    private static final int GETTOPIC = 3;

    private int state;

    private Socket socket;
    private File file;
    private JSONObject topic;

    public Client() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread send(String command) throws IOException, InterruptedException {
        String[] commands = command.split(" ");
        switch (commands[0]) {
            case "film":
                state = FILM;
                break;
            case "tv":
                state = TV;
                break;
            case "load":
                state = LOADING;
                file = new File(commands[2]);
                break;
            case "getTopic":
                state = GETTOPIC;
                break;
        }
        return new Thread(new ClientThread(this, state, socket, command), "thread");
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public JSONObject getTopic() {
        return topic;
    }

    public void setTopic(JSONObject topic) {
        this.topic = topic;
    }

}

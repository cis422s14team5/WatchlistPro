package client;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * A client used to connect and communicate with the WatchlistPro Server.
 */
public class Client {

    // "localhost"; "hkhamm.com"; "128.223.4.21"; <- ix.cs.uoregon.edu
    private static final String HOST = "hkhamm.com";
    private static final int PORT = 1981;

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;
    private static final int GETTOPIC = 3;
    private static final int GETSAVES = 4;
    private static final int ADDACCOUNT = 5;

    private int state;

    private Socket socket;
    private File file;
    private JSONObject topic;
    private String[] saveArray;

    /**
     * Constructor.
     */
    public Client() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a thread that will communicate with the server.
     * @param command the command to send to the server.
     * @return the client thread.
     * @throws IOException
     * @throws InterruptedException
     */
    public Thread send(String command) throws IOException, InterruptedException {
        System.out.println("Command: " + command);
        String[] commands = command.split("-=-");
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
                command = commands[0] + "-=-" + commands[1] + "-=-" + file.getName();
                break;
            case "getTopic":
                state = GETTOPIC;
                break;
            case "getsaves":
                state = GETSAVES;
                break;
            case "add":
                state = ADDACCOUNT;
                break;
        }
        return new Thread(new ClientThread(this, state, socket, command), "thread");
    }

    /**
     * Gets the current save file.
     * @return the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the current save file.
     * @param file is the new file.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets the current topic.
     * @return the topic.
     */
    public JSONObject getTopic() {
        return topic;
    }

    /**
     * Sets the current topic.
     * @param topic is the topic.
     */
    public void setTopic(JSONObject topic) {
        this.topic = topic;
    }

    /**
     * Gets the current array of save file names.
     * @return the array of save files.
     */
    public String[] getSaveArray() {
        return saveArray;
    }

    /**
     * Sets the current array of save file names.
     * @param saveArray is an array of file names
     */
    public void setSaveArray(String[] saveArray) {
        this.saveArray = saveArray;
    }


}

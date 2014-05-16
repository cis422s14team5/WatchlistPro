package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;

    private int state;

    private ArrayList<String> outputList;
    private Socket socket;
    private File file;

    public Client() {
        String hostName = "hkhamm.com";
        int portNumber = 1981;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread send(String command) throws IOException, InterruptedException {
        switch (command) {
            case "film":
                state = FILM;
                break;
            case "tv":
                state = TV;
                break;
            case "load":
                state = LOADING;
                break;
        }
        return new Thread(new ClientThread(this, state, socket, command), "thread");
    }

    public ArrayList<String> getOutputList() {
        return outputList;
    }

    public void setOutputList(ArrayList<String> outputList) {
        this.outputList = outputList;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}

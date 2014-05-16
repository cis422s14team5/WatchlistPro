package freebase;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import watchlistpro.FileIO;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientProcessor {

    private static final int FILM = 0;
    private static final int TV = 1;
    private static final int LOADING = 2;

    private int state;
    private boolean finished = false;

    private ArrayList<String> outputList;

    TopicHandler topicHandler = new TopicHandler();

    public void processServer(String input) {
        String[] output = input.split("");
        finished = false;

        if (output[0].equals("{")) {

            outputList = new ArrayList<>();
            switch (state) {
                case FILM:
                    outputList = topicHandler.filmOutput((JSONObject) JSONValue.parse(input));
                    break;
                case TV:
                    outputList = topicHandler.tvOutput((JSONObject) JSONValue.parse(input));
                    break;
                case LOADING:
                    FileIO io = new FileIO();
                    io.save(io.load(new ArrayList<>(Arrays.asList(input.split("<('_')>")))));
                    break;
                default:
                    break;
            }
            finished = true;
        }
    }

    public ArrayList<String> getOutputList() {
        return outputList;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFilmState() {
        this.state = FILM;
    }

    public void setTvState() {
        this.state = TV;
    }

    public void setLoadingState() {
        this.state = LOADING;
    }
}

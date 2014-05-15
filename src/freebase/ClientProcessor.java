package freebase;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

public class ClientProcessor {

    private static final int FILM = 0;
    private static final int TV = 1;

    private int state = FILM;
    private boolean finished = false;

    private ArrayList<String> outputList;

    TopicProcessor processor = new TopicProcessor();

    public void processServer(String input) {
        String[] output = input.split("");
        finished = false;

        if (output[0].equals("{")) {
            JSONObject topic = (JSONObject) JSONValue.parse(input);

            outputList = new ArrayList<>();
            switch (state) {
                case FILM:
                    outputList = processor.filmOutput(topic);
                    break;
                case TV:
                    outputList = processor.tvOutput(topic);
                default:
                    break;
            }
            finished = true;
        }
    }

    public String processUser(String input) {

        if (input.equals("film")) {
            state = FILM;
        } else if (input.equals("tv")) {
            state = TV;
        }

        return input;
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
}

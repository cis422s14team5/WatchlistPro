package freebase;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

public class ClientProcessor {

    private static final int FILM = 0;
    private static final int TV = 1;

    private int state = FILM;

    private ArrayList<String> outputList;

    TopicProcessor processor = new TopicProcessor();

    public void processServer(String input) {
        String[] output = input.split("");

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
            outputList.forEach(System.out::println);

            System.out.println(
                    "Search with <title>, \"film\" or \"tv\" to switch filters, or \"quit\" to quit.");
        } else {
            System.out.println(input);
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
}

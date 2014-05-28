package controller;

import client.Client;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import model.Media;

import java.util.List;

public class Fetch extends Task {

    private Controller controller;
    private ListView<Media> mediaList;
    private String mediaEditType;
    private Client client;

    public Fetch(Controller controller, ListView<Media> mediaList, String mediaEditType) {
        this.controller = controller;
        this.mediaList = mediaList;
        this.mediaEditType = mediaEditType;
        controller.startProgressIndicator();
        client = new Client();
    }

    @Override
    protected Object call() throws Exception {
        String command = mediaList.getSelectionModel().getSelectedItem().getTitle();

        Thread type = client.send(mediaEditType);
        Thread search = client.send(command);
        Thread quit = client.send("quit");

        type.start();
        type.join();

        search.start();
        search.join();

        quit.start();
        quit.join();

        return null;
    }

    @Override protected void succeeded() {
        super.succeeded();
        System.out.println("Fetch is done!");
        end();
    }

    @Override protected void cancelled() {
        super.cancelled();
        System.out.println("Fetch was cancelled!");
        end();
    }

    @Override protected void failed() {
        super.failed();
        System.out.println("Fetch failed!");
        end();
    }

    private void end() {
        TopicHandler handler = new TopicHandler();
        List<String> outputList;
        if (mediaEditType.equals("film")) {
            outputList = handler.filmOutput(client.getTopic());
            controller.setFilmEditPane(outputList);
        } else {
            outputList = handler.tvOutput(client.getTopic());
            controller.setTvEditPane(outputList);
        }
        controller.stopProgressIndicator();
    }
}

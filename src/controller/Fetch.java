package controller;

import client.Client;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import model.Media;
import view.DialogPane;

import java.util.List;

/**
 * A JavaFX task used to fetch data from the WatchlistPro server about a media object.
 */
public class Fetch extends Task {

    private Controller controller;
    private ListView<Media> mediaList;
    private String mediaEditType;
    private Client client;

    /**
     * Constructor.
     * @param controller is the controller that initialized the fetch.
     * @param mediaList is the list of media objects in the controller.
     * @param mediaEditType is the type of the media object for which the data will be fetched.
     */
    public Fetch(Controller controller, ListView<Media> mediaList, String mediaEditType) {
        this.controller = controller;
        this.mediaList = mediaList;
        this.mediaEditType = mediaEditType;
        controller.startProgressIndicator();
        client = new Client();
    }

    /**
     * Communicates with the server.
     * @return null.
     * @throws Exception
     */
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

    /**
     * Runs the end method if the thread completes successfully.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        end();
    }

    /**
     * Runs the end method if the thread is cancelled.
     */
    @Override
    protected void cancelled() {
        super.cancelled();
        end();
    }

    /**
     * Runs the end method if the thread fails to complete.
     */
    @Override
    protected void failed() {
        super.failed();
        DialogPane dialogPane = new DialogPane();
        dialogPane.createWarningDialog("Failed to connect!", "Failed to fetch because the server is unavailable.\n" +
                                                             "Please try again later.");
        end();
    }

    /**
     * Sets the controllers edit pane to the contents of the output list and stops the progress indicator.
     */
    private void end() {
        TopicHandler handler = new TopicHandler();
        List<String> outputList;
        if (mediaEditType.equals("film")) {
            outputList = handler.handleFilmOutput(client.getTopic());
            controller.setFilmEditPane(outputList);
        } else {
            outputList = handler.handleTvOutput(client.getTopic());
            controller.setTvEditPane(outputList);
        }
        controller.stopProgressIndicator();
    }
}

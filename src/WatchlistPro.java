import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Launches the WatchlistPro view.
 */
public class WatchlistPro extends Application {

    /**
     * Start to application.
     * @param stage is the application stage.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/WatchlistPro.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);

        Controller controller = loader.getController();
        controller.setStage(stage);

        stage.setOnCloseRequest((final WindowEvent windowEvent) -> controller.closeWindow());
        controller.createRecentMenu();
        String file = controller.getSaveFile().getName();
        stage.setTitle("WatchlistPro - " + file);
        stage.show();

    }

    /**
     * Main method.
     * @param args is the command line arguments array.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

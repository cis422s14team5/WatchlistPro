package watchlistpro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Launches the WatchlistPro view.
 */
public class WatchlistPro extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../WatchlistPro.fxml"));

        Parent root = loader.load();

        WatchlistProController controller = loader.getController();
        controller.setStage(stage);
        stage.setOnCloseRequest((final WindowEvent windowEvent) -> controller.closeWindow());

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("WatchlistPro");

        controller.updateRecentList();
        controller.styleButtons();

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

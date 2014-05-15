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
        Parent root = FXMLLoader.load(getClass().getResource("WatchlistPro.fxml"));

        Scene scene = new Scene(root);

        stage.setOnCloseRequest((final WindowEvent windowEvent) -> Platform.exit());

        stage.setScene(scene);
        stage.setTitle("WatchlistPro");
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

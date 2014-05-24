import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import controller.Controller;

/**
 * Launches the WatchlistPro view.
 */
public class WatchlistPro extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WatchlistPro.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(stage);

        stage.setScene(new Scene(root));
        stage.setOnCloseRequest((final WindowEvent windowEvent) -> controller.closeWindow());
        controller.createRecentMenu();
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

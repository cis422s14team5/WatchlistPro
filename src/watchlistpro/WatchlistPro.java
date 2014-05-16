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
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("../WatchlistPro.fxml"));
        final Parent root = loader.load();
        final WatchlistProController controller = loader.getController();

        Scene scene = new Scene(root);

        controller.setStage(stage);

        //stage.initStyle(StageStyle.UTILITY);
        //stage.setResizable(false);
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

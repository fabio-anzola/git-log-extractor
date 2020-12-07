package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.response.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The App class
 *
 * @author fabioanzola
 */
public class App extends Application {

    /**
     * Contains the current log
     */
    public static Log log;

    /**
     * The stage
     */
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("applayout.fxml"));
        stage.setTitle("Git Log Extraction App");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}

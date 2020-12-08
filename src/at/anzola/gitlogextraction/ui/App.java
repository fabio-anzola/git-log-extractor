package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.response.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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

    /**
     * Recents menu
     */
    public static VBox vbox;

    /**
     * Recents menu
     */
    public static ListView<String> listv;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        //Build UI
        UIBuilder.basicUI();
        vbox.getChildren().add(listv);

        //Show UI
        stage.setTitle("Git Log Extraction App");
        stage.setScene(new Scene(vbox, 800, 500));
        stage.show();
    }

    /**
     * Called when the MenuItem 'open' is pressed
     *
     * @throws IOException If something goes wrong
     */
    public void open() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            loadNew(file);
            MenuItem menuItem = new MenuItem(file.getName());
            menuItem.setOnAction(actionEvent -> {
                try {
                    loadNew(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            UIBuilder.recent.getItems().add(menuItem);
        }
    }

    /**
     * Loads new log
     *
     * @param file The log file
     * @throws IOException If something goes wrong
     */
    public void loadNew(File file) throws IOException {
        log = LogReader.read(file.getPath());
        UIBuilder.showCommits();
    }
}

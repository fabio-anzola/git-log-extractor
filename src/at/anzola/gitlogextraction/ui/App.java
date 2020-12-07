package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.response.Log;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        //Build UI
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        Menu recentMenu = new Menu("Open Recent");
        MenuItem prefItem = new MenuItem("Preferences...");
        MenuItem quitItem = new MenuItem("Quit");

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About Git Log Extraction");

        fileMenu.getItems().addAll(
                openItem,
                recentMenu,
                prefItem,
                quitItem);
        helpMenu.getItems().addAll(
                aboutItem
        );
        menuBar.getMenus().addAll(
                fileMenu,
                helpMenu
        );
        vBox.getChildren().add(menuBar);

        //Show UI
        stage.setTitle("Git Log Extraction App");
        stage.setScene(new Scene(vBox, 800, 500));
        stage.show();
    }

    /**
     * Called when the MenuItem 'open' is pressed
     *
     * @throws IOException If something goes wrong
     */
    @FXML
    public void open() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            loadNew(file);
        }
    }

    public static void loadNew(File file) throws IOException {
        log = LogReader.read(file.getPath());
    }
}

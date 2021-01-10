package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogReader;
import at.anzola.gitlogextraction.response.Log;
import at.anzola.gitlogextraction.utlis.Analysis;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
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
     * The scene
     */
    public static Scene scene;

    /**
     * The analysis for Log
     */
    public static Analysis analysis;

    /**
     * Recents menu
     */
    public static VBox vbox;

    /**
     * Saves the current log
     */
    public static File currentLog;

    /**
     * Recents menu
     */
    public static ListView<String> listv;

    /**
     * Items for ListView
     */
    public static ObservableList<String> listvItems;

    public static void main(String[] args) {
        if (Taskbar.isTaskbarSupported()) {
            if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)) {
                try {
                    File file = new File("src/main/resources/icon.png");
                    Image image = ImageIO.read(file);
                    Taskbar.getTaskbar().setIconImage(image);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        //Build UI
        UIBuilder.basicUI();
        UIBuilder.anonymizeItemToggle.setDisable(true);
        UIBuilder.viewMenu.setDisable(true);
        vbox.getChildren().add(listv);

        //App Icon
        stage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/icon.png"));

        //Set scene
        scene = new Scene(vbox, 800, 500);

        //Show UI
        stage.setTitle("Git Log Extraction App");
        stage.setScene(scene);
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
            currentLog = file;
            loadNew(file);
            MenuItem menuItem = new MenuItem(file.getName());
            menuItem.setOnAction(actionEvent -> {
                try {
                    loadNew(file);
                }
                catch (IOException e) {
                    Views.info("Sorry, something went wrong. \n Please reopen the file manually or issue a bug.");
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
        UIBuilder.viewMenu.setDisable(false);
        UIBuilder.anonymizeItemToggle.setDisable(false);
        UIBuilder.anonymizeItemToggle.setSelected(false);
        UIBuilder.showCommits();
        analysis = new Analysis(log);
    }

    /**
     * Updates ListView with item-list
     */
    public static void updateListView() {
        App.listv.getItems().clear();
        App.listv.setItems(listvItems);
        App.listv.refresh();
    }
}

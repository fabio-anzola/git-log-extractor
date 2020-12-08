package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.response.Commit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * The App class
 *
 * @author fabioanzola
 */
public class UIBuilder {

    /**
     * Recents menu
     */
    public static Menu recent;

    /**
     * Creates the basic ui
     *
     * @return vBox
     */
    public static void basicUI() {
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        Menu recentMenu = new Menu("Open Recent");
        recent = recentMenu;
        MenuItem prefItem = new MenuItem("Preferences...");
        MenuItem quitItem = new MenuItem("Quit");

        Menu viewMenu = new Menu("View");
        RadioMenuItem anonymizeItemToggle = new RadioMenuItem("Anonymize");

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About Git Log Extraction");

        quitItem.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });
        openItem.setOnAction(actionEvent -> {
            try {
                (new App()).open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        anonymizeItemToggle.setOnAction(actionEvent -> {
            //TODO Show pane "only once action"
            anonymizeItemToggle.setDisable(true);
            //TODO anonymize!
        });
        aboutItem.setOnAction(actionEvent -> {
            (new App()).getHostServices().showDocument("https://github.com/fabio-anzola/git-log-extractor");
        });

        fileMenu.getItems().addAll(
                openItem,
                recentMenu,
                prefItem,
                quitItem);
        viewMenu.getItems().addAll(
                anonymizeItemToggle
        );
        helpMenu.getItems().addAll(
                aboutItem
        );
        menuBar.getMenus().addAll(
                fileMenu,
                viewMenu,
                helpMenu
        );

        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList("No Commits to view...");
        list.setItems(items);
        list.setOrientation(Orientation.VERTICAL);
        App.listv = list;

        vBox.getChildren().add(menuBar);
        App.vbox = vBox;
    }

    /**
     * Shows list of commits on screen
     *
     * @throws IOException If something goes wrong
     */
    public static void showCommits() throws IOException {
        ArrayList<String> commits = new ArrayList<>();
        for (Commit commit : App.log.gitLog) {
            commits.add(String.format("Hash: %s \nAuthor: %s \nDate: %s \nMessage: %s",
                    commit.hash, commit.author, commit.authorDate.atZone(ZoneId.systemDefault()), commit.message));
        }
        ObservableList<String> items = FXCollections.observableList(commits);
        App.listv.getItems().clear();
        App.listv.setItems(items);
        App.listv.refresh();
    }
}

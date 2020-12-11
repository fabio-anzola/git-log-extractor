package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.utlis.Saver;
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

    public static RadioMenuItem anonymizeItemToggle;

    /**
     * Creates the basic ui
     */
    public static void basicUI() {
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        Menu recentMenu = new Menu("Open Recent");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save as");
        recent = recentMenu;
        MenuItem prefItem = new MenuItem("Preferences...");
        MenuItem quitItem = new MenuItem("Quit");

        Menu viewMenu = new Menu("View");
        anonymizeItemToggle = new RadioMenuItem("Anonymize");

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
        saveItem.setOnAction(actionEvent -> {
            if (anonymizeItemToggle.isSelected()) {
                //TODO save
            } else {
                Views.info("Sie müssen den Log \n vor dem Speichern ändern.");
            }
        });
        saveAsItem.setOnAction(actionEvent -> {
            if (anonymizeItemToggle.isSelected()) {
                //TODO Save as
            } else {
                Views.info("Sie müssen den Log \n vor dem Speichern ändern.");
            }
        });
        anonymizeItemToggle.setOnAction(actionEvent -> {
            Views.criticalDecision("Attention!\nThis is a one way action.\nAfter anonymizing there is no way\nto recover the data.");
        });
        aboutItem.setOnAction(actionEvent -> {
            (new App()).getHostServices().showDocument("https://github.com/fabio-anzola/git-log-extractor");
        });

        fileMenu.getItems().addAll(
                openItem,
                recentMenu,
                saveItem,
                saveAsItem,
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
     */
    public static void showCommits() {
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

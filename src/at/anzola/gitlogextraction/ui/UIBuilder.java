package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogWriter;
import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.utlis.UIUtils;
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

        Menu sortMenu = new Menu("Sort by");
        ToggleGroup sortMenuToggleGroup = new ToggleGroup();

        RadioMenuItem sortMenuHash = new RadioMenuItem("Hash");
        sortMenuHash.setToggleGroup(sortMenuToggleGroup);

        RadioMenuItem sortMenuAuthor = new RadioMenuItem("Author");
        sortMenuAuthor.setToggleGroup(sortMenuToggleGroup);

        RadioMenuItem sortMenuDate = new RadioMenuItem("Date");
        sortMenuDate.setToggleGroup(sortMenuToggleGroup);

        RadioMenuItem sortMenuMessage = new RadioMenuItem("Message");
        sortMenuMessage.setToggleGroup(sortMenuToggleGroup);

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
                try {
                    LogWriter.save();
                } catch (IOException e) {
                    Views.info("Sorry, something went wrong. \n Please try again or issue a bug.");
                    e.printStackTrace();
                }
            } else {
                Views.info("Sie müssen den Log \n vor dem Speichern ändern.");
            }
        });
        saveAsItem.setOnAction(actionEvent -> {
            if (anonymizeItemToggle.isSelected()) {
                try {
                    LogWriter.saveAs(Views.fileSave());
                } catch (IOException e) {
                    Views.info("Sorry, something went wrong. \n Please try again or issue a bug.");
                    e.printStackTrace();
                }
            } else {
                Views.info("Sie müssen den Log \n vor dem Speichern ändern.");
            }
        });
        anonymizeItemToggle.setOnAction(actionEvent -> {
            Views.criticalDecision("Attention!\nThis is a one way action.\nAfter anonymizing there is no way\nto recover the data.");
        });
        sortMenuAuthor.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.copareLV(App.listvItems, UIUtils.lvsort.author);
        });
        sortMenuHash.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.copareLV(App.listvItems, UIUtils.lvsort.hash);
        });
        sortMenuDate.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.copareLV(App.listvItems, UIUtils.lvsort.date);
        });
        sortMenuMessage.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.copareLV(App.listvItems, UIUtils.lvsort.message);
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
        sortMenu.getItems().addAll(
                sortMenuAuthor,
                sortMenuDate,
                sortMenuHash,
                sortMenuMessage
        );
        viewMenu.getItems().addAll(
                anonymizeItemToggle,
                sortMenu
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
        App.listvItems = FXCollections.observableList(commits);
        App.updateListView();
    }
}

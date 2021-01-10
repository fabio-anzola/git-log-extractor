package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogWriter;
import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.utlis.Diagrams;
import at.anzola.gitlogextraction.utlis.UIUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.io.File;
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
     * View menu
     */
    public static Menu viewMenu;

    /**
     * anonymizeItemToggle reference
     */
    public static RadioMenuItem anonymizeItemToggle;

    /**
     * Sort by Date item
     */
    public static RadioMenuItem sortMenuDate;

    /**
     * Creates the basic ui
     */
    public static void basicUI() {
        VBox vBox = new VBox();
        MenuBar menuBar = new MenuBar();

        TextField searchInput = new TextField();

        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        Menu recentMenu = new Menu("Open Recent");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save as");
        recent = recentMenu;
        MenuItem prefItem = new MenuItem("Change Theme");
        prefItem.setOnAction(actionEvent -> {
            changeStylesheet(App.scene.getStylesheets().size() > 0);
        });
        MenuItem quitItem = new MenuItem("Quit");

        viewMenu = new Menu("View");
        anonymizeItemToggle = new RadioMenuItem("Anonymize");

        Menu sortMenu = new Menu("Sort by");
        ToggleGroup sortMenuToggleGroup = new ToggleGroup();

        RadioMenuItem sortMenuHash = new RadioMenuItem("Hash");
        sortMenuHash.setToggleGroup(sortMenuToggleGroup);

        RadioMenuItem sortMenuAuthor = new RadioMenuItem("Author");
        sortMenuAuthor.setToggleGroup(sortMenuToggleGroup);

        sortMenuDate = new RadioMenuItem("Date");
        sortMenuDate.setToggleGroup(sortMenuToggleGroup);

        RadioMenuItem sortMenuMessage = new RadioMenuItem("Message");
        sortMenuMessage.setToggleGroup(sortMenuToggleGroup);

        Menu chartsMenu = new Menu("Charts");
        MenuItem cpdChart = new MenuItem("Commits per Day");
        MenuItem cpmChart = new MenuItem("Commits per Month");
        MenuItem cpyChart = new MenuItem("Commits per Year");
        cpdChart.setOnAction(actionEvent -> {
            DiagramViews.createChart(Diagrams.day);
        });
        cpmChart.setOnAction(actionEvent -> {
            DiagramViews.createChart(Diagrams.month);
        });
        cpyChart.setOnAction(actionEvent -> {
            DiagramViews.createChart(Diagrams.year);
        });

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
            App.listvItems = UIUtils.compareLV(App.listvItems, UIUtils.Lvsort.author);
        });
        sortMenuHash.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.compareLV(App.listvItems, UIUtils.Lvsort.hash);
        });
        sortMenuDate.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.compareLV(App.listvItems, UIUtils.Lvsort.date);
        });
        sortMenuMessage.setOnAction(actionEvent -> {
            App.listvItems = UIUtils.compareLV(App.listvItems, UIUtils.Lvsort.message);
        });
        aboutItem.setOnAction(actionEvent -> {
            (new App()).getHostServices().showDocument("https://github.com/fabio-anzola/git-log-extractor");
        });
        searchInput.textProperty().addListener(obs -> {
            UIUtils.filterListView(searchInput.getText());
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
        chartsMenu.getItems().addAll(
                cpdChart,
                cpmChart,
                cpyChart
        );
        viewMenu.getItems().addAll(
                anonymizeItemToggle,
                sortMenu,
                chartsMenu
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

        vBox.getChildren().addAll(menuBar, searchInput);
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
        sortMenuDate.fire();
        sortMenuDate.setSelected(true);
        App.updateListView();
    }

    /**
     * Changes style of App.scene
     *
     * @param isDark If dark theme is applied
     */
    public static void changeStylesheet(boolean isDark) {
        if (isDark) {
            App.scene.getStylesheets().remove(0);
        } else {
            App.scene.getStylesheets().add(new File("src/main/resources/javafx/frontend/dark_style.css").toURI().toString());
        }
        App.stage.show();
    }
}

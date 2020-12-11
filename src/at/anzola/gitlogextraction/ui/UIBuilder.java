package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.response.Commit;
import at.anzola.gitlogextraction.utlis.Anonym;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(App.stage);
            Text errorDisplay = new Text("Attention!\nThis is a one way action.\nAfter anonymizing there is no way\nto recover the data.");
            Button exit = new Button("Cancel");
            Button ok = new Button("Ok");
            HBox hBox = new HBox(exit, ok);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            errorDisplay.setTextAlignment(TextAlignment.CENTER);
            VBox screen = new VBox(errorDisplay, hBox);
            screen.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(screen, 250, 100);
            dialog.setScene(dialogScene);
            dialog.show();
            exit.setCancelButton(true);
            exit.setDefaultButton(true);
            exit.setOnAction(exitEvent -> {
                anonymizeItemToggle.setSelected(false);
                dialog.close();
            });
            ok.setOnAction(actionEvent1 -> {
                anonymizeItemToggle.setDisable(true);
                dialog.close();
                App.log = Anonym.anonymize(App.log);
                UIBuilder.showCommits();
            });
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

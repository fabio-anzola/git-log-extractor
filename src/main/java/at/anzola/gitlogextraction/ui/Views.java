package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.Checkstyle.MagicNumber;
import at.anzola.gitlogextraction.utlis.Anonym;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * The Views class
 *
 * @author fabioanzola
 */
public class Views {
    /**
     * Simple 'ok' pop up box
     *
     * @param message The message to be shown
     */
    public static void info(String message) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.stage);
        Text errorDisplay = new Text(message);
        Button exit = new Button("Ok");
        errorDisplay.setTextAlignment(TextAlignment.CENTER);
        VBox screen = new VBox(errorDisplay, exit);
        screen.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(screen, MagicNumber.TWO_HUNDRED_FIFTY, MagicNumber.ONE_HUNDRED);
        dialog.setScene(dialogScene);
        dialog.show();
        exit.setOnAction(exitEvent -> dialog.close());
    }

    /**
     * Critical 'ok'/'cancel' pop up box (used with anonymizing)
     *
     * @param message The message to be shown
     */
    public static void criticalDecision(String message) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.stage);
        Text errorDisplay = new Text(message);
        Button exit = new Button("Cancel");
        Button ok = new Button("Ok");
        HBox hBox = new HBox(exit, ok);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(MagicNumber.TEN);
        errorDisplay.setTextAlignment(TextAlignment.CENTER);
        VBox screen = new VBox(errorDisplay, hBox);
        screen.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(screen, MagicNumber.TWO_HUNDRED_FIFTY, MagicNumber.ONE_HUNDRED);
        dialog.setScene(dialogScene);
        dialog.show();
        exit.setCancelButton(true);
        exit.setDefaultButton(true);
        exit.setOnAction(exitEvent -> {
            UIBuilder.anonymizeItemToggle.setSelected(false);
            dialog.close();
        });
        ok.setOnAction(actionEvent1 -> {
            UIBuilder.anonymizeItemToggle.setDisable(true);
            dialog.close();
            App.log = Anonym.anonymize(App.log);
            UIBuilder.showCommits();
        });
    }

    /**
     * Dialog to select where a File should be saved to
     *
     * @return The destination File
     */
    public static File fileSave() {
        final FileChooser fileChooser = new FileChooser();
        return fileChooser.showSaveDialog(App.stage);
    }
}

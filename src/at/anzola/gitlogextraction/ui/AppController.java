package at.anzola.gitlogextraction.ui;

import at.anzola.gitlogextraction.reader.LogReader;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * The AppController class
 *
 * @author fabioanzola
 */
public class AppController {

    /**
     * Called when the MenuItem 'open' is pressed
     *
     * @throws IOException If something goes wrong
     */
    @FXML
    public void open() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.stage);
        if (file != null) {
            App.log = LogReader.read(file.getPath());
        }
    }
}

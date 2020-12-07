package at.anzola.gitlogextraction.ui;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class AppController {

    @FXML
    public void open() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.stage);
        if (file != null) {
            System.out.println(file);
        }
    }
}

package org.openjfx.MainMenuScreens;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class Credits {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}
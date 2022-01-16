package org.openjfx.MainMenuScreens;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class Rules {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}
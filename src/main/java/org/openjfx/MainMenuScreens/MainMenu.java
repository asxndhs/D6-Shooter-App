package org.openjfx.MainMenuScreens;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.openjfx.App;

import static org.openjfx.Logic.ReadSavedData.saveStats;
import static org.openjfx.Logic.Reset.resetGameVariables;

public class MainMenu {

    public VBox confirmBox;

    @FXML
    private void switchToGame() throws IOException {
        resetGameVariables();
        App.setRoot("game");
    }

    @FXML
    private void switchToRules() throws IOException {
        App.setRoot("rules");
    }

    @FXML
    private void switchToHighScores() throws IOException {
        App.setRoot("highScores");
    }

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void switchToCredits() throws IOException {
        App.setRoot("credits");
    }

    @FXML
    private void exitGame() throws IOException {
        confirmBox.setVisible(true);
    }

    @FXML
    private void confirmQuit() {
        saveStats();
        System.exit(0);
    }

    @FXML
    private void returnToMenu() {
        confirmBox.setVisible(false);
    }
}

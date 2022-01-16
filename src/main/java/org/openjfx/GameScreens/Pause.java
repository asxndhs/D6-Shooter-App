package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.Logic.Day.day;
import static org.openjfx.Logic.ReadSavedData.saveStats;
import static org.openjfx.Logic.TownMethods.FindTown.townNames;
import static org.openjfx.SettingsScreens.Audio.audioFromPauseMenu;
import static org.openjfx.SettingsScreens.Settings.dayLimit;
import static org.openjfx.SettingsScreens.Settings.finishingTownIndex;

public class Pause implements Initializable {

    public Label gameMessage;
    public VBox confirmBox;

    public static String fromScene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameMessage.setText("It is day " + day + "/" + dayLimit + " and your destination is " + townNames[finishingTownIndex]);
    }

    @FXML
    private void quitToMenu() {
        confirmBox.setVisible(true);
    }

    @FXML
    private void confirmQuit() throws IOException {
        saveStats();
        App.setRoot("mainMenu");
    }

    @FXML
    private void returnToPause() {
        confirmBox.setVisible(false);
    }

    @FXML
    private void closePause() throws IOException {
        App.setRoot(fromScene);
    }

    @FXML
    private void audioSettings() throws IOException {
        audioFromPauseMenu = true;
        App.setRoot("audio");
    }

    @FXML
    private void goToRules() throws IOException {
        // TODO: Add in variable which allows to return to pause menu
        App.setRoot("rules");
    }
}
package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.playerSpace;
import static org.openjfx.Logic.Reset.endOfTurn;
import static org.openjfx.Logic.TownMethods.FindTown.*;
import static org.openjfx.Logic.TownMethods.PlayPoker.pokerPlayed;
import static org.openjfx.Logic.TownMethods.PlayPoker.resetPoker;
import static org.openjfx.SettingsScreens.Settings.unlimitedPoker;

public class Town implements Initializable {

    public Button pokerButton; // TODO: Grey out when poker not available
    public VBox confirmBox;

    public static int townSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (townSceneProgress == 1) {
            confirmBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "town";
        App.setRoot("pause");
    }

    @FXML
    private void switchToShop() throws IOException {
        App.setRoot("shop");
    }

    @FXML
    private void switchToPoker() throws IOException {
        checkPokerPlayable(); // Determine player has something to wager
        if (!pokerPlayed || unlimitedPoker) {
            resetPoker();
            App.setRoot("poker");
        }
    }

    @FXML
    private void leaveTown() {
        townSceneProgress = 1;
        confirmBox.setVisible(true);
    }

    @FXML
    private void returnToTown() {
        townSceneProgress = 0; // Reset
        confirmBox.setVisible(false);
    }

    @FXML
    private void confirmLeave() throws IOException {
        townSpace = false;
        townSceneProgress = 0; // Reset
        setPlayerSpaceLabel("Player space: " + playerSpace);
        endOfTurn();
        App.setRoot("game");
    }

    // TODO: Should player see a message stating whether they can play poker or not?
    private void checkPokerPlayable() {
        if (ammo < 1 && food < 1 && gold < 1) {
            pokerPlayed = true;
        }
    }

    public static StringProperty townName = new SimpleStringProperty();

    public String getTownName() {
        return townName.get();
    }

    public static StringProperty townNameProperty() {
        return townName;
    }

    public static void setTownName(String townName) {
        Town.townName.set(townName);
    }
}
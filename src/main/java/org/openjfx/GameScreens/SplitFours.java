package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.EventMethods.FullEvents.lostGunsightEvent;
import static org.openjfx.Logic.RollMethods.RollActions.*;

public class SplitFours implements Initializable {

    public static int fourCounter;
    public Label hideCount;
    public Label seekShelterCount;
    public Label backroadsCount;
    public Label fightCount;
    public Label fourCounterLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshPage();
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "splitFours";
        App.setRoot("pause");
    }

    @FXML
    private void switchToResolveDice() throws IOException {
        if (fourCounter == 0) {
            App.setRoot("resolveDice");
        }
    }

    @FXML
    private void hideMinus() {
        if (hide > 0) {
            hide--;
            fourCounter++;
            System.out.println("Hide Dice: " + hide);
        }
        refreshPage();
    }

    @FXML
    private void hidePlus() {
        if (fourCounter > 0) {
            hide++;
            fourCounter--;
            System.out.println("Hide Dice: " + hide);
        }
        refreshPage();
    }

    @FXML
    private void seekShelterMinus() {
        if (seekShelter > 0) {
            seekShelter--;
            fourCounter++;
            System.out.println("Seek Shelter Dice: " + seekShelter);
        }
        refreshPage();
    }

    @FXML
    private void seekShelterPlus() {
        if (!lostGunsightEvent) {
            if (fourCounter > 0) {
                seekShelter++;
                fourCounter--;
                System.out.println("Seek Shelter Dice: " + seekShelter);
            }
            refreshPage();
        }
    }

    @FXML
    private void backroadsMinus() {
        if (backroads > 0) {
            backroads--;
            fourCounter++;
            System.out.println("Backroads Dice: " + backroads);
        }
        refreshPage();
    }

    @FXML
    private void backroadsPlus() {
        if (fourCounter > 0) {
            backroads++;
            fourCounter--;
            System.out.println("Backroads Dice: " + backroads);
        }
        refreshPage();
    }

    @FXML
    private void fightMinus() {
        if (fight > 0) {
            fight--;
            fourCounter++;
            System.out.println("Fight Dice: " + fight);
        }
        refreshPage();
    }

    @FXML
    private void fightPlus() {
        if (fourCounter > 0) {
            fight++;
            fourCounter--;
            System.out.println("Fight Dice: " + fight);
        }
        refreshPage();
    }

    @FXML
    public void refreshPage() {

        // Insert functions to refresh page every time an action is taken
        fourCounterLabel.setText("Total 4s: " + fourCounter);
        hideCount.setText(Integer.toString(hide));
        seekShelterCount.setText(Integer.toString(seekShelter));
        backroadsCount.setText(Integer.toString(backroads));
        fightCount.setText(Integer.toString(fight));
    }
}

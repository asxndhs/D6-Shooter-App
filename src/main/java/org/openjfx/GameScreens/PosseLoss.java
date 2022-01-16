package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.GameScreens.Rations.setRationsResult;
import static org.openjfx.GameScreens.ResolveDice.specialRoll;
import static org.openjfx.GameScreens.SpecialRoll.chooseOption;
import static org.openjfx.GameScreens.SpecialRoll.specialRollPopUpMessage;
import static org.openjfx.Logic.Day.day;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.GameOverMethods.posseCheck;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.RationsMethods.rationsDay;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class PosseLoss implements Initializable {

    public static String posseLossFromScene;
    public static Boolean stopPosseLoss = false;
    public static int posseLossLeft;
    public static Boolean posseItemsDisabled = false;
    public static int startingPosse;

    public VBox useItemPopUpBox;
    public Label itemMessage;
    public HBox items;
    public VBox itemUsedPopUpBox;
    public Label itemResult;
    public VBox posseLossPopUpBox;
    public Label totalPosseLoss;

    public RadioButton medBandagesButton = new RadioButton("Medicine/Bandages");
    public RadioButton deserter1Button = new RadioButton("Sacrifice the first Deserter");
    public RadioButton deserter2Button = new RadioButton("Sacrifice the second Deserter");

    public static int posseLossSceneProgress = 0;
    public static String totalPosseLossMessage;
    public static String itemResultMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (posseLossSceneProgress == 1) {
            // Posse loss item not used, posse loss message pops up
            totalPosseLoss.setText(totalPosseLossMessage);
            posseLossPopUpBox.setVisible(true);
        } else if (posseLossSceneProgress == 2) {
            // Posse item is to be used and use item pop up box appears
            usePosseItem();
        } else if (posseLossSceneProgress == 3) {
            // Posse item selected and used
            itemResult.setText(itemResultMessage);
            itemUsedPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "posseLoss";
        App.setRoot("pause");
    }

    @FXML
    private void backToScene() throws IOException {
        posseItemsDisabled = true;
        if (hunterInventory) {
            hunterPosseCheck(); // Check there are at least two posse for hunter to remain
        }
        posseCheck(); // Check player still has at least one posse
        if (posse > 0 && posseLossLeft > 0) {
            // method that does the rest of the posse loss - does not reload this scene even if items still available
            posseChange = posseLossLeft;
            for (int i = 0; i < posseChange; i++) {
                posse--;
                posseLossLeft--;
                System.out.println("Player loses one Posse. They now have " + posse + " Posse members remaining");
                posseLossChecks();
                if (stopPosseLoss) {
                    stopPosseLoss = false;
                    posseCheck();
                    return;
                }
            }
            System.out.println("Player loses " + posseChange + " Posse. They now have " + posse + " Posse members remaining");
            totalPosseLossMessage = "You lost " + posseChange + " more Posse members and now have " + posse + " members remaining";
            totalPosseLoss.setText(totalPosseLossMessage);
            posseLossSceneProgress = 1;
            posseLossPopUpBox.setVisible(true);
        } else if (posse > 0) {
            negatePosseLoss = false;
            if (posseLossFromScene.equals("specialRoll") && specialRoll.equals("6")) {
                specialRollPopUpMessage = "You lost one Posse member. You now have " + posse + " members remaining";
            }
            posseLossSceneProgress = 0; // Reset
            App.setRoot(posseLossFromScene);
        }
        posseItemsDisabled = false;
    }

    @FXML
    private void backToSceneItemUsed() throws IOException {
        if (negatePosseLoss) {
            posseLossLeft = 0;
            totalPosseLossMessage = "Medicine/Bandages used to negate the rest of the Posse loss. You have " + posse + " Posse members remaining";
            totalPosseLoss.setText(totalPosseLossMessage);
            posseLossSceneProgress = 1;
            posseLossPopUpBox.setVisible(true);
        } else if (posseLossLeft > 0) {
            // method that does the rest of the posse loss - reloads this scene if items still available
            posseChange = posseLossLeft;
            for (int i = 0; i < posseChange; i++) {
                posse--;
                posseLossLeft--;
                System.out.println("Player loses one Posse. They now have " + posse + " Posse members remaining");
                posseLossChecks();
                if (stopPosseLoss) {
                    stopPosseLoss = false;
                    posseLossSceneProgress = 0; // Reset
                    App.setRoot("posseLoss");
                    posseCheck();
                    return;
                }
            }
            System.out.println("Player loses " + posseChange + " Posse. They now have " + posse + " Posse members remaining");
            totalPosseLossMessage = "You lost " + posseChange + " more Posse members and now have " + posse + " members remaining";
            totalPosseLoss.setText(totalPosseLossMessage);
            posseLossSceneProgress = 1;
            posseLossPopUpBox.setVisible(true);
        } else {
            negatePosseLoss = false;
            if (posseLossFromScene.equals("specialRoll") && specialRoll.equals("6")) {
                specialRollPopUpMessage = "You did not lose any Posse members. You still have " + posse + " members remaining";
            } else if (posseLossFromScene.equals("rations")) {
                setRationsResult("You did not lose any Posse members but you did use up all your Food. You still have " + posse + " Posse members remaining");
            }
            posseLossSceneProgress = 0; // Reset
            App.setRoot(posseLossFromScene);
        }
    }

    @FXML
    private void backToSceneFinished() throws IOException {
        if (!chooseOption.equals("5") || !negatePosseLoss) {
            negatePosseLoss = false;
        }
        if (posseLossFromScene.equals("specialRoll")) {
            if (specialRoll.equals("6")) {
                specialRollPopUpMessage = "You lost a total of " + (startingPosse - posse) + " Posse member(s). You now have " + posse + " members remaining";
            } else if (specialRoll.equals("Stampede")) {
                specialRollPopUpMessage = "You rolled lower than your total number of Posse members. You lost " + (startingPosse - posse) + " Posse member(s) and now have " + posse + " members remaining";
            } else if (specialRoll.equals("Rattlesnakes")) {
                specialRollPopUpMessage = "You were attacked by rattlesnakes! It is now Day " + day + " and you lost " + (startingPosse-posse) + " Posse members. You have " + posse + " members remaining";
            }
        } else if (posseLossFromScene.equals("rations")) {
            setRationsResult("You lost " + (startingPosse - posse) + " Posse members and used up all your Food. You now have " + posse + " Posse members remaining");
        }
        posseLossSceneProgress = 0; // Reset
        App.setRoot(posseLossFromScene);
    }

    @FXML
    private void usePosseItem() {
        posseLossSceneProgress = 2;
        useItemPopUpBox.setVisible(true);
        // Group radio buttons
        ToggleGroup group = new ToggleGroup();
        medBandagesButton.setToggleGroup(group);
        deserter1Button.setToggleGroup(group);
        deserter2Button.setToggleGroup(group);
        // Add items available
        if (medicineBandagesInventory && !rationsDay) {
            items.getChildren().add(medBandagesButton);
        }
        if (deserter1) {
            items.getChildren().add(deserter1Button);
        }
        if (deserter2) {
            items.getChildren().add(deserter2Button);
        }
    }

    @FXML
    private void selectPosseItem() {
        posseLossSceneProgress = 3;
        useItemPopUpBox.setVisible(false);
        itemUsedPopUpBox.setVisible(true);
        if (medBandagesButton.isSelected()) {
            medicineBandages();
            itemResultMessage = "You've used Medicine/Bandages to negate all Posse loss from a single source";
            itemResult.setText(itemResultMessage);
        } else if (deserter1Button.isSelected()) {
            deserter1Sacrifice();
            itemResultMessage = "You've sacrificed the first Deserter. They will leave your group instead of a member of your Posse";
            itemResult.setText(itemResultMessage);
        } else if (deserter2Button.isSelected()) {
            deserter2Sacrifice();
            itemResultMessage = "You've sacrificed the second Deserter. They will leave your group instead of a member of your Posse";
            itemResult.setText(itemResultMessage);
        }
    }

    public static StringProperty posseLossMessage = new SimpleStringProperty();

    public String getPosseLossMessage() {
        return posseLossMessage.get();
    }

    public static StringProperty posseLossMessageProperty() {
        return posseLossMessage;
    }

    public static void setPosseLossMessage(String posseLossMessage) {
        PosseLoss.posseLossMessage.set(posseLossMessage);
    }
}
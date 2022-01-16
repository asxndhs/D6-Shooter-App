package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.RationsMethods.rationsDay;
import static org.openjfx.Logic.Reset.endOfTurn;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.Scoring.townsVisited;
import static org.openjfx.Logic.TownMethods.FindTown.townSpace;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class ResolveDice implements Initializable {

    public Label resolveMessage;

    public static String onesMessage;
    public static String twosMessage;
    public static String threesMessage;
    public static String hideMessage;
    public static String seekShelterMessage;
    public static String backroadsMessage;
    public static String fightMessage;
    public static String fivesMessage;
    public static String endOfFivesMessage;
    public static String sixesMessage;
    public static String endOfSixesMessage;
    public static Boolean onesChecked = false;
    public static Boolean twosChecked = false;
    public static Boolean threesChecked = false;
    public static Boolean hideChecked = false;
    public static Boolean seekShelterChecked = false;
    public static Boolean backroadsChecked = false;
    public static Boolean fightChecked = false;
    public static Boolean fivesChecked = false;
    public static Boolean fiveSpecialRoll = false;
    public static Boolean sixesChecked = false;
    public static Boolean sixSpecialRoll = false;
    public static Boolean playShootout = false;
    public static String specialRoll = "null";

    public static int resolveDiceSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (resolveDiceSceneProgress == 1) {
            resolveMessage.setText(onesMessage);
        } else if (resolveDiceSceneProgress == 2) {
            resolveMessage.setText(twosMessage);
        } else if (resolveDiceSceneProgress == 3) {
            resolveMessage.setText(threesMessage);
        } else if (resolveDiceSceneProgress == 4) {
            resolveMessage.setText(hideMessage);
        } else if (resolveDiceSceneProgress == 5) {
            resolveMessage.setText(seekShelterMessage);
        } else if (resolveDiceSceneProgress == 6) {
            resolveMessage.setText(backroadsMessage);
        } else if (resolveDiceSceneProgress == 7) {
            resolveMessage.setText(fightMessage);
        } else if (resolveDiceSceneProgress == 8) {
            resolveMessage.setText(fivesMessage);
        } else if (resolveDiceSceneProgress == 9) {
            if (badFiveCounter > 0) {
                endOfFivesMessage = "You rolled " + badFiveCounter + " rolls of 3-6. After losing resources, you now have " + posse + " Posse members and " + food + " Food";
            } else {
                endOfFivesMessage = "You rolled did not roll any rolls of 3-6 and lost no resources as a result";
            }
            resolveMessage.setText(endOfFivesMessage);
        } else if (resolveDiceSceneProgress == 10) {
            resolveMessage.setText(sixesMessage);
        } else if (resolveDiceSceneProgress == 11) {
            endOfSixesMessage = "All 6's re-rolled and you now have " + posse + " Posse members remaining. All dice have been resolved.\nEnd of turn";
            resolveMessage.setText(endOfSixesMessage);
        } else if (resolveDiceSceneProgress == 12) {
            resolveMessage.setText("Shootout ended. End of turn");
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "resolveDice";
        App.setRoot("pause");
    }

    @FXML
    private void continueToNextStep() throws IOException {
        if (ones > 0 && !onesChecked) {
            resolveDiceSceneProgress = 1;
            if (townSpace) {
                onesMessage = "No movement allowed this turn as you have reached a Town";
            } else {
                oneRoll();
                if (compassUsed > 0) {
                    onesMessage = "Compass ability used. You have rolled " + (ones-compassUsed) + " 1(s) and " + compassUsed + " Red 1(s). You move forward " + movement + " space(s) to space " + playerSpace;
                    compassUsed = 0;
                } else {
                    onesMessage = "You have rolled " + ones + " 1(s) and move forward " + movement + " space(s) to space " + playerSpace;
                }
            }
            resolveMessage.setText(onesMessage);
            onesChecked = true;
        } else if (twos > 0 && !twosChecked) {
            resolveDiceSceneProgress = 2;
            twoRoll();
            if (hunterUsed > 0) {
                twosMessage = "Hunter ability used: You have rolled " + (twos-hunterUsed) + " 2(s) and " + hunterUsed + " Red 2(s). You gain " + foodChange + " Food and now have " + food + " Food";
                hunterUsed = 0;
            } else {
                twosMessage = "You have rolled " + twos + " 2(s) and gain " + foodChange + " Food. You now have " + food + " Food";
            }
            resolveMessage.setText(twosMessage);
            twosChecked = true;
        } else if (threes > 0 && !threesChecked) {
            resolveDiceSceneProgress = 3;
            threeRoll();
            if (prospectorsMapUsed) {
                prospectorsMapUsed = false;
                threesMessage = "Prospector's Map ability used. You have rolled " + threes + " 3(s) and gain " + goldChange + " Gold. You now have " + gold + " Gold";
            } else {
                threesMessage = "You have rolled " + threes + " 3(s) and gain " + goldChange + " Gold. You now have " + gold + " Gold";
            }
            resolveMessage.setText(threesMessage);
            threesChecked = true;
        } else if (hide > 0 && !hideChecked) {
            resolveDiceSceneProgress = 4;
            hideRoll();
            resolveMessage.setText(hideMessage);
        } else if (seekShelter > 0 && !seekShelterChecked) {
            resolveDiceSceneProgress = 5;
            seekShelterRoll();
            seekShelterMessage = "You have " + seekShelter + " Seek Shelter dice.\n" + fivesChange + " 5(s) have been removed";
            resolveMessage.setText(seekShelterMessage);
            seekShelterChecked = true;
        } else if (backroads > 0 && !backroadsChecked) {
            resolveDiceSceneProgress = 6;
            if (townSpace) {
                backroadsMessage = "No movement allowed this turn as you have reached a Town";
            } else {
                backroadsRoll();
                backroadsMessage = "You have " + backroads + " Backroads dice.\nYou move forward " + movement + " space(s)\n\nMoving to space " + playerSpace;
            }
            resolveMessage.setText(backroadsMessage);
            backroadsChecked = true;
        } else if (fight > 0 && !fightChecked && campfireEvent) {
            resolveDiceSceneProgress = 7;
            fight++;
            fightMessage = "Campfire Songs: You now have " + fight + " Fight dice. These will be used during the Shootout";
            resolveMessage.setText(fightMessage);
            fightChecked = true;
        } else if (fives > 0 && !fivesChecked) {
            resolveDiceSceneProgress = 8;
            fivesMessage = "You have rolled " + fives + " 5s. These dice will be re-rolled and for every 3-6 rolled, you will lose either two Food or one Posse member";
            resolveMessage.setText(fivesMessage);
            specialRoll = "5";
            fiveSpecialRoll = true;
            fivesChecked = true;
        } else if (fiveSpecialRoll) {
            resolveDiceSceneProgress = 9;
            fiveSpecialRoll = false;
            App.setRoot("specialRoll");
        } else if (sixes > 0 && !sixesChecked) {
            resolveDiceSceneProgress = 10;
            if (fight > 0) {
                // Start a Shootout
                sixesMessage = "You have rolled " + sixes + " 6s and have " + fight + " Fight dice. Heading to the Shootout";
                resolveMessage.setText(sixesMessage);
                playShootout = true;
            } else {
                // Re-roll 6s
                sixesMessage = "You have rolled " + sixes + " 6s. These dice will be re-rolled and for every 3-6 rolled, you will lose one Posse member";
                resolveMessage.setText(sixesMessage);
                specialRoll = "6";
                sixSpecialRoll = true;;
            }
            sixesChecked = true;
        } else if (sixSpecialRoll) {
            resolveDiceSceneProgress = 11;
            sixSpecialRoll = false;
            App.setRoot("specialRoll");
        } else if (playShootout) {
            resolveDiceSceneProgress = 12;
            resolveMessage.setText("Shootout ended. End of turn");
            App.setRoot("shootout");
            playShootout = false;
        } else if (townSpace) {
            resolveDiceSceneProgress = 0;
            loseEventAbility();
            if (endEvent) {
                endEvent = false;
                return;
            }
            posseChange = 0; // Reset for shop
            townsVisited++; // Town stat tracking
            App.setRoot("town");
        } else {
            resolveDiceSceneProgress = 0;
            setPlayerSpaceLabel("Player space: " + playerSpace);
            endOfTurn();
            if (!rationsDay) {
                App.setRoot("game");
            }
        }
    }
}
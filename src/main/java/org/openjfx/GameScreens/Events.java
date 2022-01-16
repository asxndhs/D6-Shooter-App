package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.ChooseOption.*;
import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.GameScreens.SpecialRoll.*;
import static org.openjfx.Logic.EventMethods.EventCall.*;
import static org.openjfx.Logic.EventMethods.SimplifiedEvents.*;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.GameScreens.ResolveDice.*;
import static org.openjfx.Logic.Map.*;

public class Events implements Initializable {

    public Label eventMessage;
    public Button eventButton;
    public VBox eventPopUpBox;
    public Label popUpMessage;
    public VBox movementPopUpBox;
    public Label movementMessage;

    public static int eventProgress = 1;
    public static Boolean specialEvent = false;
    public static Boolean specialEventUsed = false;

    public static int eventsSceneProgress = 0;
    public static String eventPopupMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (eventsSceneProgress == 1) {
            // Event revealed
            changeEventMessage();
        } else if (eventsSceneProgress == 2) {
            // Event has a pop up message
            popUpMessage.setText(eventPopupMessage);
            eventPopUpBox.setVisible(true);
        } else if (eventsSceneProgress == 3) {
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            movementPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "events";
        App.setRoot("pause");
    }

    @FXML
    private void revealEvent() throws IOException {

        if (eventProgress == 1) {
            loseEventAbility();
            if (endEvent) {
                endEvent = false;
                return;
            }
            eventsSceneProgress = 1;
            eventType();
            changeEventMessage();
            nextScene = "Resolve Dice";
            eventButton.setText("Continue"); // Change eventButton text and onAction
        } else if (eventProgress == 2) {
            // If event needs to be resolved without dice roll/options, pop up message appears
            eventProgress = 6;
            eventsSceneProgress = 2;
            changePopUpMessage();
            eventPopUpBox.setVisible(true);
        } else if (eventProgress == 3) {
            // If event requires special roll, change to specialRoll scene
            App.setRoot("specialRoll");
        } else if (eventProgress == 4) {
            // If event requires player to pick options, change to chooseOption scene
            checkOptionRequirements();
            optionChosen = false;
            App.setRoot("chooseOption");
        } else if (eventProgress == 5) {
            // If event is wagon train, change to wagonTrain scene
            App.setRoot("wagonTrain");
        } else if (eventProgress == 6) {
            eventSpace = false;
            if (extraMovement > 0) {
                eventsSceneProgress = 3;
                moveForward();
                movementPopUpBox.setVisible(true);
                movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            } else {
                // If event is over, continue button will move to next scene
                eventsSceneProgress = 0; // Reset
                if (specialEvent) {
                    specialEvent = false;
                    setPlayerSpaceLabel("Player space: " + playerSpace);
                    App.setRoot("game");
                } else {
                    App.setRoot("resolveDice");
                }
            }
        }
    }

    private void changeEventMessage() {

        if (eventTypeFull) {
            if (fullEventNumber == 11) {
                eventProgress = 6; // Continue
                eventMessage.setText("TUMBLING TUMBLEWEEDS - A tumbleweed tumbles on by and runs right into a cactus.");
            } else if (fullEventNumber == 12) {
                eventProgress = 2; // Pop-up --> Roll dice
                eventMessage.setText("STAMPEDE! - Roll two dice. If you roll lower than the total number of your Posse, " +
                        "lose 2 Posse members.");
                numberOfDice = 2;
                specialRoll = "Stampede";
            } else if (fullEventNumber == 13) {
                eventProgress = 2; // Pop-up --> Roll dice
                eventMessage.setText("RATTLESNAKES! - Roll one die per Posse member. Add 1 Day for the first 6 rolled. " +
                        "Lose 1 Posse member for any additional 6’s rolled.");
                numberOfDice = posse;
                specialRoll = "Rattlesnakes";
            } else if (fullEventNumber == 14) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("BILLY ESCAPES! - Billy escapes while your Posse is resting. You are able to track " +
                        "him down, but it costs you some time. If the nearest Town is behind you, add 1 Day. If the " +
                        "nearest Town is ahead of you, move ahead 2 spaces and add 1 Day. If distance is equal, add 1 Day.");
            } else if (fullEventNumber == 15) {
                eventProgress = 4; // Choose option --> roll dice
                eventMessage.setText("DEER HERD - A herd of deer passes by, and you see a lot more down across the " +
                        "valley. You may spend 1 Ammo to gain 2 Food OR you may spend 2 Ammo and add 1 Day to roll a " +
                        "die and get Food equal to the number rolled +2.");
                chooseOption = "Deer Herd";
                setChooseOptionMessage("Do you want to go hunting?");
            } else if (fullEventNumber == 16) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("THERE'S GOLD IN THEM HILLS! - Until you reach the next Town or Event, you may " +
                        "change any one die to a 3 on the first roll of each turn. (Red 5’s and 6’s may not be changed)");
            } else if (fullEventNumber == 22) {
                eventProgress = 5; // Shop
                eventMessage.setText("WAGON TRAIN - You may buy or sell (2 Food or 2 Ammo = 1 Gold)");
            } else if (fullEventNumber == 23) {
                eventProgress = 5; // Shop
                eventMessage.setText("WAGON TRAIN - You may buy or sell (2 Food or 2 Ammo = 1 Gold)");
            } else if (fullEventNumber == 24) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("SLOW TRAIN CROSSING - No further movement can be made on this turn (with 1’s or 4’s).");
            } else if (fullEventNumber == 25) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("DOWNHILL RIDE - You always have an extra 1 die available until you reach the next " +
                        "Town or Event.");
            } else if (fullEventNumber == 26) {
                eventProgress = 4; // Choose option
                eventMessage.setText("LOST GUNSIGHT MINE - You have found the legendary Lost Gunsight Mine, but the " +
                        "heat of the valley is particularly brutal. You may gain 5 Gold, but if so, then until you " +
                        "reach the next Town or Event, you may not use 4’s to Seek Shelter.");
                chooseOption = "Lost Gunsight Mine";
                setChooseOptionMessage("Do you want to venture down the mine?");
            } else if (fullEventNumber == 33) {
                eventProgress = 4; // Choose option --> roll dice
                eventMessage.setText("SHORTCUT? - One of your Posse suggests a shortcut. If you decide to try it, roll " +
                        "a die and subtract 3 from the number rolled. If the total is positive, move ahead that many " +
                        "spaces. If the total is negative, add 1 Day.");
                chooseOption = "Shortcut";
                setChooseOptionMessage("Do you want to take the shortcut?");
            } else if (fullEventNumber == 34) {
                eventProgress = 4; // Choose option
                eventMessage.setText("POSSE DEMANDS - Your Posse is demanding more money and food. Lose 2 Gold or 2 " +
                        "Food. OR Lose 1 Gold and 1 Food. OR Lose 2 Posse members. If you cannot fully meet one of " +
                        "those options, you lose the game. ");
                chooseOption = "Posse Demands";
                setChooseOptionMessage("Pick which resources to give up to the demands");
            } else if (fullEventNumber == 35) {
                eventProgress = 2; // Pop-up --> Roll dice
                eventMessage.setText("FISHING HOLE - Roll a die for each Posse member. Gain 1 Food for each 1-2 rolled. ");
                numberOfDice = posse;
                specialRoll = "Fishing Hole";
            } else if (fullEventNumber == 36) {
                eventProgress = 4; // Choose option
                eventMessage.setText("BANDITS! - Bandits attack your Posse. You may give them 3 Gold or 3 Food, or 2 " +
                        "Gold and 2 Food, or 2 Gold, 1 Food and 1 Ammo, or you may try to fight them off. If you fight, " +
                        "there is a Shootout with one die for you and two dice for the Bandits.");
                chooseOption = "Bandits";
                setChooseOptionMessage("Choose how you want to deal with the bandits");
            } else if (fullEventNumber == 44) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("PRISONER - The next time you roll a Red 6, put the die on this sheet to take it " +
                        "“prisoner”. If you lose a Shootout before you reach the next Town, return the die and lose 1 " +
                        "Gold (if you have it). If you reach the next Town with the die still on this sheet, gain 3 Gold" +
                        " and return the die. While on this sheet, the die does not take part in any Shootouts that occur.");
            } else if (fullEventNumber == 45) {
                eventProgress = 4; // Choose option
                eventMessage.setText("CAMPFIRE SONGS - You may spend 1 Food to stop early for the night and make a " +
                        "campfire to share stories and sing songs with your Posse, which would be good for their morale. " +
                        "If you do that, add an extra die to your Fight dice pools for all Shootouts you" +
                        " are involved in until you reach the next Event or Town.");
                chooseOption = "Campfire Songs";
                setChooseOptionMessage("Do you want to stop by the campfire for the night?");
            } else if (fullEventNumber == 46) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("CRITTERS EVERYWHERE - You always have an extra 2 die available until you reach " +
                        "the next Town or Event.");
            } else if (fullEventNumber == 55) {
                eventProgress = 4; // Choose option
                eventMessage.setText("LOST FAMILY - You come across a pioneer family that is lost. If you stop to help " +
                        "them, add 1 Day. They have nothing to offer in return but their thanks and a blessing. On your " +
                        "next turn you may lock one White die and one Red die on any number, before rolling any dice.");
                chooseOption = "Lost Family";
                setChooseOptionMessage("Do you want to help the lost family?");
            } else if (fullEventNumber == 56) {
                eventProgress = 4; // Choose option
                eventMessage.setText("ARMY DESERTERS - You come across two Army deserters. You may add them to your " +
                        "Posse (but they do not count towards your Posse members in game). You may remove them from " +
                        "your Posse in place of Posse loss at any time. Before each turn, roll a die for each deserter " +
                        "remaining. If you roll a 5, that person has deserted you. If you roll a 6, that person has " +
                        "deserted you and you lose 1 Food and 1 Gold (or as much Food/Gold as you have).");
                chooseOption = "Army Deserters";
                setChooseOptionMessage("Do you want to take on the deserters?");
            } else if (fullEventNumber == 66) {
                eventProgress = 2; // Pop-up
                eventMessage.setText("INDIAN GUIDE - You come across an Indian who knows this area very well. He joins " +
                        "you for part of your journey, but he is never considered to be part of your Posse. Before each " +
                        "turn, take one Red die and lock it, with whatever number you choose. You may not unlock that " +
                        "die this turn. The Indian Guide moves on when you reach the next Event or Town.");
            }
        } else {
            if (simpleEventDie == 1) {
                eventProgress = 2; // Pop-up message
                eventMessage.setText("SHORTCUT - Move 3 spaces ahead");
            } else if (simpleEventDie == 2) {
                eventProgress = 4; // Choose option
                eventMessage.setText("ANIMAL HERD - You may spend 1 Ammo for 2 Food");
                chooseOption = "Animal Herd";
                setChooseOptionMessage("Do you want to go hunting?");
            } else if (simpleEventDie == 3) {
                eventProgress = 5; // Shop
                eventMessage.setText("WAGON TRAIN - You may buy or sell (2 Food or 2 Ammo = 1 Gold)");
            } else if (simpleEventDie == 4) {
                eventProgress = 6; // Continue
                eventMessage.setText("OPEN ROAD - Nothing happens");
            } else if (simpleEventDie == 5) {
                eventProgress = 2; // Pop-up message
                eventMessage.setText("LOST - 1 Day is added and you lose 1 Food");
            } else if (simpleEventDie == 6) {
                eventProgress = 4; // Choose option
                eventMessage.setText("DEMANDS - Lose 1 Posse or lose 1 Gold and 1 food");
                chooseOption = "Demands";
                setChooseOptionMessage("Pick which resources to give up to the demands");
            }
        }
    }

    private void changePopUpMessage() throws IOException {

        if (eventTypeFull) {
            if (fullEventNumber == 12) {
                eventPopupMessage = "Roll two dice to determine if any Posse members are lost to the stampede";
                popUpMessage.setText(eventPopupMessage);
                eventProgress = 3;
            } else if (fullEventNumber == 13) {
                eventPopupMessage = "Roll " + posse + " dice to determine how many Posse members are lost to the rattlesnakes";
                popUpMessage.setText(eventPopupMessage);
                eventProgress = 3;
            } else if (fullEventNumber == 14) {
                billyEscapes();
                eventPopupMessage = event14Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 16) {
                theresGoldInThemHills();
                eventPopupMessage = event16Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 24) {
                slowTrainCrossing();
                eventPopupMessage = event24Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 25) {
                downhillRide();
                eventPopupMessage = event25Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 35) {
                eventPopupMessage = "Roll " + posse + " dice to determine how much Food is gathered at the fishing hole";
                popUpMessage.setText(eventPopupMessage);
                eventProgress = 3;
            } else if (fullEventNumber == 44) {
                prisoner();
                eventPopupMessage = event44Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 46) {
                crittersEverywhere();
                eventPopupMessage = event46Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (fullEventNumber == 66) {
                indianGuide();
                eventPopupMessage = event66Message;
                popUpMessage.setText(eventPopupMessage);
            }
        } else {
            if (simpleEventDie == 1) {
                shortcut();
                eventPopupMessage = event1Message;
                popUpMessage.setText(eventPopupMessage);
            } else if (simpleEventDie == 5) {
                lost();
                eventPopupMessage = event5Message;
                popUpMessage.setText(eventPopupMessage);
            }
        }
    }
}
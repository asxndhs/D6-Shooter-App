package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Events.specialEvent;
import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.PickRoute.pickRouteFromScene;
import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.GameScreens.ResolveDice.*;
import static org.openjfx.GameScreens.ChooseOption.*;
import static org.openjfx.GameScreens.Pause.*;
import static org.openjfx.Logic.Day.day;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.GameOverMethods.finalDayCheck;
import static org.openjfx.Logic.GameOverMethods.posseCheck;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.RationsMethods.checkRations;
import static org.openjfx.Logic.RationsMethods.rationsFromScene;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.Scoring.spacesTravelled;

public class SpecialRoll implements Initializable {

    public Label dice1 = new Label();
    public Label dice2 = new Label();
    public Label dice3 = new Label();
    public Label dice4 = new Label();
    public Label dice5 = new Label();
    public Label dice6 = new Label();
    public Label dice7 = new Label();
    public Label dice8 = new Label();
    public Label dice9 = new Label();
    public Label dice10 = new Label();
    public Label dice11 = new Label();
    public Label dice12 = new Label();
    public Label dicePlaceholder;
    public HBox diceBox;
    public VBox specialRollPopUpBox;
    public Label popUpMessage;
    public VBox movementPopUpBox;
    public Label movementMessage;

    public static ArrayList<Integer> specialRollDiceArray = new ArrayList<Integer>();
    public static String chooseOption = "null";
    public static Boolean rollFinished = false;
    public static Boolean popUpRequired = false;
    public static int numberOfDice;
    ArrayList<Label> diceLabel = new ArrayList<Label>();
    public static String specialRollPopUpMessage;

    public static int specialRollSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (specialRollSceneProgress == 1) {
            diceBox.getChildren().remove(dicePlaceholder);
            addSpecialDiceToScene();
        } else if (specialRollSceneProgress == 2) {
            diceBox.getChildren().remove(dicePlaceholder);
            addSpecialDiceToScene();
            popUpMessage.setText(specialRollPopUpMessage);
            specialRollPopUpBox.setVisible(true);
        } else if (specialRollSceneProgress == 3) {
            movementPopUpBox.setVisible(true);
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "specialRoll";
        App.setRoot("pause");
    }

    @FXML
    private void continueToNextPhase() throws IOException {
        if (stopPosseLoss) {
            // Check game over otherwise go to posse loss scene
            stopPosseLoss = false;
            App.setRoot("posseLoss");
            posseCheck();
        } else if (popUpRequired) {
            specialRollSceneProgress = 2;
            popUpMessage.setText(specialRollPopUpMessage);
            specialRollPopUpBox.setVisible(true);
            popUpRequired = false;
        } else if (rollFinished) {
            eventSpace = false;
            if (extraMovement > 0) {
                specialRollSceneProgress = 3;
                moveForward(); // if player still has movement for after the event
                movementPopUpBox.setVisible(true);
                movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            } else {
                specialRollSceneProgress = 0;
                rollFinished = false;
                if (specialEvent) {
                    specialEvent = false;
                    setPlayerSpaceLabel("Player space: " + playerSpace);
                    App.setRoot("game");
                } else switch (specialRoll) {
                    case "5":
                        if (badFiveCounter > 0) {
                            chooseOption = "5";
                            numberOfChoices = badFiveCounter;
                            foodChange = 0;
                            posseChange = 0;
                            nextScene = "Resolve Dice";
                            optionChosen = false;
                            checkOptionRequirements();
                            setChooseOptionMessage("Pick which resources you will lose from the options below");
                            App.setRoot("chooseOption");
                        } else {
                            App.setRoot("resolveDice");
                        }
                        break;
                    case "6":
                    case "Stampede":
                    case "Rattlesnakes":
                    case "Deer Herd":
                    case "Shortcut":
                    case "Fishing Hole":
                        App.setRoot("resolveDice");
                        break;
                }
            }
        }
    }

    @FXML
    public void rollSpecialDice() throws IOException {

        if (rollFinished) {
            return;
        }

        specialRollSceneProgress = 1;
        popUpRequired = false;
        switch (specialRoll) {
            case "5":
                fiveRoll();
                rollFinished = true;
                if (badFiveCounter > 0) {
                    specialRollPopUpMessage = "You rolled " + badFiveCounter + " rolls of 3-6. Pick which resources you will lose";
                } else {
                    specialRollPopUpMessage = "You did not roll any rolls of 3-6";
                }
                popUpRequired = true;
                break;
            case "6":
                sixRoll();
                rollFinished = true;
                if (posseChange > 0) {
                    specialRollPopUpMessage = "You rolled " + posseChange + " rolls of 3-6 and lose " + posseChange + " Posse member(s) as a result.\nYou have " + posse + " Posse members remaining";
                } else {
                    specialRollPopUpMessage = "You rolled no rolls of 3-6 and lose no Posse members as a result";
                }
                popUpRequired = true;
                break;
            case "Stampede":
                eventSpecialRoll();
                rollFinished = true;
                int stampedeTotal = specialRollDiceArray.get(0) + specialRollDiceArray.get(1);
                System.out.println("Player rolled " + stampedeTotal + " and has " + posse + " Posse");
                if (stampedeTotal < posse) {
                    posseChange = 2;
                    posseLossFromScene = "specialRoll";
                    posseLossLoop();
                    System.out.println("Player rolled lowed than total Posse members. Player loses two Posse");
                    System.out.println("Player now has " + posse + " Posse remaining");
                    specialRollPopUpMessage = "You rolled lower than your total number of Posse members. You lose two Posse and now have " + posse + " Posse members remaining";
                } else {
                    System.out.println("Player did not roll lower than total Posse members. Continue");
                    specialRollPopUpMessage = "You did not roll lower than your total number of Posse members. You avoid the stampede";
                }
                popUpRequired = true;
                break;
            case "Rattlesnakes":
                eventSpecialRoll();
                rollFinished = true;
                int sixCounter = 0;
                for (int i = 0; i < specialRollDiceArray.size(); i++) {
                    if (specialRollDiceArray.get(i) == 6) {
                        sixCounter++;
                    }
                }
                System.out.println("Player rolled " + sixCounter + " 6s");
                if (sixCounter > 0) {
                    day++;
                    finalDayCheck(); // Check it hasn't passed the end of day 40
                    rationsFromScene = "specialRoll";
                    checkRations(); // Rations check before moving onto next day
                    // TODO: if game is over due to final day, return and stop this method - should be fixed, test
                    System.out.println("One day added and " + (sixCounter-1) + " Posse lost");
                    posseChange = sixCounter-1; // Set posse change counter
                }
                System.out.println("It is now Day " + day + " and player has " + posse + " Posse remaining");
                if (sixCounter > 1) {
                    posseLossFromScene = "specialRoll";
                    posseLossLoop();
                    specialRollPopUpMessage = "You rolled " + sixCounter + " 6's. It is now Day " + day + " and you lost " + (startingPosse-posse) + " Posse members. You have " + posse + " members remaining";
                } else if (sixCounter > 0) {
                    specialRollPopUpMessage = "You rolled one 6. It is now Day " + day + " but you did not lose any Posse members";
                } else {
                    specialRollPopUpMessage = "You did not roll any 6's. You have avoided the rattlesnakes";
                }
                popUpRequired = true;
                break;
            case "Deer Herd":
                eventSpecialRoll();
                rollFinished = true;
                foodChange = specialRollDiceArray.get(0) + 2;
                // Set max food
                if (foodChange+food > 12) {
                    foodChange = 12 - food;
                    System.out.println("Player has reached max Food");
                }
                System.out.println("Player has spent two Ammo and lost one day");
                System.out.println("Player rolled " + specialRollDiceArray.get(0) + " and gains " + foodChange + " Food");
                food+=foodChange;
                System.out.println("It is now Day " + day + ". Player has " + ammo + " Ammo and " + food + " Food");
                specialRollPopUpMessage = "You rolled a " + specialRollDiceArray.get(0) + ". You gain " + foodChange + " Food and now have " + food + " Food";
                popUpRequired = true;
                break;
            case "Shortcut":
                eventSpecialRoll();
                rollFinished = true;
                System.out.println("Player rolled a " + specialRollDiceArray.get(0));
                if (specialRollDiceArray.get(0) < 3) {
                    day++;
                    finalDayCheck(); // Check it hasn't passed the end of day 40
                    rationsFromScene = "specialRoll";
                    checkRations(); // Rations check before moving onto next day
                    System.out.println("One day added.\nIt is now Day " + day);
                    specialRollPopUpMessage = "You rolled a " + specialRollDiceArray.get(0) + ". You do not move forward and one day has been added. It is not Day " + day;
                } else {
                    eventSpace = false; // needs to be changed to allow shortcut to move properly
                    movement = specialRollDiceArray.get(0) - 3;
                    movement += extraMovement;
                    currentSpace = playerSpace;
                    pickRouteFromScene = "specialRoll";
                    for (int i = 0; i < movement; i++) {
                        playerSpace++;
                        spacesTravelled++; // Stat tracking for spaces travelled
                        if (extraMovement > 0) {
                            extraMovement--;
                        }
                        spaceChecks();
                        if (eventSpace || pickRouteSpace) {
                            return;
                        }
                    }
                    System.out.println("Player moves forward " + (playerSpace - currentSpace) + " spaces.\nPlayer is now on space " + playerSpace);
                    specialRollPopUpMessage = "You rolled a " + specialRollDiceArray.get(0) + ". You move forward " + (playerSpace - currentSpace) + " spaces onto space " + playerSpace;
                }
                popUpRequired = true;
                break;
            case "Fishing Hole":
                eventSpecialRoll();
                rollFinished = true;
                int fishCaught = 0; //  Reset food change variable
                for (int i = 0; i < specialRollDiceArray.size(); i++) {
                    if (specialRollDiceArray.get(i) < 3) {
                        fishCaught++;
                    }
                }
                // Set max food
                if (fishCaught+food > 12) {
                    foodChange = 12 - food;
                    System.out.println("Player has reached max Food");
                } else {
                    foodChange = fishCaught;
                }
                food+= foodChange;
                System.out.println("Player caught " + fishCaught + " fish. Player gains " + foodChange + " Food");
                System.out.println("Player now has " + food + " Food");
                specialRollPopUpMessage = "You caught " + fishCaught + " fish and now have " + food + " Food";
                popUpRequired = true;
                break;
            default:
                rollFinished = true;
                break;
        }

        diceBox.getChildren().remove(dicePlaceholder);
        addSpecialDiceToScene();
    }

    private void addSpecialDiceToScene() {

        diceLabel.add(dice1);
        diceLabel.add(dice2);
        diceLabel.add(dice3);
        diceLabel.add(dice4);
        diceLabel.add(dice5);
        diceLabel.add(dice6);
        diceLabel.add(dice7);
        diceLabel.add(dice8);
        diceLabel.add(dice9);
        diceLabel.add(dice10);
        diceLabel.add(dice11);
        diceLabel.add(dice12);

        for (int i = 0; i < specialRollDiceArray.size(); i++) {
            diceLabel.get(i).setText("Dice " + (i+1) + ": " + specialRollDiceArray.get(i));
            diceBox.getChildren().add(diceLabel.get(i));
        }
    }

    private void eventSpecialRoll() {

        // Reset variables
        specialRollDiceArray.clear(); // Reset special roll dice array to zeros

        // Re-roll fives to determine effect
        for (int i = 0; i < numberOfDice; i++) {
            specialRollDiceArray.add((int)(Math.random()*6+1));
            System.out.println(i + " dice rolled a " + specialRollDiceArray.get(i));
            if (specialRollDiceArray.get(i) >= 3) {
                badFiveCounter++;
            }
        }
    }
}
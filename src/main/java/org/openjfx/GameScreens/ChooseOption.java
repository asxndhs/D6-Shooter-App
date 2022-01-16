package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Events.specialEvent;
import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.GameOver.setGameOverMessage;
import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.GameScreens.SpecialRoll.*;
import static org.openjfx.GameScreens.ResolveDice.*;
import static org.openjfx.GameScreens.Pause.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.GameOverMethods.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.RationsMethods.checkRations;
import static org.openjfx.Logic.RationsMethods.rationsFromScene;
import static org.openjfx.Logic.Scoring.ammoUsed;
import static org.openjfx.Logic.ShootoutMethods.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.negatePosseLoss;

public class ChooseOption implements Initializable {

    public ChoiceBox optionsChoiceBox;
    public VBox chooseOptionPopUpBox;
    public Label popUpMessage;
    public VBox movementPopUpBox;
    public Label movementMessage;
    public static Boolean optionChosen = false;
    public static Boolean optionAvailable = false;
    public static int numberOfChoices;
    public static String nextScene;
    public static Boolean reloadChoices = false;
    public static String chooseOptionPopUpMessage;

    public static int chooseOptionSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (chooseOptionSceneProgress == 1) {
            popUpMessage.setText(chooseOptionPopUpMessage);
            chooseOptionPopUpBox.setVisible(true);
        } else if (chooseOptionSceneProgress == 2) {
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            movementPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "chooseOption";
        App.setRoot("pause");
    }

    @FXML
    private void switchToNextPhase() throws IOException {
        if (stopPosseLoss) {
            // Check game over otherwise go to posse loss scene
            stopPosseLoss = false;
            App.setRoot("posseLoss");
            posseCheck();
        } else if (reloadChoices) {
            chooseOptionSceneProgress = 0;
            reloadChoices = false;
            checkOptionRequirements();
            App.setRoot("chooseOption");
        } else if (optionChosen) {
            chooseOptionSceneProgress = 0;
            negatePosseLoss = false;
            switch (nextScene) {
                case "Resolve Dice":
                    eventSpace = false;
                    if (extraMovement > 0) {
                        chooseOptionSceneProgress = 2;
                        moveForward();
                        movementPopUpBox.setVisible(true);
                        movementMessage.setText("Moving additional spaces onto space " + playerSpace);
                    } else {
                        if (specialEvent) {
                            specialEvent = false;
                            setPlayerSpaceLabel("Player space: " + playerSpace);
                            App.setRoot("game");
                        } else {
                            App.setRoot("resolveDice");
                        }
                    }
                    break;
                case "Special Roll":
                    App.setRoot("specialRoll");
                    break;
                case "Shootout":
                    App.setRoot("shootout");
                    break;
            }
        }
    }

    @FXML
    private void choiceBoxSelect() throws IOException {
        if (!optionChosen) {
            chooseOptionSceneProgress = 1;
            Object selectedItem = optionsChoiceBox.getSelectionModel().getSelectedItem();
            int selectedIndex = optionsChoiceBox.getSelectionModel().getSelectedIndex();
            System.out.println(selectedItem + " selected");
            System.out.println("At index " + selectedIndex);
            switch (String.valueOf(selectedItem)) {
                case "Lose two Food":
                    food -= 2;
                    foodChange += 2; // Remove if not needed for stat tracking
                    System.out.println("Player now has " + food + " Food");
                    chooseOptionPopUpMessage = "You now have " + food + " Food";
                    break;
                case "Lose one Posse member":
                    // Add method where if med/bandages has been used for 5 rolls, rest of posse loss is ignored
                    if (chooseOption.equals("5") && negatePosseLoss) {
                        chooseOptionPopUpMessage = "Posse loss negated by Medicine/Bandages";
                    } else {
                        posseChange = 1;
                        posseLossFromScene = "chooseOption";
                        posseLossLoop();
                        chooseOptionPopUpMessage = "You lose " + posseChange + " Posse member";
                    }
                    break;
                case "Spend one Ammo to gain two Food":
                    ammo--;
                    ammoUsed++; // Stat tracking for ammo used
                    foodChange = 2;
                    if (foodChange+food > 12) {
                        foodChange = 12 - food;
                        System.out.println("Player has reached max Food");
                    }
                    food += foodChange;
                    System.out.println("Player now has " + ammo + " Ammo");
                    System.out.println("Player now has " + food + " Food");
                    chooseOptionPopUpMessage = "You go hunting and now have " + food + " Food and " + ammo + " Ammo";
                    break;
                case "Skip hunting":
                case "Skip the mine":
                case "Skip the shortcut":
                case "Skip the campfire":
                case "Ignore the family":
                case "Ignore the deserters":
                    System.out.println("Event skipped");
                    chooseOptionPopUpMessage = "Event skipped. Continue";
                    break;
                case "Lose one Gold and one Food":
                    gold--;
                    goldChange++; // Remove if not needed for stat tracking
                    food--;
                    foodChange++; // Remove if not needed for stat tracking
                    System.out.println("Player now has " + gold + " Gold");
                    System.out.println("Player now has " + food + " Food");
                    chooseOptionPopUpMessage = "You now have " + gold + " Gold and " + food + " Food";
                    break;
                case "Spend two Ammo and one Day to get Food equal to a dice roll +2":
                    ammo -=2;
                    ammoUsed+=2; // Stat tracking for ammo used
                    System.out.println("Player now has " + ammo + " Ammo");
                    day++;
                    finalDayCheck(); // Check it hasn't passed the end of day 40
                    rationsFromScene = "chooseOption";
                    checkRations(); // Rations check before moving onto next day
                    System.out.println("It is now Day " + day);
                    nextScene = "Special Roll";
                    numberOfDice = 1;
                    specialRoll = "Deer Herd";
                    chooseOptionPopUpMessage = "It is now Day " + day + " and you have " + ammo + " Ammo. Continue to roll for Food";
                    break;
                case "Gain five Gold but lose the ability to use 4's for Seek Shelter":
                    gold+=5;
                    goldChange+=5;
                    System.out.println("Player now has " + gold + " Gold");
                    lostGunsightMine();
                    chooseOptionPopUpMessage = "You venture down the mine and now have " + gold + " Gold";
                    break;
                case "Roll a die, subtract 3 and move that many spaces forwards. If the result is negative, add one Day instead":
                    nextScene = "Special Roll";
                    numberOfDice = 1;
                    specialRoll = "Shortcut";
                    chooseOptionPopUpMessage = "You have taken the shortcut. Continue to roll the effects of the shortcut";
                    break;
                case "Lose two Gold":
                    gold-=2;
                    goldChange+=2;
                    System.out.println("Player now has " + gold + " Gold");
                    chooseOptionPopUpMessage = "You now have " + gold + " Gold";
                    break;
                case "Lose two Posse members":
                    posseChange = 2;
                    posseLossFromScene = "chooseOption";
                    posseLossLoop();
                    chooseOptionPopUpMessage = "You lose " + posseChange + " Posse members";
                    break;
                case "Lose three Gold":
                    gold-=3;
                    goldChange+=3;
                    System.out.println("Player now has " + gold + " Gold");
                    chooseOptionPopUpMessage = "You now have " + gold + " Gold";
                    break;
                case "Lose three Food":
                    food -= 3;
                    foodChange += 3;
                    System.out.println("Player now has " + food + " Food");
                    chooseOptionPopUpMessage = "You now have " + food + " Food";
                    break;
                case "Lose two Gold and two Food":
                    gold-=2;
                    goldChange+=2;
                    food -= 2;
                    foodChange += 2;
                    System.out.println("Player now has " + gold + " Gold");
                    System.out.println("Player now has " + food + " Food");
                    chooseOptionPopUpMessage = "You now have " + gold + " Gold and "+ food + " Food";
                    break;
                case "Lose two Gold, one Food and one Ammo":
                    gold-=2;
                    goldChange+=2;
                    food--;
                    foodChange++;
                    ammo--;
                    System.out.println("Player now has " + gold + " Gold");
                    System.out.println("Player now has " + food + " Food");
                    System.out.println("Player now has " + ammo + " Ammo");
                    chooseOptionPopUpMessage = "You now have " + gold + " Gold, "+ food + " Food and " + ammo + " Ammo";
                    break;
                case "Start a Shootout (with only one Fight dice and two opponent Shootout dice)":
                    nextScene = "Shootout";
                    banditsShootout = true;
                    eventPlayerDice = 1;
                    eventOpponentDice = 2;
                    chooseOptionPopUpMessage = "You have decided to fight off the bandits. Continue to begin the Shootout";
                    break;
                case "Spend one Food to gain one extra Fight dice for Shootouts":
                    food--;
                    foodChange++;
                    System.out.println("Player now has " + food + " Food");
                    campfireSongs();
                    chooseOptionPopUpMessage = "You have stopped early for the night. You now have " + food + " Food";
                    break;
                case "Lose one Day to be able to lock two dice before the next turn":
                    day++;
                    finalDayCheck(); // Check it hasn't passed the end of day 40
                    rationsFromScene = "chooseOption";
                    checkRations(); // Rations check before moving onto next day
                    System.out.println("It is now Day " + day);
                    lostFamily();
                    chooseOptionPopUpMessage = "You decide to help the family. It is now day " + day;
                    break;
                case "Take on the two Army deserters":
                    armyDeserters();
                    chooseOptionPopUpMessage = "You take on the army deserters";
                    break;
                default:
                    System.out.println("No option selected");
                    return;
            }
            popUpMessage.setText(chooseOptionPopUpMessage);
            chooseOptionPopUpBox.setVisible(true);

            if (selectedIndex >= 0) {
                numberOfChoices--;
                if (numberOfChoices > 0) {
                    reloadChoices = true;
                } else {
                    optionChosen = true;
                }
            }
        }
    }

    public static void checkOptionRequirements() throws IOException {

        optionsList.clear();
        optionAvailable = false;
        switch (chooseOption) {
            case "5":
                if (food > 1) {
                    optionsList.add("Lose two Food"); // Add food option
                    optionAvailable = true;
                }
                if (posse > 1) {
                    optionsList.add("Lose one Posse member"); // Add posse option
                    optionAvailable = true;
                }
                break;
            case "Animal Herd":
                if (ammo > 0) {
                    optionsList.addAll("Spend one Ammo to gain two Food", "Skip hunting"); // Yes and no options
                    optionAvailable = true;
                } else {
                    optionsList.addAll("Skip hunting"); // No option
                    optionAvailable = true;
                    setChooseOptionMessage("You do not have enough Ammo to go hunting. Continue");
                    optionChosen = true; // Player can continue
                }
                break;
            case "Demands":
                if (food > 0 && gold > 0) {
                    optionsList.add("Lose one Gold and one Food"); // Add food/gold option
                    optionAvailable = true;
                }
                if (posse > 1) {
                    optionsList.add("Lose one Posse member"); // Add posse option
                    optionAvailable = true;
                }
                break;
            case "Deer Herd":
                if (ammo > 1 && day < timeLimit) {
                    optionsList.addAll("Spend two Ammo and one Day to get Food equal to a dice roll +2", "Spend one Ammo to get 2 Food", "Skip Hunting"); // All three options
                    optionAvailable = true;
                } else if (ammo > 0) {
                    optionsList.addAll("Spend one Ammo to gain two Food", "Skip hunting"); // 1 ammo and skip event options
                    optionAvailable = true;
                } else {
                    optionsList.addAll("Skip Hunting"); // Skip event
                    optionAvailable = true;
                    setChooseOptionMessage("You do not have enough Ammo to go hunting. Continue");
                    optionChosen = true; // Player can continue
                }
                break;
            case "Lost Gunsight Mine":
                optionsList.addAll("Gain five Gold but lose the ability to use 4's for Seek Shelter", "Skip the mine"); // Both options
                optionAvailable = true;
                break;
            case "Shortcut":
                optionsList.addAll("Roll a die, subtract 3 and move that many spaces forwards. If the result is negative, add one Day instead", "Skip the shortcut"); // Both options
                optionAvailable = true;
                break;
            case "Posse Demands":
                if (gold > 1) {
                    optionsList.addAll("Lose two Gold"); // Add 2 gold option
                    optionAvailable = true;
                }
                if (food > 1) {
                    optionsList.addAll("Lose two Food"); // Add 2 food option
                    optionAvailable = true;
                }
                if (food > 0 && gold > 0) {
                    optionsList.add("Lose one Gold and one Food"); // Add food/gold option
                    optionAvailable = true;
                }
                if (posse > 2) {
                    optionsList.add("Lose two Posse members"); // Add posse option
                    optionAvailable = true;
                }
                break;
            case "Bandits":
                if (gold > 2) {
                    optionsList.addAll("Lose three Gold"); // Add 3 gold option
                    optionAvailable = true;
                }
                if (food > 2) {
                    optionsList.addAll("Lose three Food"); // Add 3 food option
                    optionAvailable = true;
                }
                if (food > 1 && gold > 1) {
                    optionsList.addAll("Lose two Gold and two Food"); // Add 2 gold, 2 food option
                    optionAvailable = true;
                }
                if (food > 0 && gold > 1 && ammo > 0) {
                    optionsList.addAll("Lose two Gold, one Food and one Ammo"); // Add gold/food/ammo option
                    optionAvailable = true;
                }
                optionsList.addAll("Start a Shootout (with only one Fight dice and two opponent Shootout dice)"); // Add shootout option
                optionAvailable = true;
                break;
            case "Campfire Songs":
                if (food > 0) {
                    optionsList.addAll("Spend one Food to gain one extra Fight dice for Shootouts", "Skip the campfire"); // Yes and no options
                    optionAvailable = true;
                } else {
                    optionsList.addAll("Skip the campfire"); // No option
                    optionAvailable = true;
                    setChooseOptionMessage("You do not have enough Food for the campfire. Continue");
                    optionChosen = true; // Player can continue
                }
                break;
            case "Lost Family":
                if (day < timeLimit) {
                    optionsList.addAll("Lose one Day to be able to lock two dice before the next turn", "Ignore the family"); // Yes and no options
                    optionAvailable = true;
                } else {
                    optionsList.addAll("Ignore the family"); // No option
                    optionAvailable = true;
                    setChooseOptionMessage("You do not have enough time to help the family. Continue");
                    optionChosen = true; // Player can continue
                }
                break;
            case "Army Deserters":
                optionsList.addAll("Take on the two Army deserters", "Ignore the deserters"); // Both options
                optionAvailable = true;
                break;
        }

        if (!optionAvailable) {
            setGameOverMessage("You could not meet any of the requirements of the Event");
            setChooseOptionMessage("You do not have enough resources to meet the requirements.\nGame Over!");
            loss = true;
            gameOverMethods();
        }
    }

    public static StringProperty chooseOptionMessage = new SimpleStringProperty("Choose from the options below");

    public String getChooseOptionMessage() {
        return chooseOptionMessage.get();
    }

    public StringProperty chooseOptionMessageProperty() {
        return chooseOptionMessage;
    }

    public static void setChooseOptionMessage(String chooseOptionMessage) {
        ChooseOption.chooseOptionMessage.set(chooseOptionMessage);
    }
    public static final ObservableList<String> optionsList = FXCollections.observableArrayList();

    public ObservableList<String> getOptionsList() {
        return optionsList;
    }
}
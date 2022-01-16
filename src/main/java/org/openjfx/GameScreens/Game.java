package org.openjfx.GameScreens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.RollMethods.Locking.*;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.GameScreens.Events.*;
import static org.openjfx.GameScreens.SplitFours.*;
import static org.openjfx.Logic.Scoring.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class Game implements Initializable {

    public VBox confirmBox;
    public Button rollButton;
    public Button whiteDie1Btn = new Button();
    public Button whiteDie2Btn = new Button();
    public Button whiteDie3Btn = new Button();
    public Button whiteDie4Btn = new Button();
    public Button whiteDie5Btn = new Button();
    public Button redDie1Btn = new Button();
    public Button redDie2Btn = new Button();
    public Button redDie3Btn = new Button();
    public VBox startTurnBox;
    public HBox diceLabels;
    public HBox diceButtons;
    public Label redDie1Label;
    public Label redDie2Label;
    public Label redDie3Label;

    // Event FX properties
    public VBox goldHillsPopUpBox;
    public RadioButton goldHillsWhiteDie1;
    public RadioButton goldHillsWhiteDie2;
    public RadioButton goldHillsWhiteDie3;
    public RadioButton goldHillsWhiteDie4;
    public RadioButton goldHillsWhiteDie5;
    public RadioButton goldHillsRedDie1;
    public RadioButton goldHillsRedDie2;
    public RadioButton goldHillsRedDie3;
    public HBox goldHillsOptions;
    public VBox lostFamilyPopUpBox;
    public ChoiceBox whiteDieChoices;
    public ChoiceBox redDieChoices;
    public VBox indianGuidePopUpBox;
    public ChoiceBox indianGuideChoices;
    public Label eventDie1Label = new Label("Event Die 1");
    public Label eventDie2Label = new Label("Event Die 2");
    public Button eventDie1Btn = new Button("0");
    public Button eventDie2Btn = new Button("0");
    public VBox prisonerPopUpBox;
    public Label prisonerMessage;
    public VBox deserterPopUpBox;
    public Label deserter1Message;
    public Label deserter2Message;
    public Button deserterPopUpButton;
    public VBox binocularsPopUpBox;
    public VBox hunterPopUpBox;

    public static Boolean goldHillsUsed = false;
    public static Boolean lostFamilyUsed = false;
    public static Boolean indianGuideUsed = false;
    public static Boolean rollStarted = false;

    public static long startTime; // Used for tracking time

    public static int gameSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (downhillEvent) {
            // Add in extra dice
            diceLabels.getChildren().add(eventDie1Label);
            diceButtons.getChildren().add(eventDie1Btn);
            eventDie1Btn.setOnAction((event) -> {
                if (rollCounter > 0) {
                    lockUnlockEventDie1();
                }
            });
        } else if (crittersEvent) {
            // Add in extra two dice
            diceLabels.getChildren().addAll(eventDie1Label,eventDie2Label);
            diceButtons.getChildren().addAll(eventDie1Btn,eventDie2Btn);
            eventDie1Btn.setOnAction((event) -> {
                if (rollCounter > 0) {
                    lockUnlockEventDie1();
                }
            });
            eventDie2Btn.setOnAction((event) -> {
                if (rollCounter > 0) {
                    lockUnlockEventDie2();
                }
            });
        }
        if (prisonerRed6) {
            removePrisonerDie();
        }
        if (gameSceneProgress !=0) {
            startTurnBox.setVisible(false);
        }
        if (gameSceneProgress == 2) {
            // Deserter
            deserterPopUpBox.setVisible(true);
        } else if (gameSceneProgress == 3) {
            // Hunter
            hunterPopUpBox.setVisible(true);
        } else if (gameSceneProgress == 4) {
            // Lost family
            lostFamilyPopUpBox.setVisible(true);
        } else if (gameSceneProgress == 5) {
            // Indian guide
            indianGuidePopUpBox.setVisible(true);
        } else if (gameSceneProgress == 6) {
            // Prisoner red 6
            showPrisonerMessage();
        } else if (gameSceneProgress == 7) {
            // Gold hills
            goldHillsSetup();
            goldHillsPopUpBox.setVisible(true);
        } else if (gameSceneProgress == 8) {
            // Binoculars
            binocularsPopUpBox.setVisible(true);
        } else if (gameSceneProgress == 9) {
            // Final roll
            rollButton.setText("Continue");
        } else if (gameSceneProgress == 10) {
            // Deserter method
            deserterPopUpBox.setVisible(true);
            if (deserter1) {
                deserter1Message.setVisible(true);
            }
            if (deserter2) {
                deserter2Message.setVisible(true);
            }
            deserter1Message.setText(deserter1Status);
            deserter2Message.setText(deserter2Status);
            deserterPopUpButton.setText("Close");
            // Change button action
            deserterPopUpButton.setOnAction((event) -> {
                gameSceneProgress = 1;
                deserterPopUpBox.setVisible(false);
            });
        }
        buttonText();
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "game";
        App.setRoot("pause");
    }

    @FXML
    private void startTurn() {
        gameSceneProgress = 1;
        startTurnBox.setVisible(false);
        turns++; // Turns stat tracking
        startTime = System.currentTimeMillis(); // Take current time for stat tracking
        gamesPlayed++; // Add to games played tracker
        updateStats();
        resetDiceVariables();
        if (desertersEvent) {
            gameSceneProgress = 2;
            deserterPopUpBox.setVisible(true);
        }
        if (hunterLost) {
            gameSceneProgress = 3;
            hunterPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void confirmSpecialEvent() throws IOException {
        if (!rollStarted) {
            specialEvent = true;
            specialEventUsed = true; // stats tracking special event
            App.setRoot("events");
        }
    }

    @FXML
    private void startSpecialEvent() {
        if (!specialEventUsed) {
            confirmBox.setVisible(true);
        }
    }

    @FXML
    private void returnToGame() {
        confirmBox.setVisible(false);
    }

    @FXML
    private void rollMethod1() throws IOException {

        rollStarted = true;

        // Input method that calls all roll methods
        if (rollCounter < 1 && familyEvent && !lostFamilyUsed) {
            gameSceneProgress = 4;
            lostFamilyPopUpBox.setVisible(true);
            lostFamilyUsed = true;
        } else if (rollCounter < 1 && indianEvent && !indianGuideUsed) {
            gameSceneProgress = 5;
            indianGuidePopUpBox.setVisible(true);
            indianGuideUsed = true;
        } else if (rollCounter < 1) {
            rollDice();
            forceLock();
            if (binocularsInventory) {
                binocularsCheck();
            }
            if (prisonerEvent) {
                prisonerRed6Check();
                if (prisonerRed6) {
                    gameSceneProgress = 6;
                    showPrisonerMessage();
                }
            }
        } else if (rollCounter == 1 && goldHillsEvent && !goldHillsUsed) {
            gameSceneProgress = 7;
            goldHillsSetup();
            goldHillsPopUpBox.setVisible(true);
            goldHillsUsed = true;
        } else if (rollCounter == 1 && binocularsInventory && binocularsCounter) {
            gameSceneProgress = 8;
            binocularsPopUpBox.setVisible(true);
            binocularsCounter = false;
        } else if (rollCounter < 3) {
            rollDice();
            if (rollCounter == 3) {
                gameSceneProgress = 9;
                rollButton.setText("Continue");
            }
            if (prisonerEvent) {
                prisonerRed6Check();
                if (prisonerRed6) {
                    gameSceneProgress = 6;
                    showPrisonerMessage();
                }
            }
        } else if (rollCounter == 3) {
            gameSceneProgress = 0;
            countRolls();
            System.out.println("Resolving dice");
            if (fours > 0) {
                App.setRoot("splitFours");
                fourCounter = fours;
                System.out.println(fourCounter + " 4s available");
            } else {
                App.setRoot("resolveDice");
            }
        }
        buttonText();
    }

    // Gold Hills Event methods

    private void goldHillsSetup() {
        // Set radio buttons text
        goldHillsWhiteDie1.setText(Integer.toString(whiteDie1));
        goldHillsWhiteDie2.setText(Integer.toString(whiteDie2));
        goldHillsWhiteDie3.setText(Integer.toString(whiteDie3));
        goldHillsWhiteDie4.setText(Integer.toString(whiteDie4));
        goldHillsWhiteDie5.setText(Integer.toString(whiteDie5));

        // Remove red 5 and 6 radio buttons
        if (redDie1 > 4 || redDie1 == 0) {
            goldHillsOptions.getChildren().remove(goldHillsRedDie1);
        } else {
            goldHillsRedDie1.setText(Integer.toString(redDie1));
        }
        if (redDie2 > 4 || redDie2 == 0) {
            goldHillsOptions.getChildren().remove(goldHillsRedDie2);
        } else {
            goldHillsRedDie2.setText(Integer.toString(redDie2));
        }
        if (redDie3 > 4 || redDie3 == 0) {
            goldHillsOptions.getChildren().remove(goldHillsRedDie3);
        } else {
            goldHillsRedDie3.setText(Integer.toString(redDie3));
        }
    }

    @FXML
    private void saveGoldHillsPopUp() {
        // Change and lock the dice selected
        gameSceneProgress = 1;
        if (goldHillsWhiteDie1.isSelected()) {
            whiteDie1 = 3;
            whiteDie1Lock = true;
            whiteDie1Btn.setText("3");
        } else if (goldHillsWhiteDie2.isSelected()) {
            whiteDie2 = 3;
            whiteDie2Lock = true;
            whiteDie2Btn.setText("3");
        } else if (goldHillsWhiteDie3.isSelected()) {
            whiteDie3 = 3;
            whiteDie3Lock = true;
            whiteDie3Btn.setText("3");
        } else if (goldHillsWhiteDie4.isSelected()) {
            whiteDie4 = 3;
            whiteDie4Lock = true;
            whiteDie4Btn.setText("3");
        } else if (goldHillsWhiteDie5.isSelected()) {
            whiteDie5 = 3;
            whiteDie5Lock = true;
            whiteDie5Btn.setText("3");
        } else if (goldHillsRedDie1.isSelected()) {
            redDie1 = 3;
            redDie1Lock = true;
            redDie1Btn.setText("3");
        } else if (goldHillsRedDie2.isSelected()) {
            redDie3 = 3;
            redDie2Lock = true;
            redDie2Btn.setText("3");
        } else if (goldHillsRedDie3.isSelected()) {
            redDie3 = 3;
            redDie3Lock = true;
            redDie3Btn.setText("3");
        }
        goldHillsPopUpBox.setVisible(false);
    }

    @FXML
    private void closeGoldHillsPopUp() {
        gameSceneProgress = 1;
        goldHillsPopUpBox.setVisible(false);
    }

    @FXML
    private void radioButton() {
        // Group the radio buttons together
        ToggleGroup group = new ToggleGroup();
        goldHillsWhiteDie1.setToggleGroup(group);
        goldHillsWhiteDie2.setToggleGroup(group);
        goldHillsWhiteDie3.setToggleGroup(group);
        goldHillsWhiteDie4.setToggleGroup(group);
        goldHillsWhiteDie5.setToggleGroup(group);
        goldHillsRedDie1.setToggleGroup(group);
        goldHillsRedDie2.setToggleGroup(group);
        goldHillsRedDie3.setToggleGroup(group);
    }

    // Lost family event methods

    @FXML
    private void saveLostFamilyPopUp() {
        gameSceneProgress = 1;
        Object selectedItem = whiteDieChoices.getSelectionModel().getSelectedItem();
        int selectedIndex = whiteDieChoices.getSelectionModel().getSelectedIndex();
        System.out.println("Lost family white die " + selectedItem + " selected");
        System.out.println("At index " + selectedIndex);
        for (int i = 0; i < 6; i++) {
            if ((selectedIndex) == (i)) {
                whiteDie1 = selectedIndex + 1;
                whiteDie1Lock = true;
                whiteDie1Btn.setText(String.valueOf(selectedItem));
            }
        }
        Object selectedItem2 = redDieChoices.getSelectionModel().getSelectedItem();
        int selectedIndex2 = redDieChoices.getSelectionModel().getSelectedIndex();
        System.out.println("Lost family red die " + selectedItem2 + " selected");
        System.out.println("At index " + selectedIndex2);
        for (int i = 0; i < 6; i++) {
            if ((selectedIndex2) == (i)) {
                redDie1 = selectedIndex2 + 1;
                redDie1Lock = true;
                redDie1Btn.setText(String.valueOf(selectedItem2));
            }
        }
        familyEvent = false;
        lostFamilyPopUpBox.setVisible(false);
    }

    @FXML
    private void closeLostFamilyPopUp() {
        gameSceneProgress = 1;
        familyEvent = false;
        lostFamilyPopUpBox.setVisible(false);
    }

    // Indian guide event methods

    @FXML
    private void saveIndianGuidePopUp() {
        gameSceneProgress = 1;
        Object selectedItem = indianGuideChoices.getSelectionModel().getSelectedItem();
        int selectedIndex = indianGuideChoices.getSelectionModel().getSelectedIndex();
        System.out.println("Indian Guide red die " + selectedItem + " selected");
        System.out.println("At index " + selectedIndex);
        for (int i = 0; i < 6; i++) {
            if ((selectedIndex) == (i)) {
                redDie1 = selectedIndex + 1;
                redDie1Lock = true;
                redDie1ForceLock = true;
                redDie1Btn.setText(String.valueOf(selectedItem));
            }
        }
        indianGuidePopUpBox.setVisible(false);
    }

    @FXML
    private void closeIndianGuidePopUp() {
        gameSceneProgress = 1;
        indianGuidePopUpBox.setVisible(false);
    }

    // Prisoner event methods

    private void showPrisonerMessage() {
        prisonerMessage.setText("Prisoner Event; You rolled a Red 6. This die will be removed until you reach " +
                "the next Town. If you do not lose a Shootout before then, you will gain three Gold, otherwise " +
                "you will lose one Gold");
        prisonerPopUpBox.setVisible(true);
        removePrisonerDie();
    }

    private void removePrisonerDie() {
        // Remove dice from play
        if (redDie1 == 0) {
            diceLabels.getChildren().remove(redDie1Label);
            diceButtons.getChildren().remove(redDie1Btn);
        } else if (redDie2 == 0) {
            diceLabels.getChildren().remove(redDie2Label);
            diceButtons.getChildren().remove(redDie2Btn);
        } else if (redDie3 == 0) {
            diceLabels.getChildren().remove(redDie3Label);
            diceButtons.getChildren().remove(redDie3Btn);
        }
    }

    @FXML
    private void closePrisonerPopUp() {
        gameSceneProgress = 1;
        prisonerPopUpBox.setVisible(false);
    }

    // Deserters roll event methods

    @FXML
    private void desertersRollMethod() {
        gameSceneProgress = 10;
        if (deserter1) {
            deserter1Message.setVisible(true);
        }
        if (deserter2) {
            deserter2Message.setVisible(true);
        }
        deserterRoll();
        deserter1Message.setText(deserter1Status);
        deserter2Message.setText(deserter2Status);
        deserterPopUpButton.setText("Close");

        // Change button action
        deserterPopUpButton.setOnAction((event) -> {
            gameSceneProgress = 1;
            deserterPopUpBox.setVisible(false);
        });
    }

    // Binoculars ability methods

    @FXML
    private void useBinoculars() {
        gameSceneProgress = 1;
        binocularsAbility();
        binocularsPopUpBox.setVisible(false);
    }

    @FXML
    private void closeBinocularsPopUp() {
        gameSceneProgress = 1;
        binocularsPopUpBox.setVisible(false);
    }

    @FXML
    private void closeHunterPopUp() {
        gameSceneProgress = 1;
        hunterPopUpBox.setVisible(false);
    }

    @FXML
    private void buttonText() {

        whiteDie1Btn.setText(Integer.toString(whiteDie1));
        whiteDie2Btn.setText(Integer.toString(whiteDie2));
        whiteDie3Btn.setText(Integer.toString(whiteDie3));
        whiteDie4Btn.setText(Integer.toString(whiteDie4));
        whiteDie5Btn.setText(Integer.toString(whiteDie5));
        redDie1Btn.setText(Integer.toString(redDie1));
        redDie2Btn.setText(Integer.toString(redDie2));
        redDie3Btn.setText(Integer.toString(redDie3));
        if (downhillEvent) {
            eventDie1Btn.setText(Integer.toString(eventDie1));
        } else if (crittersEvent) {
            eventDie1Btn.setText(Integer.toString(eventDie1));
            eventDie2Btn.setText(Integer.toString(eventDie2));
        }
    }

    @FXML
    private void whiteDie1Button() {
        if (rollCounter > 0) {
            lockUnlockWhiteDie1();
        }
    }

    @FXML
    private void whiteDie2Button() {
        if (rollCounter > 0) {
            lockUnlockWhiteDie2();
        }
    }

    @FXML
    private void whiteDie3Button() {
        if (rollCounter > 0) {
            lockUnlockWhiteDie3();
        }
    }

    @FXML
    private void whiteDie4Button() {
        if (rollCounter > 0) {
            lockUnlockWhiteDie4();
        }
    }

    @FXML
    private void whiteDie5Button() {
        if (rollCounter > 0) {
            lockUnlockWhiteDie5();
        }
    }

    @FXML
    private void redDie1Button() {
        if (rollCounter > 0) {
            lockUnlockRedDie1();
        }
    }

    @FXML
    private void redDie2Button() {
        if (rollCounter > 0) {
            lockUnlockRedDie2();
        }
    }

    @FXML
    private void redDie3Button() {
        if (rollCounter > 0) {
        lockUnlockRedDie3();
        }
    }

    public static StringProperty dayLabel = new SimpleStringProperty("Day: " + day);

    public String getDayLabel() {
        return dayLabel.get();
    }

    public static StringProperty dayLabelProperty() {
        return dayLabel;
    }

    public static void setDayLabel(String dayLabel) {
        Game.dayLabel.set(dayLabel);
    }

    public static StringProperty playerSpaceLabel = new SimpleStringProperty("Player space: " + playerSpace);

    public String getPlayerSpaceLabel() {
        return playerSpaceLabel.get();
    }

    public static StringProperty playerSpaceLabelProperty() {
        return playerSpaceLabel;
    }

    public static void setPlayerSpaceLabel(String playerSpaceLabel) {
        Game.playerSpaceLabel.set(playerSpaceLabel);
    }
    // Start turn game status messages
    public static StringProperty daySpaceStatus = new SimpleStringProperty();

    public static StringProperty inventoryStatus = new SimpleStringProperty();

    public String getDaySpaceStatus() {
        return daySpaceStatus.get();
    }

    public static StringProperty daySpaceStatusProperty() {
        return daySpaceStatus;
    }

    public static void setDaySpaceStatus(String daySpaceStatus) {
        Game.daySpaceStatus.set(daySpaceStatus);
    }

    public String getInventoryStatus() {
        return inventoryStatus.get();
    }

    public static StringProperty inventoryStatusProperty() {
        return inventoryStatus;
    }

    public static void setInventoryStatus(String inventoryStatus) {
        Game.inventoryStatus.set(inventoryStatus);
    }
}
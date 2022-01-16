package org.openjfx.SettingsScreens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.openjfx.Logic.TownMethods.FindTown.townNames;

public class Settings implements Initializable {

    // Game settings variables
    public static Boolean customGame = false; // true is custom game, false is not custom
    public static Boolean gameLengthLong = true; // true is long game, false is short game
    public static Boolean gameDifficultyStandard = true; // true is standard difficulty, false is easy
    public static int startingPosse = 12;
    public static int startingGold = 3;
    public static int startingFood = 6;
    public static int startingAmmo = 5;
    public static int dayLimit = 40;
    public static int finishingTownIndex = 5;
    public static Boolean eventTypeStatus = true; // true is full events, false is simplified events
    public static ArrayList<Integer> startingSpecialItems = new ArrayList<>(0);
    public static int startingBetterGuns; // Better guns modifier to apply
    public static Boolean unlimitedPoker = false; // true is unlimited, false is limited

    public RadioButton longGameRadioButton;
    public RadioButton shortGameRadioButton;
    public RadioButton standardDifficultyRadioButton;
    public RadioButton easyDifficultyGameRadioButton;
    public RadioButton fullEventsRadioButton;
    public RadioButton simplifiedEventsRadioButton;
    public ToggleButton yesCustomGameToggleButton;
    public ToggleButton noCustomGameToggleButton;
    public Button customGameButton;

    public VBox customGameBox;
    public RadioButton unlimitedPokerRadioButton;
    public RadioButton limitedPokerRadioButton;
    public ChoiceBox endTown;
    public ToggleButton compassToggleButton;
    public ToggleButton hunterToggleButton;
    public ToggleButton prospectorsMapToggleButton;
    public ToggleButton binocularsToggleButton;
    public ToggleButton medicineBandagesToggleButton;
    public ToggleButton betterGunsToggleButton;
    public Slider betterGunsSlider;
    public Slider daySlider;
    public Label daySliderLabel;
    public Slider posseSlider;
    public Label posseSliderLabel;
    public Slider goldSlider;
    public Label goldSliderLabel;
    public Slider foodSlider;
    public Label foodSliderLabel;
    public Slider ammoSlider;
    public Label ammoSliderLabel;

    // Toggle groups
    public ToggleGroup gameLength = new ToggleGroup();
    public ToggleGroup gameDifficulty = new ToggleGroup();
    public ToggleGroup eventType = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Game length
        longGameRadioButton.setToggleGroup(gameLength);
        shortGameRadioButton.setToggleGroup(gameLength);

        // Game difficulty
        standardDifficultyRadioButton.setToggleGroup(gameDifficulty);
        easyDifficultyGameRadioButton.setToggleGroup(gameDifficulty);

        // Event type
        fullEventsRadioButton.setToggleGroup(eventType);
        simplifiedEventsRadioButton.setToggleGroup(eventType);

        // Custom game
        if (customGame) {
            yesCustomGameToggleButton.setSelected(true);
        } else {
            noCustomGameToggleButton.setSelected(true); // Default
            if (!gameLengthLong) {
                shortGameRadioButton.setSelected(true);
            } else {
                longGameRadioButton.setSelected(true); // Default
            }
            if (!gameDifficultyStandard) {
                easyDifficultyGameRadioButton.setSelected(true);
            } else {
                standardDifficultyRadioButton.setSelected(true); // Default
            }
            if (!eventTypeStatus) {
                simplifiedEventsRadioButton.setSelected(true);
            } else {
                fullEventsRadioButton.setSelected(true); // Default
            }
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void saveSettings() {
        // Check toggled options and save settings - will this return to main menu?
        startingSpecialItems.clear();
        // Custom game
        // TODO: Add message that custom games do not track stats - stats tracked only for standard length and difficulty games
        if (yesCustomGameToggleButton.isSelected()) {
            customGame = true;
            dayLimit = (int)daySlider.getValue();
            startingPosse = (int)posseSlider.getValue();
            startingGold = (int)goldSlider.getValue();
            startingFood = (int)foodSlider.getValue();
            startingAmmo = (int)ammoSlider.getValue();
            finishingTownIndex = endTown.getSelectionModel().getSelectedIndex();
            if (compassToggleButton.isSelected()) {
                startingSpecialItems.add(1);
            }
            if (hunterToggleButton.isSelected()) {
                startingSpecialItems.add(2);
            }
            if (prospectorsMapToggleButton.isSelected()) {
                startingSpecialItems.add(3);
            }
            if (binocularsToggleButton.isSelected()) {
                startingSpecialItems.add(4);
            }
            if (medicineBandagesToggleButton.isSelected()) {
                startingSpecialItems.add(5);
            }
            if (betterGunsToggleButton.isSelected())
                startingSpecialItems.add(6);
            startingBetterGuns = (int)betterGunsSlider.getValue();
            if (unlimitedPokerRadioButton.isSelected()) {
                unlimitedPoker = true;
            } else {
                unlimitedPoker = false;
            }
        } else {
            // Set default unused options
            customGame=false;
            startingPosse = 12;
            startingAmmo = 5;
            unlimitedPoker = false;

            // Game length
            if (longGameRadioButton.isSelected()) {
                finishingTownIndex = 5; // Reno
                dayLimit = 40;
                gameLengthLong = true;
            } else if (shortGameRadioButton.isSelected()) {
                finishingTownIndex = 4; // Buckskin
                dayLimit = 30;
                gameLengthLong = false;
            }

            // Game difficulty
            if (standardDifficultyRadioButton.isSelected()) {
                startingFood = 6;
                startingGold = 3;
                gameDifficultyStandard = true;
            } else if (easyDifficultyGameRadioButton.isSelected()) {
                startingFood = 12;
                startingGold = 5;
                startingSpecialItems.add((int) (Math.random() * 6 + 1));
                if (startingSpecialItems.contains(6)) {
                    startingBetterGuns = (int)(Math.random()*6+1);
                }
                gameDifficultyStandard = false;
            }

            // Event types
            if (fullEventsRadioButton.isSelected()) {
                eventTypeStatus = true;
            } else if (simplifiedEventsRadioButton.isSelected()) {
                eventTypeStatus = false;
            }
        }

        System.out.println("Day limit set to " + dayLimit);
        System.out.println("Starting Posse set to " + startingPosse);
        System.out.println("Starting Gold set to " + startingGold);
        System.out.println("Starting Food set to " + startingFood);
        System.out.println("Starting Ammo set to " + startingAmmo);
        System.out.println("Finishing town index to " + finishingTownIndex);
        System.out.println("Full events: " + eventTypeStatus);
        System.out.println("Custom game: " + customGame);
        System.out.println("Starting special items: " + startingSpecialItems);
        System.out.println("Unlimited poker: " + unlimitedPoker);
    }

    @FXML
    private void resetToDefaults() {
        // Revert settings back to defaults
        longGameRadioButton.setSelected(true);
        standardDifficultyRadioButton.setSelected(true);
        fullEventsRadioButton.setSelected(true);
        noCustomGameToggleButton.setSelected(true);
        yesCustomGameToggleButton.setSelected(false);
        // Reset custom games options too
        saveSettings();
    }

    @FXML
    private void openCustomGamePopUp() {
        yesCustomGameToggle();
        // Set up custom game settings pop up
        // Unlimited poker
        ToggleGroup unlimitedPokerGroup = new ToggleGroup();
        unlimitedPokerRadioButton.setToggleGroup(unlimitedPokerGroup);
        limitedPokerRadioButton.setToggleGroup(unlimitedPokerGroup);
        if (unlimitedPoker) {
            unlimitedPokerRadioButton.setSelected(true);
        } else {
            limitedPokerRadioButton.setSelected(true);
        }

        // Ending destination
        if (endTown.getItems().isEmpty()) {
            endTown.getItems().addAll(townNames[0], townNames[1], townNames[2], townNames[3], townNames[4], townNames[5]);
            endTown.setValue(townNames[finishingTownIndex]);
        }

        // Special items - Search through special items arraylist for specific number and if it is there, then toggle that button on
        if (startingSpecialItems.contains(1)) {
            compassToggleButton.setSelected(true);
        }
        if (startingSpecialItems.contains(2)) {
            hunterToggleButton.setSelected(true);
        }
        if (startingSpecialItems.contains(3)) {
            prospectorsMapToggleButton.setSelected(true);
        }
        if (startingSpecialItems.contains(4)) {
            binocularsToggleButton.setSelected(true);
        }
        if (startingSpecialItems.contains(5)) {
            medicineBandagesToggleButton.setSelected(true);
        }
        if (startingSpecialItems.contains(6)) {
            betterGunsToggleButton.setSelected(true);
        }
        showBetterGunsSlider();

        // Inventory items
        daySlider.setValue(dayLimit);
        daySliderLabel.setText(Integer.toString(dayLimit));
        daySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                        daySliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
        posseSlider.setValue(startingPosse);
        posseSliderLabel.setText(Integer.toString(startingPosse));
        posseSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                posseSliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
        goldSlider.setValue(startingGold);
        goldSliderLabel.setText(Integer.toString(startingGold));
        goldSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                goldSliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
        foodSlider.setValue(startingFood);
        foodSliderLabel.setText(Integer.toString(startingFood));
        foodSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                foodSliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
        ammoSlider.setValue(startingAmmo);
        ammoSliderLabel.setText(Integer.toString(startingAmmo));
        ammoSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                ammoSliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });

        customGameBox.setVisible(true);
    }

    @FXML
    private void saveCustomSettings() {
        // Save toggled options
        saveSettings();
        customGameBox.setVisible(false);
    }

    @FXML
    private void closeCustomSettings() {
        customGameBox.setVisible(false);
    }

    @FXML
    private void yesCustomGameToggle() {
        if (noCustomGameToggleButton.isSelected()) {
            yesCustomGameToggleButton.setSelected(true);
            noCustomGameToggleButton.setSelected(false);
        } else {
            yesCustomGameToggleButton.setSelected(true);
        }
        // If a custom game, de-select other options
        longGameRadioButton.setSelected(false);
        shortGameRadioButton.setSelected(false);
        standardDifficultyRadioButton.setSelected(false);
        easyDifficultyGameRadioButton.setSelected(false);
        fullEventsRadioButton.setSelected(false);
        simplifiedEventsRadioButton.setSelected(false);
    }

    @FXML
    private void noCustomGameToggle() {
        if (yesCustomGameToggleButton.isSelected()) {
            noCustomGameToggleButton.setSelected(true);
            yesCustomGameToggleButton.setSelected(false);
            // Reset other options if not a custom game
            if (!longGameRadioButton.isSelected() || !shortGameRadioButton.isSelected()) {
                if (gameLength.getSelectedToggle() == null) {
                    longGameRadioButton.setSelected(true);
                }
            }
            if (!standardDifficultyRadioButton.isSelected() || !easyDifficultyGameRadioButton.isSelected()) {
                if (gameDifficulty.getSelectedToggle() == null) {
                    standardDifficultyRadioButton.setSelected(true);
                }
            }
            if (!fullEventsRadioButton.isSelected() || !simplifiedEventsRadioButton.isSelected()) {
                if (eventType.getSelectedToggle() == null) {
                    fullEventsRadioButton.setSelected(true);
                }
            }
        } else {
            noCustomGameToggleButton.setSelected(true);
        }
    }

    @FXML
    private void showBetterGunsSlider() {
        if (betterGunsToggleButton.isSelected()) {
            betterGunsSlider.setVisible(true);
            betterGunsSlider.setValue(startingBetterGuns);
        } else {
            betterGunsSlider.setVisible(false);
        }
    }

    @FXML
    private void audioSettings() throws IOException {
        saveSettings();
        App.setRoot("audio");
    }
}
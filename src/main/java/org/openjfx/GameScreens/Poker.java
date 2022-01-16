package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.TownMethods.PlayPoker.*;

public class Poker implements Initializable {

    public Button pokerCard1Button;
    public Button pokerCard2Button;
    public Button pokerCard3Button;
    public Button pokerCard4Button;
    public Button pokerCard5Button;
    public HBox pokerHandBox; // Contains player's poker cards
    public HBox opponentHandBox; // Might not be needed
    public Label playersHandMessage; // You have two pair
    public Label handToBeat; // The hand to beat is a full house (4 4 4 2 2)
    public VBox quitBox; // Quit poker box
    public Label quitMessage; // Quit poker message
    public Button revealHandButton; // Reveal hand button
    public Label pokerMessage; // Message update property
    public Label resultMessage; // You won the hand. You won 2 Ammo and 1 Gold
    public VBox resultBox; // Pop-up at final result
    public Button continueButton; // Open results pop-up
    public static int pokerRollCounter = 1;

    // Wager pop-up properties
    public Slider ammoSlider = new Slider();
    public Slider foodSlider = new Slider();
    public Slider goldSlider = new Slider();
    public Label ammoLabel = new Label("Ammo:");
    public Label foodLabel = new Label("Food:");
    public Label goldLabel = new Label("Gold:");
    public VBox wagerBox; // Wager pop-up box
    public HBox ammoSliderBox = new HBox(); // Box for ammo slider
    public HBox foodSliderBox = new HBox(); // Box for food slider
    public HBox goldSliderBox = new HBox(); // Box for gold slider
    public VBox sliderBox; // Box for all sliders to be added to
    Boolean wagerPopUpConfigured = false;
    public static Boolean wagerSet = false;
    public static Boolean opponentHandRevealed = false;
    public static Boolean pokerResultDone = false;

    public static int pokerSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (wagerSet) {
            pokerMessage.setText("You have wagered " + ammoWager + " Ammo, " + foodWager + " Food and " + goldWager + " Gold");
        }
        if (pokerSceneProgress == 1) {
            updateCardValues();
            pokerHandBox.setVisible(true);
            pokerMessage.setText("Lock any cards that you want to keep");
        } else if (pokerSceneProgress == 2) {
            updateCardValues();
            pokerHandBox.setVisible(true);
            playersHandMessage.setText("You have " + playerHandCombination);
            revealHandButton.setVisible(true);
            pokerMessage.setText("Reveal the hand you need to beat");
        } else if (pokerSceneProgress == 3) {
            updateCardValues();
            pokerHandBox.setVisible(true);
            playersHandMessage.setText("You have " + playerHandCombination);
            revealHandButton.setVisible(true);
            handToBeat.setText("The hand to beat is a " + opponentHand);
            continueButton.setVisible(true);
            pokerMessage.setText("Continue to see the results");
        } else if (pokerSceneProgress == 4) {
            if (pokerWin) {
                resultMessage.setText("You won the round! You won " + ammoWager + " Ammo, " + foodWager + " Food and " +
                        goldWager + " Gold. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            } else if (pokerTie) {
                resultMessage.setText("You tied! You get your wager of " + ammoWager + " Ammo, " + foodWager + " Food and " +
                        goldWager + " Gold back. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            } else if (pokerLoss) {
                resultMessage.setText("You lost the round! You lost your wager of " + ammoWager + " Ammo, " + foodWager +
                        " Food and " + goldWager + " Gold. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            }
            resultBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "poker";
        App.setRoot("pause");
    }

    @FXML
    private void switchToTown() throws IOException {
        pokerSceneProgress = 0;
        App.setRoot("town");
    }

    @FXML
    private void quitPoker() {
        // Change quit message depending on whether eager has been set/roll started
        quitBox.setVisible(true);
    }

    @FXML
    private void returnToPoker() {
        quitBox.setVisible(false);
    }

    @FXML
    private void pokerHands() throws IOException {
        // TODO: Takes player to rules page on poker rules/hands
        App.setRoot("rules");
    }

    @FXML
    private void setWagerPopUp() {
        if (pokerRollCounter > 1) {
            return; // Wager can no longer be set/changed
        }
        // Set slider amounts and appearance
        if (!wagerPopUpConfigured) {
            checkAmmo();
            checkFood();
            checkGold();
            wagerPopUpConfigured = true;
        }
        if (ammo > 0) {
            ammoSlider.setValue(ammoWager);
        }
        if (food > 0) {
            foodSlider.setValue(foodWager);
        }
        if (gold > 0) {
            goldSlider.setValue(goldWager);
        }
        wagerBox.setVisible(true);
    }

    @FXML
    private void setWager() {
        // Save slider values as wager values
        if (ammo > 0) {
            ammoWager = (int) ammoSlider.getValue();
        } else {
            ammoWager = 0;
        }
        if (food > 0) {
            foodWager = (int) foodSlider.getValue();
        } else {
            foodWager = 0;
        }
        if (gold > 0) {
            goldWager = (int) goldSlider.getValue();
        } else {
            goldWager = 0;
        }
        if (ammoWager > 0 || foodWager > 0 || goldWager > 0) {
            wagerSet = true;
        }
        // Set wager message into poker message
        pokerMessage.setText("You have wagered " + ammoWager + " Ammo, " + foodWager + " Food and " + goldWager + " Gold");
        wagerBox.setVisible(false);
    }

    @FXML
    private void closeWager() {
        wagerBox.setVisible(false);
    }

    private void checkAmmo() {
        if (ammo > 0) {
            // Slider configuration
            ammoSlider.setMin(0);
            ammoSlider.setMax(ammo);
            ammoSlider.setValue(0);
            ammoSlider.setShowTickLabels(true);
            ammoSlider.setSnapToTicks(true);
            ammoSlider.setMajorTickUnit(1);
            ammoSlider.setMinorTickCount(0);
            ammoSlider.setBlockIncrement(1);
            ammoSlider.setShowTickMarks(true);
            ammoSlider.setPrefWidth(200);
            // Box configuration
            ammoSliderBox.getChildren().addAll(ammoLabel, ammoSlider);
            ammoSliderBox.setAlignment(Pos.CENTER);
            ammoSliderBox.setSpacing(40);
            // Add slider box
            sliderBox.getChildren().add(ammoSliderBox);
        }
    }

    private void checkFood() {
        if (food > 0) {
            // Slider configuration
            foodSlider.setMin(0);
            foodSlider.setMax(food);
            foodSlider.setValue(0);
            foodSlider.setShowTickLabels(true);
            foodSlider.setSnapToTicks(true);
            foodSlider.setMajorTickUnit(1);
            foodSlider.setMinorTickCount(0);
            foodSlider.setBlockIncrement(1);
            foodSlider.setShowTickMarks(true);
            foodSlider.setMinWidth(200);
            foodSlider.setMaxWidth(400);
            foodSlider.setPrefWidth(food*40);
            // Box configuration
            foodSliderBox.getChildren().addAll(foodLabel, foodSlider);
            foodSliderBox.setAlignment(Pos.CENTER);
            foodSliderBox.setSpacing(40);
            // Add slider box
            sliderBox.getChildren().add(foodSliderBox);
        }
    }

    private void checkGold() {
        if (gold > 0) {
            // Slider configuration
            goldSlider.setMin(0);
            goldSlider.setMax(gold);
            goldSlider.setValue(0);
            goldSlider.setShowTickLabels(true);
            goldSlider.setSnapToTicks(true);
            goldSlider.setMajorTickUnit(1);
            goldSlider.setMinorTickCount(0);
            goldSlider.setBlockIncrement(1);
            goldSlider.setShowTickMarks(true);
            goldSlider.setMinWidth(200);
            goldSlider.setMaxWidth(400);
            goldSlider.setPrefWidth(gold*40);
            // Box configuration
            goldSliderBox.getChildren().addAll(goldLabel, goldSlider);
            goldSliderBox.setAlignment(Pos.CENTER);
            goldSliderBox.setSpacing(40);
            // Add slider box
            sliderBox.getChildren().add(goldSliderBox);
        }
    }

    @FXML
    private void rollPokerCards() {
        if (!wagerSet) {
            pokerMessage.setText("You have not set a wager yet. You must wager at least one resource to play");
            return;
        }
        if (pokerRollCounter == 1) {
            // First time - make pokerHandBox visible and roll all cards
            pokerSceneProgress = 1;
            ammo-= ammoWager;
            food-= foodWager;
            gold-= goldWager;
            pokerPlayed = true;
            playerHand();
            updateCardValues();
            pokerHandBox.setVisible(true);
            pokerRollCounter++;
            pokerMessage.setText("Lock any cards that you want to keep");
        } else if (pokerRollCounter == 2) {
            // Second time - roll only unlocked cards
            playerHand();
            updateCardValues();
            pokerRollCounter++;
            pokerMessage.setText("Lock any cards that you want to keep");
        } else if (pokerRollCounter == 3) {
            // Third time - roll only unlocked cards and make playersHandMessage and revealHandButton visible
            pokerSceneProgress = 2;
            playerHand();
            updateCardValues();
            pokerHandsLogic();
            playersHandMessage.setText("You have " + playerHandCombination);
            revealHandButton.setVisible(true);
            pokerRollCounter++;
            pokerMessage.setText("Reveal the hand you need to beat");
        }
    }

    private void updateCardValues() {
        pokerCard1Button.setText(Integer.toString(pokerCard1));
        pokerCard2Button.setText(Integer.toString(pokerCard2));
        pokerCard3Button.setText(Integer.toString(pokerCard3));
        pokerCard4Button.setText(Integer.toString(pokerCard4));
        pokerCard5Button.setText(Integer.toString(pokerCard5));
    }

    @FXML
    private void lockUnlockCard1() {
        pokerCard1Lock = !pokerCard1Lock;
        System.out.println("Poker Card 1 Lock: " + pokerCard1Lock);
    }

    @FXML
    private void lockUnlockCard2() {
        pokerCard2Lock = !pokerCard2Lock;
        System.out.println("Poker Card 2 Lock: " + pokerCard2Lock);
    }

    @FXML
    private void lockUnlockCard3() {
        pokerCard3Lock = !pokerCard3Lock;
        System.out.println("Poker Card 3 Lock: " + pokerCard3Lock);
    }

    @FXML
    private void lockUnlockCard4() {
        pokerCard4Lock = !pokerCard4Lock;
        System.out.println("Poker Card 4 Lock: " + pokerCard4Lock);
    }

    @FXML
    private void lockUnlockCard5() {
        pokerCard5Lock = !pokerCard5Lock;
        System.out.println("Poker Card 5 Lock: " + pokerCard5Lock);
    }

    @FXML
    private void showHandToBeat() {
        if (!opponentHandRevealed) {
            pokerSceneProgress = 3;
            handToBeat();
            handToBeat.setText("The hand to beat is a " + opponentHand);
            continueButton.setVisible(true);
            pokerMessage.setText("Continue to see the results");
            opponentHandRevealed = true;
        }
    }

    @FXML
    private void seeResult() {
        if (!pokerResultDone) {
            pokerSceneProgress = 4;
            pokerResult();
            if (pokerWin) {
                resultMessage.setText("You won the round! You won " + ammoWager + " Ammo, " + foodWager + " Food and " +
                        goldWager + " Gold. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            } else if (pokerTie) {
                resultMessage.setText("You tied! You get your wager of " + ammoWager + " Ammo, " + foodWager + " Food and " +
                        goldWager + " Gold back. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            } else if (pokerLoss) {
                resultMessage.setText("You lost the round! You lost your wager of " + ammoWager + " Ammo, " + foodWager +
                        " Food and " + goldWager + " Gold. You now have " + ammo + " Ammo, " + food + " Food and " + gold + " Gold");
            } else {
                System.out.println("Error calculating poker result. Please check error");
            }
            resultBox.setVisible(true);
            pokerResultDone = true;
        }
    }
}
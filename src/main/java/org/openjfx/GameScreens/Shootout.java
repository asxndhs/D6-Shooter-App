package org.openjfx.GameScreens;

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
import static org.openjfx.GameScreens.Pause.*;
import static org.openjfx.GameScreens.PosseLoss.posseLossFromScene;
import static org.openjfx.GameScreens.PosseLoss.stopPosseLoss;
import static org.openjfx.Logic.GameOverMethods.posseCheck;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.RollMethods.RollActions.fight;
import static org.openjfx.Logic.ShootoutMethods.*;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.betterGunsInventory;
import static org.openjfx.Logic.TownMethods.SpecialItems.betterGunsModifier;

public class Shootout implements Initializable {

    public HBox shootoutBox;
    public Label shootoutScore;
    public HBox opponentBox;
    public Label opponentScore;
    public HBox messageBox;
    public Label shootoutMessage;
    public VBox ammoMessageBox;
    public Label ammoMessage;

    // Shootout dice labels
    public Label shootoutDice1 = new Label();
    public Label shootoutDice2 = new Label();
    public Label shootoutDice3 = new Label();
    public Label shootoutDice4 = new Label();
    public Label shootoutDice5 = new Label();
    public Label shootoutDice6 = new Label();
    public Label shootoutDice7 = new Label();
    public Label shootoutDice8 = new Label();
    public Label shootoutDice9 = new Label();
    public Label ammoModifier = new Label();
    public Label betterGuns = new Label();

    public Label opponentDice1 = new Label();
    public Label opponentDice2 = new Label();
    public Label opponentDice3 = new Label();
    public Label opponentDice4 = new Label();
    public Label opponentDice5 = new Label();
    public Label opponentDice6 = new Label();
    public Label opponentDice7 = new Label();
    public Label opponentDice8 = new Label();
    public Label opponentDice9 = new Label();

    public static int stage = 1;
    public static ArrayList<Integer> shootoutDiceArray = new ArrayList<Integer>();
    public static ArrayList<Integer> opponentDiceArray = new ArrayList<Integer>();
    ArrayList<Label> shootoutDiceLabels = new ArrayList<Label>();
    ArrayList<Label> opponentDiceLabels = new ArrayList<Label>();

    public static int shootoutSceneProgress = 0;
    public static String shootoutLabelMessage;
    public static String ammoLabelMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: Slight issue (not really important) where at progress = 3, a dice will have been removed (if not a tie)
        //  and this will be reflected when reloading the scene from pause or posse loss i.e. that die will now be missing,
        //  shootout score is still correct though
        if (shootoutSceneProgress == 1) {
            addShootoutDiceToScene();
        } else if (shootoutSceneProgress == 2) {
            addShootoutDiceToScene();
            addOpponentDiceToScene();
        } else if (shootoutSceneProgress == 3) {
            addShootoutDiceToScene();
            addOpponentDiceToScene();
            shootoutMessage.setText(shootoutLabelMessage);
            shootoutMessage.setVisible(true);
        } else if (shootoutSceneProgress == 4) {
            ammoMessage.setText(ammoLabelMessage);
            ammoMessageBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "shootout";
        App.setRoot("pause");
    }

    @FXML
    private void shootoutContinue() throws IOException {
        if (stage == 1) {
            // Roll player's shootout dice and calculate total
            stage++;
            shootoutSceneProgress = 1;
            if (banditsShootout) {
                banditsShootout();
            } else {
                shootout();
            }
            addShootoutDiceToScene();
        } else if (stage == 2) {
            // Roll opponent's shootout dice and calculate total
            stage++;
            shootoutSceneProgress = 2;
            addOpponentDiceToScene();
        } else if (stage == 3) {
            // Resolve results of shootout
            stage++;
            shootoutSceneProgress = 3;
            if (banditsShootout) {
                banditsResolveShootoutRound();
            } else {
                resolveShootoutRound();
            }
            if (replayShootout && !shootoutWin && !shootoutLoss) {
                // It was a tie, replay the round
                stage = 6;
                shootoutLabelMessage = "Player and opponent tied on the Shootout. Replay the round";
                shootoutMessage.setText(shootoutLabelMessage);
                shootoutMessage.setVisible(true);
            } else if (replayShootout && shootoutWin) {
                // Player won the round but both players have dice remaining, start next shootout
                stage = 6;
                shootoutLabelMessage = "You scored higher than your opponent in the Shootout. They lose a Shootout dice\nBoth you and " +
                        "your opponent have Shootout dice remaining - Start the next round";
                shootoutMessage.setText(shootoutLabelMessage);
                shootoutMessage.setVisible(true);
            } else if (replayShootout && shootoutLoss) {
                // Player won the round but both players have dice remaining, start next shootout
                stage = 6;
                shootoutLabelMessage = "Your opponent scored higher than you in the Shootout. You lose a Shootout dice\nBoth you and " +
                        " your opponent have Shootout dice remaining - Start the next round";
                shootoutMessage.setText(shootoutLabelMessage);
                shootoutMessage.setVisible(true);
            } else if (shootoutWin) {
                // Player won shootout, continue
                if (banditsShootout) {
                    banditsResolveShootoutEffects();
                } else {
                    resolveShootoutEffects();
                }
                shootoutLabelMessage = "You won the round and your opponent loses a Shootout dice.\n" +
                        "Your opponent has no Shootout dice remaining. You have won the Shootout. Continue";
                shootoutMessage.setText(shootoutLabelMessage);
                shootoutMessage.setVisible(true);
            } else if (shootoutLoss) {
                // Player lost shootout, lose posse and continue
                if (banditsShootout) {
                    banditsResolveShootoutEffects();
                } else {
                    resolveShootoutEffects();
                }
                shootoutLabelMessage = "You lost the round and lose a Shootout dice as a result.\n" +
                        "You have no Shootout dice remaining. You have lost the Shootout.\nYou lose " +
                        sixes + " Posse members. Continue";
                shootoutMessage.setText(shootoutLabelMessage);
                shootoutMessage.setVisible(true);
            }
        } else if (stage == 4) {
            if (stopPosseLoss) {
                // Check game over otherwise go to posse loss scene
                stopPosseLoss = false;
                posseLossFromScene = "shootout";
                App.setRoot("posseLoss");
                posseCheck();
            } else {
                // End shootout, pop up message to take away ammo
                stage++;
                shootoutSceneProgress = 4;
                if (shootoutWin) {
                    if (ammo > 0) {
                        ammoLabelMessage = "End of Shootout. You lose one Ammo and now have " + (ammo - 1) + " Ammo remaining";
                    } else {
                        ammoLabelMessage = "End of Shootout. You do not have any Ammo to lose";
                    }
                } else {
                    if (ammo > 0) {
                        ammoLabelMessage = "End of Shootout. You now have " + posse + " Posse members.\nYou lose one Ammo and now have " + (ammo - 1) + " Ammo remaining";
                    } else {
                        ammoLabelMessage = "End of Shootout. You now have " + posse + " Posse members.\nYou do not have any Ammo to lose";
                    }
                }
                shootoutReduceAmmo();
                ammoMessage.setText(ammoLabelMessage);
                ammoMessageBox.setVisible(true);
            }
        } else if (stage == 5) {
            // Continue to next scene
            stage = 1;
            shootoutSceneProgress = 0; // Reset
            if (banditsShootout) {
                banditsShootout = false;
                if (specialEvent) {
                    specialEvent = false;
                    App.setRoot("game");
                } else {
                    App.setRoot("resolveDice");
                }
            } else {
                App.setRoot("resolveDice");
            }
        } else if (stage == 6) {
            // Replay the shootout
            stage = 1;
            shootoutSceneProgress = 0; // Reset
            App.setRoot("shootout");
        }
    }

    public void addShootoutDiceToScene() {

        // Add labels to list
        shootoutDiceLabels.add(shootoutDice1);
        shootoutDiceLabels.add(shootoutDice2);
        shootoutDiceLabels.add(shootoutDice3);
        shootoutDiceLabels.add(shootoutDice4);
        shootoutDiceLabels.add(shootoutDice5);
        shootoutDiceLabels.add(shootoutDice6);
        shootoutDiceLabels.add(shootoutDice7);
        shootoutDiceLabels.add(shootoutDice8);
        shootoutDiceLabels.add(shootoutDice9);

        if (banditsShootout) {
            for (int i = 0; i < eventPlayerDice; i++) {
                shootoutDiceLabels.get(i).setText("Dice " + (i + 1) + ": " + shootoutDiceArray.get(i));
                shootoutBox.getChildren().add(shootoutDiceLabels.get(i));
            }
        } else {
            for (int i = 0; i < fight; i++) {
                shootoutDiceLabels.get(i).setText("Dice " + (i + 1) + ": " + shootoutDiceArray.get(i));
                shootoutBox.getChildren().add(shootoutDiceLabels.get(i));
            }
        }

        if (ammo > 0) {
            // Add ammo
            ammoModifier.setText("Ammo: " + ammo);
            shootoutBox.getChildren().add(ammoModifier);
        }

        if (betterGunsInventory) {
            // Add better guns modifier if player has bought it
            betterGuns.setText("Better Guns: " + betterGunsModifier);
            shootoutBox.getChildren().add(betterGuns);
        }

        shootoutScore.setText("Player Shootout Score: " + playerShootoutScore);
        shootoutScore.setVisible(true);
    }

    public void addOpponentDiceToScene() {

        // Add labels to list
        opponentDiceLabels.add(opponentDice1);
        opponentDiceLabels.add(opponentDice2);
        opponentDiceLabels.add(opponentDice3);
        opponentDiceLabels.add(opponentDice4);
        opponentDiceLabels.add(opponentDice5);
        opponentDiceLabels.add(opponentDice6);
        opponentDiceLabels.add(opponentDice7);
        opponentDiceLabels.add(opponentDice8);
        opponentDiceLabels.add(opponentDice9);

        if (banditsShootout) {
            for (int i = 0; i < eventOpponentDice; i++) {
                opponentDiceLabels.get(i).setText("Dice " + (i + 1) + ": " + opponentDiceArray.get(i));
                opponentBox.getChildren().add(opponentDiceLabels.get(i));
            }
        } else {
            for (int i = 0; i < sixes; i++) {
                opponentDiceLabels.get(i).setText("Dice " + (i + 1) + ": " + opponentDiceArray.get(i));
                opponentBox.getChildren().add(opponentDiceLabels.get(i));
            }
        }

        opponentScore.setText("Opponent Shootout Score: " + opponentShootoutScore);
        opponentScore.setVisible(true);
    }
}
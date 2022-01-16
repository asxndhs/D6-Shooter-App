package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.openjfx.App;
import org.openjfx.Logic.Scoring;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import static org.openjfx.Logic.GameOverMethods.win;
import static org.openjfx.Logic.Map.playerSpace;
import static org.openjfx.Logic.Day.day;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Scoring.*;
import static org.openjfx.HighScoreScreens.HighScores.*;

public class GameOver {

    // Stats
    public Button continueButton1;
    public VBox gameStats;
    public Label finalDay;
    public Label finalSpace;
    public Label spacesTravelled;
    public Label turns;
    public Label gameTimer;
    public Label finalPosse;
    public Label finalGold;
    public Label finalFood;
    public Label finalAmmo;
    public Label posseMembersLost;
    public Label goldSpent;
    public Label rationsHandedOut;
    public Label ammoUsed;
    public Label specialItemsBought;
    public Label townsVisited;
    public Label eventsStarted;
    public Label pokerGames;
    public Label pokerWins;
    public Label pokerTies;
    public Label pokerLosses;
    public Label wagerWinnings;
    public Label wagerLosses;
    public Label bestPokerHand;
    public Label shootoutsStarted;
    public Label shootoutsWon;
    public Label shootoutsLost;
    public Label bestShootoutScore;

    // Scoring pop up
    public VBox scorePopUpBox;
    public Label scoreMessage;
    public Label renoScore;
    public Label daysScore;
    public Label posseScore;
    public Label goldScore;
    public Label foodScore;
    public Label ammoScore;
    public Label finalScore;

    // Initials pop up
    public VBox initialsPopUpBox;
    public TextField inputInitials;
    public Label scorePlacementMessage;
    public static String playerInitials;
    Boolean initialsTaken = false;

    @FXML
    private void switchToNextPhase() throws IOException {
        if (!gameStats.isVisible()) {
            updateStats();
            continueButton1.setText("Continue");
            gameStats.setVisible(true);
        } else if (win && !scorePopUpBox.isVisible()) {
            gameScore();
            checkHighScore();
            updateScores();
            scorePopUpBox.setVisible(true);
        } else if (highScore && !initialsTaken) {
            limitTextToInitials();
            initialsPopUpBox.setVisible(true);
        } else {
            gameFinishedLeaderboard = true;
            App.setRoot("highScores");
        }
    }

    @FXML
    private void continueToScores() throws IOException {
        // Can only submit initials if it is three characters long
        if (inputInitials.getLength() == 3) {
            playerInitials = inputInitials.getText();
            System.out.println("Initials: " + playerInitials);
            addInitials(); // Add initials to leaderboard
            initialsTaken = true;
            initialsPopUpBox.setVisible(false);
        }
    }

    private void updateStats() {

        // Set game timers
        String timer = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(Scoring.gameTimer),
                TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimer) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Scoring.gameTimer)),
                TimeUnit.MILLISECONDS.toSeconds(Scoring.gameTimer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimer)));

        finalDay.setText("Days on the road: " + day);
        finalSpace.setText("Final space: " + playerSpace);
        spacesTravelled.setText("Spaces travelled: " + Scoring.spacesTravelled);
        turns.setText("Turns: " + Scoring.turns);
        gameTimer.setText("Game time: " + timer);
        finalPosse.setText("Posse members: " + posse);
        finalGold.setText("Gold: " + gold);
        finalFood.setText("Food: " + food);
        finalAmmo.setText("Ammo: " + ammo);
        posseMembersLost.setText("Posse members lost: " + Scoring.posseMembersLost);
        goldSpent.setText("Gold spent: " + Scoring.goldSpent);
        rationsHandedOut.setText("Rations handed out: " + Scoring.rationsHandedOut);
        ammoUsed.setText("Ammo used: " + Scoring.ammoUsed);
        specialItemsBought.setText("Special Items bought: " + Scoring.specialItemsBought);
        townsVisited.setText("Towns visited: " + Scoring.townsVisited);
        eventsStarted.setText("Events started: " + Scoring.eventsStarted);
        pokerGames.setText("Poker games played: " + Scoring.pokerGames);
        pokerWins.setText("Poker wins: " + Scoring.pokerWins);
        pokerTies.setText("Poker ties: " + Scoring.pokerTies);
        pokerLosses.setText("Poker losses: " + Scoring.pokerLosses);
        wagerWinnings.setText("Wager winnings: " + Scoring.wagerWinnings[0] + " Ammo - " +
                Scoring.wagerWinnings[1] + " Food - " + Scoring.wagerWinnings[2] + " Gold");
        wagerLosses.setText("Wager losses: " + Scoring.wagerLosses[0] + " Ammo - " +
                Scoring.wagerLosses[1] + " Food - " + Scoring.wagerLosses[2] + " Gold");
        bestPokerHand.setText("Best Poker hand: " + Scoring.bestPokerHand[1] + " " + Scoring.bestPokerHand[2] + " " +
                Scoring.bestPokerHand[3] + " " + Scoring.bestPokerHand[4] + " " + Scoring.bestPokerHand[5]);
        shootoutsStarted.setText("Shootouts started: " + Scoring.shootoutsStarted);
        shootoutsWon.setText("Shootouts won: " + Scoring.shootoutsWon);
        shootoutsLost.setText("Shootouts lost: " + Scoring.shootoutsLost);
        bestShootoutScore.setText("Best Shootout score: " + Scoring.bestShootoutScore);
    }

    private void updateScores() {
        scoreMessage.setText("You got a new high score of $" + Scoring.gameScore + "!");
        renoScore.setText("Arriving in Reno: $" + Scoring.renoScore);
        daysScore.setText("Days early: $" + daysEarlyScore);
        posseScore.setText("Posse members: $" + Scoring.posseScore);
        goldScore.setText("Gold: $" + Scoring.goldScore);
        foodScore.setText("Food: $" + Scoring.foodScore);
        ammoScore.setText("Ammo: $" + Scoring.ammoScore);
        finalScore.setText("Final Score: $" + Scoring.gameScore);
    }

    private void limitTextToInitials() {
        UnaryOperator<TextFormatter.Change> initialsFilter = change -> {
            if (change.isContentChange()) {
                if (change.getControlNewText().length() > 3) {
                    return null;
                }
            }
            if (change.getText().matches("[A-Z]+" ) || change.isDeleted()){
                return change; // if change is an initial
            } else if (change.getText().matches("[a-z]+" )){
                change.setText(change.getText().toUpperCase());
                return change; // lowercase letters converted to uppercase
            } else {
                return null;
            }
        };
        inputInitials.setTextFormatter(new TextFormatter(initialsFilter));
    }

    public static StringProperty gameOverMessage = new SimpleStringProperty();

    public String getGameOverMessage() {
        return gameOverMessage.get();
    }

    public StringProperty gameOverMessageProperty() {
        return gameOverMessage;
    }

    public static void setGameOverMessage(String gameOverMessage) {
        GameOver.gameOverMessage.set(gameOverMessage);
    }
}
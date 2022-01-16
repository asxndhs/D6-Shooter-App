package org.openjfx.HighScoreScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.openjfx.App;
import org.openjfx.Logic.Scoring;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Stats implements Initializable {

    public Label gamesPlayed;
    public Label gameWins;
    public Label gameLosses;
    public Label gamesAbandoned;
    public Label winPercentage;
    public Label daysPlayed;
    public Label turnsHistoric;
    public Label gameTimerHistoric;
    public Label spacesTravelledHistoric;
    public Label posseMembersLostHistoric;
    public Label goldSpentHistoric;
    public Label rationsHandedOutHistoric;
    public Label ammoUsedHistoric;
    public Label specialItemsBoughtHistoric;
    public Label townsVisitedHistoric;
    public Label eventsStartedHistoric;
    public Label specialEventsStartedHistoric;
    public Label pokerGamesHistoric;
    public Label pokerWinsHistoric;
    public Label pokerTiesHistoric;
    public Label pokerLossesHistoric;
    public Label wagerWinningsHistoric;
    public Label wagerLossesHistoric;
    public Label bestPokerHandHistoric;
    public Label shootoutsStartedHistoric;
    public Label shootoutsWonHistoric;
    public Label shootoutsLostHistoric;
    public Label daysPlayedBest;
    public Label turnsBest;
    public Label gameTimerBest;
    public Label spacesTravelledBest;
    public Label finishingPosseBest;
    public Label finishingGoldBest;
    public Label finishingFoodBest;
    public Label finishingAmmoBest;
    public Label posseMembersLostBest;
    public Label goldSpentBest;
    public Label rationsHandedOutBest;
    public Label ammoUsedBest;
    public Label specialItemsBoughtBest;
    public Label pokerWinsBest;
    public Label pokerTiesBest;
    public Label pokerLossesBest;
    public Label wagerWinningsBest;
    public Label wagerLossesBest;
    public Label shootoutsStartedBest;
    public Label shootoutsWonBest;
    public Label shootoutsLostBest;

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void switchToHighScores() throws IOException {
        App.setRoot("highScores");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set game timers
        String timerHistoric = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(Scoring.gameTimerHistoric),
                TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimerHistoric) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Scoring.gameTimerHistoric)),
                TimeUnit.MILLISECONDS.toSeconds(Scoring.gameTimerHistoric) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimerHistoric)));
        String timerBest = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(Scoring.gameTimerBest),
                TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimerBest) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Scoring.gameTimerBest)),
                TimeUnit.MILLISECONDS.toSeconds(Scoring.gameTimerBest) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Scoring.gameTimerBest)));

        gamesPlayed.setText("Games played: " + Scoring.gamesPlayed);
        gameWins.setText("Games won: " + Scoring.gameWins);
        gameLosses.setText("Games lost: " + Scoring.gameLosses);
        gamesAbandoned.setText("Games abandoned: " + (Scoring.gamesPlayed - Scoring.gameWins - Scoring.gameLosses));
        if (Scoring.gamesPlayed > 0) {
            winPercentage.setText("Win rate: " + ((100 * Scoring.gameWins) / Scoring.gamesPlayed) + "%");
        } else {
            winPercentage.setText("Win rate: 0%");
        }
        daysPlayed.setText("Days spent on the road: " + Scoring.daysPlayed);
        turnsHistoric.setText("Turns taken: " + Scoring.turnsHistoric);
        gameTimerHistoric.setText("Time played: " + timerHistoric);
        spacesTravelledHistoric.setText("Spaces travelled: " + Scoring.spacesTravelledHistoric);
        posseMembersLostHistoric.setText("Posse members lost: " + Scoring.posseMembersLostHistoric);
        goldSpentHistoric.setText("Gold spent: " + Scoring.goldSpentHistoric);
        rationsHandedOutHistoric.setText("Rations handed out: " + Scoring.rationsHandedOutHistoric);
        ammoUsedHistoric.setText("Ammo used: " + Scoring.ammoUsedHistoric);
        specialItemsBoughtHistoric.setText("Special Items bought: " + Scoring.specialItemsBoughtHistoric);
        townsVisitedHistoric.setText("Towns visited: " + Scoring.townsVisitedHistoric);
        eventsStartedHistoric.setText("Events started: " + Scoring.eventsStartedHistoric);
        specialEventsStartedHistoric.setText("Special Events started: " + Scoring.specialEventsUsedHistoric);
        pokerGamesHistoric.setText("Poker games played: " + Scoring.pokerGamesHistoric);
        pokerWinsHistoric.setText("Poker wins: " + Scoring.pokerWinsHistoric);
        pokerTiesHistoric.setText("Poker ties: " + Scoring.pokerTiesHistoric);
        pokerLossesHistoric.setText("Poker losses: " + Scoring.pokerLossesHistoric);
        wagerWinningsHistoric.setText("Wager winnings: " + Scoring.wagerWinningsHistoric[0] + " Ammo - " +
                Scoring.wagerWinningsHistoric[1] + " Food - " + Scoring.wagerWinningsHistoric[2] + " Gold");
        wagerLossesHistoric.setText("Wager losses: " + Scoring.wagerLossesHistoric[0] + " Ammo - " +
                Scoring.wagerLossesHistoric[1] + " Food - " + Scoring.wagerLossesHistoric[2] + " Gold");
        bestPokerHandHistoric.setText("Best Poker hand: " + Scoring.bestPokerHandHistoric[1] + " " +
                Scoring.bestPokerHandHistoric[2] + " " + Scoring.bestPokerHandHistoric[3] + " " +
                Scoring.bestPokerHandHistoric[4] + " " + Scoring.bestPokerHandHistoric[5]);
        shootoutsStartedHistoric.setText("Shootouts started: " + Scoring.shootoutsStartedHistoric);
        shootoutsWonHistoric.setText("Shootouts won: " + Scoring.shootoutsWonHistoric);
        shootoutsLostHistoric.setText("Shootouts lost: " + Scoring.shootoutsLostHistoric);
        daysPlayedBest.setText("Quickest win (days): " + Scoring.daysPlayedBest);
        turnsBest.setText("Most turns: " + Scoring.turnsBest);
        gameTimerBest.setText("Quickest game: " + timerBest);
        spacesTravelledBest.setText("Most spaces travelled: " + Scoring.spacesTravelledBest);
        finishingPosseBest.setText("Most finishing Posse members: " + Scoring.finishingPosseBest);
        finishingGoldBest.setText("Most finishing Gold: " + Scoring.finishingGoldBest);
        finishingFoodBest.setText("Most finishing Food: " + Scoring.finishingFoodBest);
        finishingAmmoBest.setText("Most finishing Ammo: " + Scoring.finishingAmmoBest);
        posseMembersLostBest.setText("Most Posse members lost: " + Scoring.posseMembersLostBest);
        goldSpentBest.setText("Most Gold spent: " + Scoring.goldSpentBest);
        rationsHandedOutBest.setText("Most Rations handed out: " + Scoring.rationsHandedOutBest);
        ammoUsedBest.setText("Most Ammo used: " + Scoring.ammoUsedBest);
        specialItemsBoughtBest.setText("Most Special Items bought: " + Scoring.specialItemsBoughtBest);
        pokerWinsBest.setText("Most Poker wins: " + Scoring.pokerWinsBest);
        pokerTiesBest.setText("Most Poker ties: " + Scoring.pokerTiesBest);
        pokerLossesBest.setText("Most Poker losses " + Scoring.pokerLossesBest);
        wagerWinningsBest.setText("Most wager winnings: " + Scoring.wagerWinningsBest[0] + " Ammo - " +
                Scoring.wagerWinningsBest[1] + " Food - " + Scoring.wagerWinningsBest[2] + " Gold");
        wagerLossesBest.setText("Most wager losses: " + Scoring.wagerLossesBest[0] + " Ammo - " +
                Scoring.wagerLossesBest[1] + " Food - " + Scoring.wagerLossesBest[2] + " Gold");
        shootoutsStartedBest.setText("Most Shootouts started: " + Scoring.shootoutsStartedBest);
        shootoutsWonBest.setText("Most Shootouts won: " + Scoring.shootoutsWonBest);
        shootoutsLostBest.setText("Most Shootouts lost: " + Scoring.shootoutsLostBest);
    }
}
package org.openjfx.Logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.openjfx.ScoresLeaderboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.openjfx.GameScreens.Events.specialEventUsed;
import static org.openjfx.GameScreens.Game.startTime;
import static org.openjfx.GameScreens.GameOver.playerInitials;
import static org.openjfx.Logic.GameOverMethods.loss;
import static org.openjfx.Logic.GameOverMethods.win;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.ReadSavedData.saveLeaderboards;
import static org.openjfx.Logic.ReadSavedData.saveStats;
import static org.openjfx.SettingsScreens.Settings.*;

public class Scoring {

    // Tracking stats variables
    public static int specialItemsBought;
    public static int turns;
    public static int townsVisited;
    public static int eventsStarted;
    public static int spacesTravelled; // Movement from rolls and shortcuts
    public static long gameTimer; // From game start to game over screen
    public static int pokerWins;
    public static int pokerTies;
    public static int pokerLosses;
    public static int pokerGames; // Sum of wins, ties and losses gives total poker games
    public static Integer[] wagerWinnings = {0,0,0}; // ammo, food, gold
    public static Integer[] wagerLosses = {0,0,0}; // ammo, food, gold
    public static Integer[] bestPokerHand = {0,0,0,0,0,0}; // First element is poker hand value, next five are the rolls
    public static int goldSpent; // Only spent in shop or wagon train
    public static int rationsHandedOut; // Only food used in rations
    public static int posseMembersLost; // All posse loss from events/rolls/rations
    public static int ammoUsed; // Ammo from shootouts and animal/deer herd
    public static int shootoutsWon;
    public static int shootoutsLost;
    public static int shootoutsStarted; // Sum of wins and losses gives total shootouts
    public static int bestShootoutScore;

    // Historic stats variables
    public static int gamesPlayed; // As soon as start turn, add to tracker
    public static int gameWins; // Use win variable to track
    public static int winPercentage; // Calculate as wins/played * 100
    public static int gameLosses; // gamesPlayed - gameWins
    public static int daysPlayed; // Use day variable to track
    public static int specialItemsBoughtHistoric;
    public static int turnsHistoric;
    public static int townsVisitedHistoric;
    public static int eventsStartedHistoric;
    public static int specialEventsUsedHistoric; // Use special event boolean to track in game
    public static int spacesTravelledHistoric;
    public static long gameTimerHistoric; // From game start to game over screen
    public static int pokerWinsHistoric;
    public static int pokerTiesHistoric;
    public static int pokerLossesHistoric;
    public static int pokerGamesHistoric; // Sum of wins, ties and losses gives total poker games
    public static Integer[] wagerWinningsHistoric = {0,0,0};
    public static Integer[] wagerLossesHistoric = {0,0,0};
    public static Integer[] bestPokerHandHistoric = {0,0,0,0,0,0}; // First element is poker hand value, next five are the rolls
    public static int goldSpentHistoric; // Only spent in shop or wagon train
    public static int rationsHandedOutHistoric; // Only food used in rations
    public static int posseMembersLostHistoric; // All posse loss from events/rolls/rations
    public static int ammoUsedHistoric; // Ammo from shootouts and animal/deer herd
    public static int shootoutsWonHistoric;
    public static int shootoutsLostHistoric;
    public static int shootoutsStartedHistoric; // Total shootouts played - sum of wins and losses
    public static int bestShootoutScoreHistoric;

    // Best stats variables
    public static int daysPlayedBest; // Fewest days to win
    public static int finishingPosseBest; // Highest number of posse to finish with
    public static int finishingGoldBest; // Highest number of gold to finish with
    public static int finishingFoodBest; // Highest number of food to finish with
    public static int finishingAmmoBest; // Highest number of ammo to finish with
    public static int specialItemsBoughtBest; // Most special items bought in a game
    public static int turnsBest; // Most turns in a game
    public static int spacesTravelledBest; // Most spaces travelled in a game
    public static long gameTimerBest; // Best/shortest game time
    public static int pokerWinsBest; // Most poker wins in a game
    public static int pokerTiesBest; // Most poker ties in a game
    public static int pokerLossesBest; // Most poker losses in a game
    public static Integer[] wagerWinningsBest = {0,0,0}; // Most wager winnings in a game
    public static Integer[] wagerLossesBest = {0,0,0}; // Most wager losses in a game
    public static int goldSpentBest; // Most gold spent in a game
    public static int rationsHandedOutBest; // Most food handed out in a game
    public static int posseMembersLostBest; // Least posse lost in a game
    public static int ammoUsedBest; // Most ammo used in a game
    public static int shootoutsWonBest; // Most shootouts won in a game
    public static int shootoutsLostBest; // Most shootouts lost in a game
    public static int shootoutsStartedBest; // Most shootouts played in a game - sum of wins and losses

    public static int gameScore;
    public static int renoScore;
    public static int daysEarlyScore;
    public static int posseScore;
    public static int goldScore;
    public static int foodScore;
    public static int ammoScore;

    public static Integer[] scoreLeaderboard = {0, 0, 0, 0, 0}; // Don't initialise
    public static String[] initialsLeaderboard = {"---","---","---","---","---"}; // Don't initialise
    public static ObservableList<ScoresLeaderboard> leaderboard = FXCollections.observableArrayList();
    public static boolean highScore;
    public static int scoreIndex;

    public static void gameScore() {

        // Calculate player's score at the end of the game - only if they won
        renoScore = 10; // Award 10 points for reaching Reno
        System.out.println("+10 points for reaching Reno");
        daysEarlyScore = 2 * (40 - day); // Award 2 points for every day under 40 left on the counter
        System.out.println("+" + daysEarlyScore + " points for " + (40-day) + " remaining days");
        posseScore = 3 * posse; // Award 3 points for every posse member remaining
        System.out.println("+" + posseScore + " points for " + posse + "remaining Posse members");
        goldScore = 2 * gold; // Award 2 points for every piece of gold remaining
        System.out.println("+" + goldScore + " points for " + gold + " remaining Gold");
        foodScore = food; // Award 1 point for each piece of food remaining
        System.out.println("+" + foodScore + " points for " + food + " remaining Food");
        ammoScore = ammo/2; // Award 1 point for every two ammo remaining
        System.out.println("+" + ammoScore + " points for " + ammo + " remaining Ammo");
        gameScore = renoScore + daysEarlyScore + posseScore + goldScore + foodScore + ammoScore;
        System.out.println("\nFinal Score: " + gameScore);
    }

    public static void checkHighScore() {

        // Check if high score
        Arrays.sort(scoreLeaderboard, Collections.reverseOrder()); // Sort array from high to low
        for (int i = 0; i < scoreLeaderboard.length; i++) {
            if (gameScore > scoreLeaderboard[i]) {
                scoreIndex = i;
                highScore = true;
                System.out.println("New high score at index " + scoreIndex);
                break;
            }
        }

        // Update score leaderboard with new score
        if (highScore) {
            scoreLeaderboard[4] = gameScore;
            Arrays.sort(scoreLeaderboard, Collections.reverseOrder()); // Sort array from high to low again
        } else {
            saveLeaderboards(); // Save just in case
        }
        System.out.println(Arrays.deepToString(scoreLeaderboard));
    }

    public static void addInitials() {

        // Place initials in leaderboard at scoreIndex
        if (scoreIndex == 0) {
            initialsLeaderboard[4] = initialsLeaderboard[3];
            initialsLeaderboard[3] = initialsLeaderboard[2];
            initialsLeaderboard[2] = initialsLeaderboard[1];
            initialsLeaderboard[1] = initialsLeaderboard[0];
            initialsLeaderboard[0] = playerInitials;
        } else if (scoreIndex == 1) {
            initialsLeaderboard[4] = initialsLeaderboard[3];
            initialsLeaderboard[3] = initialsLeaderboard[2];
            initialsLeaderboard[2] = initialsLeaderboard[1];
            initialsLeaderboard[1] = playerInitials;
        } else if (scoreIndex == 2) {
            initialsLeaderboard[4] = initialsLeaderboard[3];
            initialsLeaderboard[3] = initialsLeaderboard[2];
            initialsLeaderboard[2] = playerInitials;
        } else if (scoreIndex == 3) {
            initialsLeaderboard[4] = initialsLeaderboard[3];
            initialsLeaderboard[3] = playerInitials;
        } else if (scoreIndex == 4) {
            initialsLeaderboard[4] = playerInitials;
        }
        saveLeaderboards(); // Save high score leaderboard to file
    }

    public static void updateLeaderboard() {

        leaderboard.clear();
        for (int i = 0; i < 5; i++) {
            leaderboard.add(new ScoresLeaderboard((i+1) + ".",initialsLeaderboard[i],"$" + scoreLeaderboard[i]));
        }
    }

    public static void updateStats() {

        // Miscellaneous stats
        shootoutsStarted = shootoutsWon + shootoutsLost;
        pokerGames = pokerWins + pokerTies + pokerLosses;

        // Calculate game time
        gameTimer = System.currentTimeMillis() - startTime;
        String timer = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(gameTimer),
                TimeUnit.MILLISECONDS.toMinutes(gameTimer) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(gameTimer)),
                TimeUnit.MILLISECONDS.toSeconds(gameTimer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(gameTimer)));
        System.out.println(gameTimer + "ms. " + timer);

        // If not a normal game i.e. custom game or easier difficulty or shorter length, do not save historic and best stats
        if (customGame || !gameLengthLong || !gameDifficultyStandard) {
            return;
        }

        // Save and keep track of all player's historic stats
        if (win) {
            gameWins++;
        } else if (loss) {
            gameLosses++;
        }
        winPercentage = (gameWins/gamesPlayed)*100;

        daysPlayed+= day;
        specialItemsBoughtHistoric+= specialItemsBought;
        turnsHistoric+=turns;
        spacesTravelledHistoric+= spacesTravelled;
        gameTimerHistoric+=gameTimer;

        townsVisitedHistoric+= townsVisited;
        eventsStartedHistoric+= eventsStarted;
        if (specialEventUsed) {
            specialEventsUsedHistoric++;
        }

        pokerWinsHistoric+= pokerWins;
        pokerTiesHistoric+= pokerTies;
        pokerLossesHistoric+= pokerLosses;
        pokerGamesHistoric+= pokerGames;
        for (int i = 0; i < 3; i++) {
            wagerWinningsHistoric[i]+= wagerWinnings[i];
        }
        for (int i = 0; i < 3; i++) {
            wagerLossesHistoric[i]+= wagerLosses[i];
        }
        if (bestPokerHand[0] > bestPokerHandHistoric[0]) {
            for (int i = 0; i < 6; i++) {
                bestPokerHandHistoric[i] = bestPokerHand[i];
            }
        }

        goldSpentHistoric+= goldSpent; // Only spent in shop or wagon train
        rationsHandedOutHistoric+= rationsHandedOut; // Only food used in rations
        posseMembersLostHistoric+= posseMembersLost; // All posse loss from events/rolls/rations
        ammoUsedHistoric+= ammoUsed; // Ammo from shootouts and animal/deer herd

        shootoutsWonHistoric+= shootoutsWon;
        shootoutsLostHistoric+= shootoutsLost;
        shootoutsStartedHistoric = shootoutsWonHistoric + shootoutsLostHistoric;
        if (bestShootoutScore > bestShootoutScoreHistoric) {
            bestShootoutScoreHistoric = bestShootoutScore;
        }

        // Save and keep track of all player's best stats
        if (day < daysPlayedBest) {
            daysPlayedBest = day; // Fewest days to win
        }
        if (posse > finishingPosseBest) {
            finishingPosseBest = posse; // Highest number of posse to finish with
        }
        if (gold > finishingGoldBest) {
            finishingGoldBest = gold; // Highest number of gold to finish with
        }
        if (food > finishingFoodBest) {
            finishingFoodBest = food; // Highest number of food to finish with
        }
        if (ammo > ammoUsedBest) {
            finishingAmmoBest = ammo; // Highest number of ammo to finish with
        }
        if (specialItemsBought > specialItemsBoughtBest) {
            specialItemsBoughtBest = specialItemsBought; // Most special items bought in a game
        }
        if (turns > turnsBest) {
            turnsBest = turns; // Most turns in a game
        }
        if (spacesTravelled > spacesTravelledBest) {
            spacesTravelledBest = spacesTravelled; // Most spaces travelled in a game
        }
        if (gameTimer < gameTimerBest) {
            gameTimerBest = gameTimer; // Best game time
        }
        if (pokerWins > pokerWinsBest) {
            pokerWinsBest = pokerWins; // Most poker wins in a game
        }
        if (pokerTies > pokerTiesBest) {
            pokerTiesBest = pokerTies; // Most poker ties in a game
        }
        if (pokerLosses > pokerLossesBest) {
            pokerLossesBest = pokerLosses; // Most poker losses in a game
        }
        if ((wagerWinnings[0] + wagerWinnings[1] + wagerWinnings[2]) > (wagerWinningsBest[0] + wagerWinningsBest[1] + wagerWinningsBest[2])) {
            for (int i = 0; i < 3; i++) {
                wagerWinningsBest[i]+= wagerWinnings[i]; // Most wager winnings in a game
            }
        }
        if ((wagerLosses[0] + wagerLosses[1] + wagerLosses[2]) > (wagerLossesBest[0] + wagerLossesBest[1] + wagerLossesBest[2])) {
            for (int i = 0; i < 3; i++) {
                wagerLossesBest[i] += wagerLosses[i]; // Most wager losses in a game
            }
        }
        if (goldSpent > goldSpentBest) {
            goldSpentBest = goldSpent; // Most gold spent in a game
        }
        if (rationsHandedOut > rationsHandedOutBest) {
            rationsHandedOutBest = rationsHandedOut; // Most food handed out in a game
        }
        if (posseMembersLost < posseMembersLostBest) {
            posseMembersLostBest = posseMembersLost; // Least posse lost in a game
        }
        if (ammoUsed > ammoUsedBest) {
            ammoUsedBest = ammoUsed; // Most ammo used in a game
        }
        if (shootoutsWon > shootoutsWonBest) {
            shootoutsWonBest = shootoutsWon; // Most shootouts won in a game
        }
        if (shootoutsLost > shootoutsLostBest) {
            shootoutsLostBest = shootoutsLost; // Most shootouts lost in a game
        }
        if (shootoutsStarted > shootoutsStartedBest) {
            shootoutsStartedBest = shootoutsStarted; // Most shootouts in a game
        }

        saveStats();
    }
}

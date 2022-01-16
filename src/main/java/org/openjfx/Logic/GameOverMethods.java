package org.openjfx.Logic;

import org.openjfx.App;
import java.io.IOException;

import static org.openjfx.GameScreens.GameOver.setGameOverMessage;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.RationsMethods.rationsGameOver;
import static org.openjfx.Logic.Scoring.*;

public class GameOverMethods {

    public static boolean win = false;
    public static boolean loss = false;

    public static void gameOverMethods() throws IOException {

        updateStats();

        // Go to game over scene
        System.out.println("Player reached space " + playerSpace + " on Day " + day + ".\nGame Over");
        App.setRoot("gameOver");
    }

    public static void finishCheck() throws IOException {

        // Is the player at the finish
        // Player wins and all unresolved dice are discarded, rations are ignored
        if (playerSpace >= finish) {
            win = true;
            playerSpace = finish; // Make sure player ends on finish space
            System.out.println("Player has reached Reno in " + day + " days!");
            setGameOverMessage("You reached Reno in " + day + " days!");
            gameOverMethods();
        }
    }

    public static void finalDayCheck() throws IOException {

        // Is it the end of day 40 (or whatever the time limit has been set to)
        // Carry out check at the end of the turn/beginning of next turn only - after finishCheck() at the end of the turn
        if (day > 40) {
            loss = true;
            System.out.println("Player has run out of time.\nGame Over!");
            setGameOverMessage("You did not reach Reno in time!");
            gameOverMethods();
        }
    }

    public static void posseCheck() throws IOException {

        // Does the player have at least one posse left
        if (posse < 1) {
            loss = true;
            System.out.println("There are no member's remaining in player's Posse.\nGame Over!");
            setGameOverMessage("There's no one in the Posse left to continue the journey!");
            gameOverMethods();
        }
    }

    public static void rationsFoodCheck() throws IOException {

        // Does the player have enough food on rations day i.e. at least one food on rations day
        // Use check at the beginning of rations day method
        if (rationsGameOver) {
            loss = true;
            System.out.println("Player does not have enough food to give out Rations. Game Over!");
            setGameOverMessage("You didn't have any Food for Rations day and now the Posse has disbanded");
            gameOverMethods();
        }
    }
}

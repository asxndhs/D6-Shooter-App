package org.openjfx.Logic;

import static org.openjfx.GameScreens.PosseLoss.posseLossFromScene;
import static org.openjfx.GameScreens.Shootout.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.Scoring.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class ShootoutMethods {

    // Create Shootout variables
    public static int playerShootoutScore;
    public static int opponentShootoutScore;
    public static Boolean shootoutWin = false;
    public static Boolean shootoutLoss = false;
    public static Boolean replayShootout = false;
    public static Boolean prisonerShootoutLoss = false;

    // Create Event Shootout variables;
    public static int eventPlayerDice;
    public static int eventOpponentDice;

    public static void shootout() {

        // Determine player's Shootout score
        shootoutDiceArray.clear(); // Clear array
        playerShootoutScore = 0; // Reset score
        for (int i = 0; i < fight; i++) {
            shootoutDiceArray.add((int)(Math.random()*6+1));
            System.out.println("Fight dice " + i + " rolled as " + shootoutDiceArray.get(i));
            playerShootoutScore+= shootoutDiceArray.get(i);
        }
        playerShootoutScore+= ammo; // Add on player's ammo count
        if (betterGunsInventory) {
            playerShootoutScore += betterGunsModifier; // Add on better guns modifier if in player inventory
        }
        System.out.println("Player Shootout score: " + playerShootoutScore);

        // Player shootout score tracking
        if (playerShootoutScore > bestShootoutScore) {
            System.out.println("New best shootout score");
            bestShootoutScore = playerShootoutScore;
        }

        // Determine opponent's Shootout score
        opponentDiceArray.clear(); // Clear array
        opponentShootoutScore = 0; // Reset score
        for (int i = 0; i < sixes; i++) {
            opponentDiceArray.add((int)(Math.random()*6+1));
            System.out.println("Opponent dice " + i + " rolled as " + opponentDiceArray.get(i));
            opponentShootoutScore+= opponentDiceArray.get(i);
        }
        System.out.println("Opponent Shootout score: " + opponentShootoutScore);
    }

    public static void resolveShootoutRound() {

        // Reset shootout booleans
        shootoutWin = false;
        shootoutLoss = false;
        replayShootout = false;

        // Compare scores and remove a die from the losing side
        if (playerShootoutScore == opponentShootoutScore) {
            System.out.println("\nShootout ended as a tie. Replaying Shootout...");
            replayShootout = true;
            return;
        } else if (playerShootoutScore > opponentShootoutScore) {
            System.out.println("\nPlayer wins Shootout. Opponent loses one Shootout dice");
            sixes--;
            shootoutWin = true;
        } else {
            System.out.println("\nPlayer loses Shootout. Player loses one Shootout dice");
            fight--;
            shootoutLoss = true;
        }

        // Check if both sides still have at least one die and loop to commence another Shootout
        if (fight > 0 && sixes > 0) {
            System.out.println("\nStarting the next Shootout");
            replayShootout = true;
        }
    }

    public static void resolveShootoutEffects() {

        // If one side has no dice remaining, determine end result
        posseChange = 0; // Reset posse change counter
        if (sixes == 0) {
            shootoutsWon++; // Stat tracking shootout wins
            System.out.println("\nOpponent has no Shootout dice remaining. Player has defeated opponent in the Shootout");
        } else if (fight == 0) {
            shootoutsLost++; // Stat tracking shootout losses
            System.out.println("\nPlayer has no Shootout dice remaining. Opponent has defeated player in the Shootout");
            System.out.println("Opponent has " + sixes + " Shootout dice remaining. Player loses " + sixes + " Posse");
            prisonerShootoutLoss = true;
            posseChange = sixes;
            posseLossFromScene = "shootout";
            posseLossLoop();
            System.out.println(posse + " Posse remaining");
        }
    }

    public static void shootoutReduceAmmo() {

        // Reduce player's ammo count
        if (ammo > 0) {
            System.out.println("\nPlayer ammo count reduced by one");
            ammo--;
            ammoUsed++; // Stat tracking for ammo used
            System.out.println(ammo + " Ammo remaining");
        } else {
            System.out.println("\nPlayer ammo count is zero. Player loses no ammo this Shootout");
        }
    }

    public static void banditsShootout() {

        // Determine player's Shootout score
        shootoutDiceArray.clear(); // Clear array
        playerShootoutScore = 0; // Reset score
        for (int i = 0; i < eventPlayerDice; i++) {
            shootoutDiceArray.add((int)(Math.random()*6+1));
            System.out.println("Fight dice " + i + " rolled as " + shootoutDiceArray.get(i));
            playerShootoutScore+= shootoutDiceArray.get(i);
        }
        playerShootoutScore+= ammo; // Add on player's ammo count
         if (betterGunsInventory) {
         playerShootoutScore += betterGunsModifier; // Add on better guns modifier if in player inventory
         }
        System.out.println("Player Shootout score: " + playerShootoutScore);

        // Player shootout score tracking
        if (playerShootoutScore > bestShootoutScore) {
            System.out.println("New best shootout score");
            bestShootoutScore = playerShootoutScore;
        }

        // Determine opponent's Shootout score
        opponentDiceArray.clear(); // Clear array
        opponentShootoutScore = 0; // Reset score
        for (int i = 0; i < eventOpponentDice; i++) {
            opponentDiceArray.add((int)(Math.random()*6+1));
            System.out.println("Opponent dice " + i + " rolled as " + opponentDiceArray.get(i));
            opponentShootoutScore+= opponentDiceArray.get(i);
        }
        System.out.println("Opponent Shootout score: " + opponentShootoutScore);
    }

    public static void banditsResolveShootoutRound() {

        // Reset shootout booleans
        shootoutWin = false;
        shootoutLoss = false;
        replayShootout = false;

        // Compare scores and remove a die from the losing side
        if (playerShootoutScore == opponentShootoutScore) {
            System.out.println("\nShootout ended as a tie. Replaying Shootout...");
            replayShootout = true;
            return;
        } else if (playerShootoutScore > opponentShootoutScore) {
            System.out.println("\nPlayer wins Shootout. Opponent loses one Shootout dice");
            eventOpponentDice--;
            shootoutWin = true;
        } else {
            System.out.println("\nPlayer loses Shootout. Player loses one Shootout dice");
            eventPlayerDice--;
            shootoutLoss = true;
        }

        // Check if both sides still have at least one die and loop to commence another Shootout
        if (eventPlayerDice > 0 && eventOpponentDice > 0) {
            System.out.println("\nStarting the next Shootout");
            replayShootout = true;
        }
    }

    public static void banditsResolveShootoutEffects() {

        // If one side has no dice remaining, determine end result
        posseChange = 0; // Reset posse change counter
        if (eventOpponentDice == 0) {
            shootoutsWon++; // Stat tracking shootout wins
            System.out.println("\nOpponent has no Shootout dice remaining. Player has defeated opponent in the Shootout");
        } else if (eventPlayerDice == 0) {
            shootoutsLost++; // Stat tracking shootout losses
            System.out.println("\nPlayer has no Shootout dice remaining. Opponent has defeated player in the Shootout");
            System.out.println("Opponent has " + eventOpponentDice + " Shootout dice remaining. Player loses " + eventOpponentDice + " Posse");
            prisonerShootoutLoss = true;
            posseChange = sixes;
            posseLossFromScene = "shootout";
            posseLossLoop();
            System.out.println(posse + " Posse remaining");
        }
    }
}

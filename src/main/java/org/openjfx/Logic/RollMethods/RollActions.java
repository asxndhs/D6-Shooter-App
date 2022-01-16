package org.openjfx.Logic.RollMethods;

import java.io.IOException;

import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.GameScreens.ResolveDice.*;
import static org.openjfx.GameScreens.PickRoute.pickRouteFromScene;
import static org.openjfx.GameScreens.SpecialRoll.specialRollDiceArray;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.GameOverMethods.finalDayCheck;
import static org.openjfx.Logic.RationsMethods.*;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.Scoring.posseMembersLost;
import static org.openjfx.Logic.Scoring.spacesTravelled;
import static org.openjfx.Logic.TownMethods.SpecialItems.prospectorsMap;
import static org.openjfx.Logic.TownMethods.SpecialItems.prospectorsMapInventory;

public class RollActions {

    // Create 4s options variables
    public static int hide;
    public static int seekShelter;
    public static int backroads;
    public static int fight;
    public static int remainingHideDice;

    // Create 5 roll counter variable
    public static int badFiveCounter;

    // 5/6 change variables
    public static int sixesChange;
    public static int fivesChange;

    public static void oneRoll() throws IOException {

        movement = ones;

        // For loop adding one to player space incrementally so that movement can be interrupted for an Event or Town
        currentSpace = playerSpace;
        pickRouteFromScene = "resolveDice";
        for (int i = 0; i < movement; i++) {
            playerSpace++;
            spacesTravelled++; // Stat tracking for spaces travelled
            System.out.println("\nPlayer moves forward 1 space");
            spaceChecks(); // Method to check whether player lands on Event, Town or finish space, or passed pick route space
            if (eventSpace || pickRouteSpace) {
                return;
            }
        }
        System.out.println("All 1s resolved. Player moved forward a total of " + (playerSpace - currentSpace) + " spaces");
        System.out.println("Player is now on " + playerSpace + " space");
    }

    public static void twoRoll() {

        foodChange = twos/2;
        if (foodChange+food > 12) {
            foodChange = 12 - food;
            System.out.println("Player has reached max Food");
        }
        food += foodChange;
        System.out.println("\nPlayer rolled " + twos + " 2(s) and gains " + foodChange + " Food");
        System.out.println("Player now has " + food + " Food");
    }

    public static void threeRoll() {

        if (prospectorsMapInventory) {
            System.out.println("Using Prospector's Map to reduce requirement for Gold");
            prospectorsMap(); // Use prospector's map method instead
            return;
        }

        goldChange = threes/3;
        gold+= goldChange;
        System.out.println("\nPlayer rolled " + threes + " 3(s) and gains " + goldChange + " Gold");
        System.out.println("Player now has " + gold + " Gold\n");
    }

    public static void hideRoll() throws IOException {

        // Hide - remove two 6s and add one day for each 4
        int totalHideDice = hide;
        for (int i = 0; i < totalHideDice; i++) {
            int x = sixes;
            sixes = Math.max(0, sixes - 2); // remove two 6s
            x -= sixes;
            sixesChange+=x;
            day++; // add one day
            hide--; // take away hide dice that has been resolved
            finalDayCheck(); // Check it hasn't passed the end of day 40
            rationsFromScene = "resolveDice";
            checkRations(); // Rations check before moving onto next day
            if (rationsDay) {
                remainingHideDice = totalHideDice - hide;
                return;
            }
        }
        System.out.println(sixesChange + " 6s removed and " + (totalHideDice + remainingHideDice) + " days added");
        System.out.println(sixes + " 6s remaining");
        System.out.println("Day " + day + "\n");
        hideMessage = "You have " + (totalHideDice + remainingHideDice) + " Hide dice.\n" + sixesChange + " 6(s) have been removed and " + (totalHideDice + remainingHideDice) + " day(s) have been added. It is now Day " + day;
        hideChecked = true;
    }

    public static void seekShelterRoll() {

        // Seek Shelter - remove one 5 for each 4
        fivesChange = 0;
        for (int i = 0; i < seekShelter; i++) {
            if (fives > 0) {
                fivesChange++;
            }
            fives = Math.max(0, fives - 1); // remove one 5
        }
        System.out.println(fivesChange + " 5s removed");
        System.out.println(fives + " 5s remaining");
    }

    public static void backroadsRoll() throws IOException {

        // Backroads - move one space for every two 4s
        movement = backroads/2;
        // For loop adding one to player space incrementally so that movement can be interrupted for an Event or Town
        currentSpace = playerSpace;
        pickRouteFromScene = "resolveDice";
        for (int i = 0; i < movement; i++) {
            playerSpace++;
            spacesTravelled++; // Stat tracking for spaces travelled
            System.out.println("Player moves forward 1 space");
            spaceChecks(); // Method to check whether player lands on Event, Town or finish space, or passed a pick route space
            if (eventSpace || pickRouteSpace) {
                return;
            }
        }
        System.out.println("\nAll Backroads 4s resolved. Player moved forward a total of " + (playerSpace - currentSpace) + " spaces");
        System.out.println("Player is now on " + playerSpace + " space\n");
    }

    public static void fiveRoll() {

        // Reset variables
        badFiveCounter = 0;
        specialRollDiceArray.clear(); // Reset special roll dice array to zeros

        // Re-roll fives to determine effect
        for (int i = 0; i < fives; i++) {
            specialRollDiceArray.add((int)(Math.random()*6+1));
            System.out.println(i + " 5 rolled a " + specialRollDiceArray.get(i));
            if (specialRollDiceArray.get(i) >= 3) {
                badFiveCounter++;
            }
        }
        System.out.println("Player rolled " + badFiveCounter + " actionable rolls");
    }

    public static void sixRoll() throws IOException {

        posseChange = 0; // Resetting posse change variable
        specialRollDiceArray.clear(); // Reset special roll dice array to zeros
        System.out.println("No Fight dice used. Re-rolling 6s to determine effects");
        for (int i = 0; i < sixes; i++) {
            specialRollDiceArray.add((int)(Math.random()*6+1)); // Re-rolling 6s to determine effects
            System.out.println(i + " 6 rolled a " + specialRollDiceArray.get(i));
            if (specialRollDiceArray.get(i) >=3) {
                posseChange++;
            }
        }

        // Lose posse
        posseLossFromScene = "specialRoll";
        startingPosse = posse;
        posseLossLeft = posseChange;
        for (int i = 0; i < posseChange; i++) {
            posse--;
            posseLossLeft--;
            posseMembersLost++; // Stat tracking for posse loss during the game
            System.out.println("Player loses one Posse. They now have " + posse + " Posse members remaining");
            posseLossChecks();
            if (stopPosseLoss) {
                return;
            }
        }

        System.out.println("All 6s resolved. Player lost " + posseChange + " Posse");
        System.out.println(posse + " Posse remaining");
    }

}

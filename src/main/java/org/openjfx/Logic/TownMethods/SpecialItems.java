package org.openjfx.Logic.TownMethods;

import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.Scoring.posseMembersLost;

public class SpecialItems {

    // Create special items shop and inventory booleans
    public static boolean compassShop;
    public static boolean compassInventory;
    public static boolean hunterShop;
    public static boolean hunterInventory;
    public static boolean prospectorsMapShop;
    public static boolean prospectorsMapInventory;
    public static boolean binocularsShop;
    public static boolean binocularsInventory;
    public static boolean medicineBandagesShop;
    public static boolean medicineBandagesInventory;
    public static boolean betterGunsShop;
    public static boolean betterGunsInventory;

    public static Boolean binocularsCounter = false;
    public static int betterGunsModifier;
    public static int compassUsed;
    public static int hunterUsed;
    public static Boolean hunterLost = false;
    public static Boolean prospectorsMapUsed = false;
    public static Boolean negatePosseLoss = false;

    public static void compass() {

        // Method applied after countRolls() only if compassInventory is true. Check if red dice 1, 2 & 3 are 1s and add extra to ones
        if (redDie1 == 1) {
            ones++;
            System.out.println("Red 1 ability doubled by Compass");
            compassUsed++;
        } else if (redDie2 == 1) {
            ones++;
            System.out.println("Red 1 ability doubled by Compass");
            compassUsed++;
        } else if (redDie3 == 1) {
            ones++;
            System.out.println("Red 1 ability doubled by Compass");
            compassUsed++;
        } else {
            System.out.println("Compass could not be used this turn as no red 1s have been rolled");
        }
    }

    public static void hunter() {

        // Method applied after countRolls() only if hunterInventory is true. Check if red dice 1, 2 & 3 are 2s and add extra to twos
        if (redDie1 == 2) {
            twos++;
            System.out.println("Red 2 ability doubled by Hunter");
            hunterUsed++;
        } else if (redDie2 == 2) {
            twos++;
            System.out.println("Red 2 ability doubled by Hunter");
            hunterUsed++;
        } else if (redDie3 == 2) {
            twos++;
            System.out.println("Red 2 ability doubled by Hunter");
            hunterUsed++;
        } else {
            System.out.println("Hunter could not be used this turn as no red 2s have been rolled");
        }
    }

    public static void hunterPosseCheck() {

        // When player falls to one posse, the hunter disappears
        if (posse < 2) {
            hunterInventory = false;
            hunterLost = true; // To be reset on the pop up
            System.out.println("There is only one Posse member remaining. Hunter has left the group");
        }
    }

    public static void prospectorsMap() {

        // Method used only if prospectorsMapInventory is true
        // threeRoll() method but now only requires two 3s for one Gold
        goldChange = threes/2;
        gold+= goldChange;
        if (goldChange > 0) {
            prospectorsMapUsed = true; // Set true only if player gains gold because of ability
        }
        System.out.println("\nPlayer rolled " + threes + " 3(s) and gains " + goldChange + " Gold");
        System.out.println("Player now has " + gold + " Gold\n");
    }

    public static void binocularsCheck() {
        // Identify which red dice are currently locked
        binocularsCounter = false; // Reset counter
        // Check locked red die 1 is a 5 or 6 (not locked to 1-4 from Indian Guide Event ability)
        if (redDie1 == 5) {
            binocularsCounter = true;
            System.out.println("Red die 1 is locked at 5");
        }
        if (redDie2 == 5) {
            binocularsCounter = true;
            System.out.println("Red die 2 is locked at 5");
        }
        if (redDie3 == 5) {
            binocularsCounter = true;
            System.out.println("Red die 3 is locked at 5");
        }

        if (!binocularsCounter) {
            System.out.println("Binoculars cannot be used this turn as there are no red 5s");
        }
    }

    public static void binocularsAbility() {

        if (redDie1 == 5) {
            redDie1Lock = false;
            redDie1ForceLock = false;
            System.out.println("Red die 1 unlocked");
        } else if (redDie2 == 5) {
            redDie2Lock = false;
            redDie2ForceLock = false;
            System.out.println("Red die 2 unlocked");
        } else if (redDie3 == 5) {
            redDie3Lock = false;
            redDie3ForceLock = false;
            System.out.println("Red die 3 unlocked");
        }
    }

    public static void medicineBandages() {

        posse++; // Add the posse member back to the posse
        posseMembersLost--; // Stat tracking for posse loss during the game
        medicineBandagesInventory = false;
        negatePosseLoss = true;
        System.out.println("Player has used Medicine/Bandages and negated the Posse loss. Player now has " + posse + " Posse remaining");
    }
}

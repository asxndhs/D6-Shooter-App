package org.openjfx.Logic.RollMethods;

import java.util.Arrays;

import static org.openjfx.Logic.EventMethods.FullEvents.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class Roll {

    // Create dice variables
    public static int whiteDie1 = 0;
    public static int whiteDie2 = 0;
    public static int whiteDie3 = 0;
    public static int whiteDie4 = 0;
    public static int whiteDie5 = 0;
    public static int redDie1 = 0;
    public static int redDie2 = 0;
    public static int redDie3 = 0;
    public static int rollCounter;

    // Create dice locking variables
    public static boolean whiteDie1Lock = false;
    public static boolean whiteDie2Lock = false;
    public static boolean whiteDie3Lock = false;
    public static boolean whiteDie4Lock = false;
    public static boolean whiteDie5Lock = false;
    public static boolean redDie1Lock = false;
    public static boolean redDie2Lock = false;
    public static boolean redDie3Lock = false;
    public static boolean redDie1ForceLock = false;
    public static boolean redDie2ForceLock = false;
    public static boolean redDie3ForceLock = false;

    // More roll variables
    public static Integer[] rollArray = new Integer[10];
    public static Integer[] countArray = new Integer[6];
    public static int ones;
    public static int twos;
    public static int threes;
    public static int fours;
    public static int fives;
    public static int sixes;

    public static void resetDiceVariables() {

        rollCounter = 0;
        whiteDie1 = 0;
        whiteDie2 = 0;
        whiteDie3 = 0;
        whiteDie4 = 0;
        whiteDie5 = 0;
        redDie1 = 0;
        redDie2 = 0;
        redDie3 = 0;
        // Event dice
        eventDie1 = 0;
        eventDie2 = 0;

        whiteDie1Lock = false;
        whiteDie2Lock = false;
        whiteDie3Lock = false;
        whiteDie4Lock = false;
        whiteDie5Lock = false;
        redDie1Lock = false;
        redDie2Lock = false;
        redDie3Lock = false;
        // Event dice
        eventDie1Lock = false;
        eventDie2Lock = false;

        redDie1ForceLock = false;
        redDie2ForceLock = false;
        redDie3ForceLock = false;

        // Reset arrays
        Arrays.fill(rollArray, 0);
        Arrays.fill(countArray, 0);
    }

    public static void forceLock() {

        // Check if any of the red dice roll a 5 or 6 and lock them if so
        if (redDie1 > 4) {
            redDie1Lock = true;
            redDie1ForceLock = true;
            System.out.println("Red die 1 locked at " + redDie1);
        }
        if (redDie2 > 4) {
            redDie2Lock = true;
            redDie2ForceLock = true;
            System.out.println("Red die 2 locked at " + redDie2);
        }
        if (redDie3 > 4) {
            redDie3Lock = true;
            redDie3ForceLock = true;
            System.out.println("Red die 3 locked at " + redDie3);
        }
    }

    public static void rollDice() {

        rollCounter++; // Increment roll counter
        System.out.println("Roll number " + rollCounter);

        if (!whiteDie1Lock) {
            rollArray[0] = whiteDie1 = (int)(Math.random()*6+1);
        }
        System.out.println("White Die 1: " + whiteDie1);

        if (!whiteDie2Lock) {
            rollArray[1] = whiteDie2 = (int)(Math.random()*6+1);
        }
        System.out.println("White Die 2: " + whiteDie2);

        if (!whiteDie3Lock) {
            rollArray[2] = whiteDie3 = (int)(Math.random()*6+1);
        }
        System.out.println("White Die 3: " + whiteDie3);

        if (!whiteDie4Lock) {
            rollArray[3] = whiteDie4 = (int)(Math.random()*6+1);
        }
        System.out.println("White Die 4: " + whiteDie4);

        if (!whiteDie5Lock) {
            rollArray[4] = whiteDie5 = (int)(Math.random()*6+1);
        }
        System.out.println("White Die 5: " + whiteDie5);

        if (!redDie1Lock && !prisonerRedDie1Lock) {
            rollArray[5] = redDie1 = (int)(Math.random()*6+1);
        }
        System.out.println("Red Die 1: " + redDie1);

        if (!redDie2Lock && !prisonerRedDie2Lock) {
            rollArray[6] = redDie2 = (int)(Math.random()*6+1);
        }
        System.out.println("Red Die 2: " + redDie2);

        if (!redDie3Lock && !prisonerRedDie3Lock) {
            rollArray[7] = redDie3 = (int)(Math.random()*6+1);
        }
        System.out.println("Red Die 3: " + redDie3);

        // Event dice
        if (downhillEvent && !eventDie1Lock) {
            rollArray[8] = eventDie1 = (int)(Math.random()*6+1);
            System.out.println("Downhill Ride Die: " + eventDie1);
        } else if (crittersEvent && !eventDie1Lock) {
            rollArray[8] = eventDie1 = (int) (Math.random() * 6 + 1);
            System.out.println("Critters Everywhere Die 1: " + eventDie1);
        }

        if (crittersEvent && !eventDie2Lock) {
            rollArray[9] = eventDie2 = (int) (Math.random() * 6 + 1);
            System.out.println("Critters Everywhere Die 2: " + eventDie2);
        }
    }

    public static void countRolls() {

        for (int j : rollArray) {
            if (j > 0 && j <= 6) {
                countArray[j - 1]++;
            } else {
                System.out.println("Unexpected dice roll of " + j);
            }
        }
        System.out.println(Arrays.toString(countArray));

        // Assign variables to countArray
        ones = countArray[0];
        twos = countArray[1];
        threes = countArray[2];
        fours = countArray[3];
        fives = countArray[4];
        sixes = countArray[5];

        // Add special item abilities to rolls
        if (compassInventory) {
            compass();
        }
        if (hunterInventory) {
            hunter();
        }
    }
}

package org.openjfx.Logic;

import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.GameScreens.PosseLoss.stopPosseLoss;
import static org.openjfx.Logic.EventMethods.FullEvents.desertersEvent;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.GameScreens.Rations.*;
import static org.openjfx.Logic.Map.finish;
import static org.openjfx.Logic.Scoring.rationsHandedOut;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class RationsMethods {

    public static String rationsFromScene;
    public static Boolean rationsDay = false;
    public static Boolean rationsGameOver = false;

    public static void checkRations() throws IOException {

        // After a day has passed, check if previous day was a multiple of 5
        if (day > 1 && (day-1) % 5 == 0 && (day-1) != finish) {
            rationsDay = true;
            System.out.println("Day " + (day-1) + " is a Rations day. Time to hand out rations");
            setRationsRequirements("You have " + food + " Food and " + posse + " Posse members to feed");
            if (food < 1) {
                rationsGameOver = true;
            } else {
                rations();
            }
            App.setRoot("rations");
        }
    }

    public static void rations() throws IOException {

        // Determine whether the player has enough food for their whole posse or not, and resolve
        posseChange = 0;
        foodChange = 0;
        if (posse > food) {
            posseChange = posse - food;
            System.out.println("Player does not have enough food to feed their whole Posse");
            System.out.println("Player has " + food + " Food and " + posse + " Posse. " + posseChange + " members have left the Posse");
            foodChange = food;
            food = 0;
            rationsHandedOut += foodChange; // Stat tracking for food handed out
            System.out.println("Player now has " + food + " Food and " + posse + " Posse");
            // Posse checks
            posseLossFromScene = "rations";
            posseLossLoop();
            setRationsResult("You lost " + posseChange + " Posse members and used up all your Food. You now have " + posse + " Posse members remaining");
        } else {
            foodChange = posse;
            System.out.println("Player has " + food + " Food and " + posse + " Posse. " + foodChange + " Food given out for rations");
            food-= foodChange;
            rationsHandedOut += foodChange; // Stat tracking for food handed out
            System.out.println("Player has " + food + " Food remaining");
            setRationsResult("You gave out " + foodChange + " Food to your " + posse + " Posse members. You now have " + food + " Food remaining");
        }
    }
}

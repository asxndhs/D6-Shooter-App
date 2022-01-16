package org.openjfx.Logic;

import java.io.IOException;

import static org.openjfx.GameScreens.Game.setDaySpaceStatus;
import static org.openjfx.GameScreens.Game.setInventoryStatus;
import static org.openjfx.Logic.GameOverMethods.finalDayCheck;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.playerSpace;
import static org.openjfx.Logic.RationsMethods.checkRations;
import static org.openjfx.Logic.RationsMethods.rationsFromScene;

public class Day {

    public static int day;
    public static int timeLimit;

    public static void startOfTurn() throws IOException {

        day++;
        finalDayCheck(); // Check it hasn't passed the end of day 40
        rationsFromScene = "game";
        checkRations(); // Rations check before moving onto next day
        setDaySpaceStatus("It is Day " + day + " and you are on space " + playerSpace);
        setInventoryStatus("You have " + posse + " Posse members and an inventory of " + gold + " Gold, " + food + " Food and " + ammo + " Ammo");
        System.out.println("Day " + day);
    }
}

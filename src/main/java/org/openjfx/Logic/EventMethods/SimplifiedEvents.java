package org.openjfx.Logic.EventMethods;

import java.io.IOException;

import static org.openjfx.GameScreens.GameOver.setGameOverMessage;
import static org.openjfx.GameScreens.PickRoute.pickRouteFromScene;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.GameOverMethods.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.RationsMethods.checkRations;
import static org.openjfx.Logic.RationsMethods.rationsFromScene;
import static org.openjfx.Logic.Scoring.spacesTravelled;

public class SimplifiedEvents {

    public static String event1Message;
    public static String event5Message;

    public static void shortcut() throws IOException {

        System.out.println("\nEvent - Shortcut");
        eventSpace = false; // needs to be changed to allow shortcut to move properly

        // Move 3 spaces forward
        movement = 3;
        movement += extraMovement;
        currentSpace = playerSpace;
        pickRouteFromScene = "events";
        for (int i = 0; i < movement; i++) {
            playerSpace++;
            spacesTravelled++; // Stat tracking for spaces travelled
            event1Message = "Move forward one space. You are now on space " + playerSpace;
            System.out.println(event1Message);
            if (extraMovement > 0) {
                extraMovement--;
            }
            spaceChecks();
            if (eventSpace || pickRouteSpace) {
                return;
            }
        }
        event1Message = "You moved forward " + (playerSpace - currentSpace) + " space(s) onto space " + playerSpace;
    }

    public static void lost() throws IOException {

        // Should food be lost first before day added (in case of following rations day)

        System.out.println("\nEvent - Lost");

        // Add 1 Day and lose 1 food
        if (food < 1) {
            System.out.println("Player does not have enough Food to fulfill this Event. Game Over");
            setGameOverMessage("You ran out of Food to feed your Posse during this Event");
            loss = true;
            gameOverMethods();
            return;
        }
        food--;
        System.out.println("Player loses one Food");
        day++;
        System.out.println("Player loses one day");
        System.out.println("It is now Day " + day + " and player has " + food + " Food");
        finalDayCheck(); // Check it hasn't passed the end of day 40
        rationsFromScene = "events"; // TODO: test these day check methods work
        checkRations(); // Rations check before moving onto next day
        event5Message = "You lose 1 Day and 1 Food. It is now Day " + day + " and you have " + food + " Food";
    }
}

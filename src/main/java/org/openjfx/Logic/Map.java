package org.openjfx.Logic;

import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.GameScreens.PickRoute.setPickRouteOptions;
import static org.openjfx.Logic.EventMethods.EventCall.checkEvent;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.GameOverMethods.finishCheck;
import static org.openjfx.Logic.Scoring.spacesTravelled;
import static org.openjfx.Logic.TownMethods.FindTown.checkTown;

public class Map {

    public static int playerSpace;
    public static int currentSpace;
    public static int movement;
    public static int extraMovement;
    static int[] pickRouteSpaces = {44,74};
    static int[] endShortcutSpaces = {46,78};
    static int finish;
    public static boolean shortcutTaken = false;
    public static boolean pickRouteSpace = false;

    public static void spaceChecks() throws IOException {

        checkTown(); // Check if Town - goes to Town scene at end of turn
        checkEvent(); // Check if Event - goes to Event immediately
        pickRoute(); // Check if passed pick route
        endShortcut(); // Check if passed end of shortcut - carries out logic only
        finishCheck(); // Check if at finish - goes to game over scene
    }

    public static void moveForward() throws IOException {
        currentSpace = playerSpace;
        movement = extraMovement;
        for (int i = 0; i < movement; i++) {
            playerSpace++;
            spacesTravelled++; // Stat tracking for spaces travelled
            System.out.println("\nPlayer moves forward 1 space onto space " + playerSpace);
            extraMovement--;
            spaceChecks();
            if (eventSpace || pickRouteSpace) {
                return;
            }
        }
        System.out.println("\nPlayer moved forward " + (playerSpace - currentSpace) + " space(s) onto space " + playerSpace);
    }

    public static void pickRoute() throws IOException {

        if (playerSpace-1 == pickRouteSpaces[0]) {
            // Pick Route 1
            pickRouteSpace = true;
            extraMovement += (currentSpace + movement) - playerSpace;
            setPickRouteOptions("Do you want to pass through Lonerock or take the shorter path? Pick the route you want to take:");
            App.setRoot("pickRoute");
        } else if (playerSpace-1 == pickRouteSpaces[1]) {
            // Pick Route 2
            pickRouteSpace = true;
            extraMovement = (currentSpace + movement) - playerSpace;
            setPickRouteOptions("Do you want to pass through Buckskin or take the shorter path? Pick the route you want to take:");
            App.setRoot("pickRoute");
        }
    }

    public static void checkRoute() {
        if (playerSpace-1 == pickRouteSpaces[0]) {
            if (!shortcutTaken) {
                playerSpace+=3;
                currentSpace+=3;
                System.out.println("Shortcut was not taken");
            } else {
                System.out.println("Shortcut was taken. Player space adjusted to space " + playerSpace);
            }
        } else if (playerSpace-1 == pickRouteSpaces[1]) {
            if (!shortcutTaken) {
                playerSpace+=5;
                currentSpace+=5;
                System.out.println("Shortcut was not taken");
            } else {
                System.out.println("Shortcut was taken. Player space adjusted to space " + playerSpace);
            }
        }
    }

    public static void endShortcut() {

        if (playerSpace-1 == endShortcutSpaces[0]) {
            // Move forward to end of route space
            playerSpace+=6;
            currentSpace+=6;
            shortcutTaken = false;
        } else if (playerSpace-1 == endShortcutSpaces[1]) {
            // Move forward to end of route space
            playerSpace += 9;
            currentSpace+=9;
            shortcutTaken = false;
        }
    }
}

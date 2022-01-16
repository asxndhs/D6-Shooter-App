package org.openjfx.Logic.TownMethods;

import static org.openjfx.Logic.TownMethods.PlayPoker.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.TownMethods.BuyItems.*;
import static org.openjfx.GameScreens.Town.*;

public class FindTown {

    // Town variables
    public static int[] townSpaces = new int[]{15,35,50,61,83,95};
    public static String[] townNames = new String[]{"Calico","Gold Hill","Lonerock","Thompson","Buckskin","Reno"};
    public static boolean townSpace = false;

    public static void checkTown() {

        for (int i = 0; i < townSpaces.length; i++) {
            if (playerSpace == townSpaces[i]) {
                System.out.println("\nPlayer has reached a Town.\nThere will be no more movement this turn.\nResolving remaining dice...");
                pokerPlayed = false; // Reset poker played status
                movement = 0; // Discard remaining 1s
                extraMovement = 0; // Stop any extra movement
                backroads = 0; // Discard remaining backroads 4s
                resetSpecialItem(); // Reset before determining
                pickSpecialItem(); // Determine special item to be made available
                townSpace = true; // Acknowledge town space for end of turn before resolving remaining dice
                setTownName(townNames[i]);
            }
        }
    }
}

package org.openjfx.Logic;

import static org.openjfx.GameScreens.PosseLoss.*;
import static org.openjfx.Logic.EventMethods.FullEvents.desertersEvent;
import static org.openjfx.Logic.RationsMethods.rationsDay;
import static org.openjfx.Logic.Scoring.posseMembersLost;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class Inventory {

    // Establish inventory variables and starting amounts
    public static int posse;
    public static int food;
    public static int ammo;
    public static int gold;

    // Establish inventory gained/lost variables
    public static int posseChange;
    public static int foodChange;
    public static int goldChange;

    // TODO: Currently player isn't being told when they meet a max value of food, ammo or posse - do they need to be told
    //  and if so, how should they be told

    public static void posseLossChecks() {
        stopPosseLoss = false;
        if (desertersEvent || medicineBandagesInventory) {
            if (!rationsDay || desertersEvent) {
                if (!posseItemsDisabled) {
                    setPosseLossMessage("You are about to lose " + posseChange + " Posse member(s). Do you want to use one of your items to reduce or negate this loss?");
                    stopPosseLoss = true;
                    return; // Will do the rest of the posse check at the end of the posseLoss scene
                }
            }
        }
        if (hunterInventory) {
            hunterPosseCheck(); // Check there are at least two posse for hunter to remain
        }
        if (posse < 1) {
            stopPosseLoss = true;
        }
    }

    public static void posseLossLoop() {
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
    }
}

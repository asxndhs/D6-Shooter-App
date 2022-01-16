package org.openjfx.Logic.EventMethods;

import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.GameScreens.EndEvents.setEndOfEventMessage;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.GameOverMethods.finalDayCheck;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Day.*;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.RationsMethods.checkRations;
import static org.openjfx.Logic.RationsMethods.rationsFromScene;
import static org.openjfx.Logic.RollMethods.Roll.*;
import static org.openjfx.Logic.RollMethods.RollActions.*;
import static org.openjfx.Logic.Scoring.posseMembersLost;
import static org.openjfx.Logic.Scoring.spacesTravelled;
import static org.openjfx.Logic.ShootoutMethods.*;
import static org.openjfx.Logic.TownMethods.FindTown.townSpace;
import static org.openjfx.Logic.TownMethods.FindTown.townSpaces;

public class FullEvents {

    public static boolean goldHillsEvent = false; // Boolean for There's Gold in them Hills Event
    public static boolean downhillEvent = false; // Boolean for Downhill Ride Event
    public static boolean lostGunsightEvent = false; // Boolean for Lost Gunsight Mine Event
    public static boolean crittersEvent = false; // Boolean for Critters Everywhere Event
    public static boolean prisonerEvent = false; // Boolean for Prisoner Event
    public static boolean prisonerRed6 = false; // Boolean for Prisoner Red 6
    public static boolean prisonerRedDie1Lock = false; // Boolean for Prisoner lock Red die 1
    public static boolean prisonerRedDie2Lock = false; // Boolean for Prisoner lock Red die 2
    public static boolean prisonerRedDie3Lock = false; // Boolean for Prisoner lock Red die 3
    public static boolean campfireEvent = false; // Boolean for Campfire Songs Event
    public static boolean familyEvent = false; // Boolean for Lost Family Event
    public static boolean desertersEvent = false; // Boolean for Army Deserters Event
    public static boolean deserter1 = false; // Boolean for Deserter 1
    public static boolean deserter2 = false; // Boolean for Deserter 2
    public static boolean deserterRepeatCheck = false; // Boolean to keep track of if player wants to sacrifice deserter
    public static boolean indianEvent = false; // Boolean for Indian Guide Event
    public static int eventDie1; // Dice for Downhill Ride and Critters Everywhere Event
    public static int eventDie2; // Dice for Critters Everywhere Event
    public static boolean eventDie1Lock = false; // Lock boolean for event dice
    public static boolean eventDie2Lock = false; // Lock boolean for event dice
    public static boolean banditsShootout = false; // Boolean for Bandits event shootout

    public static String event14Message;
    public static String event16Message;
    public static String event24Message;
    public static String event25Message;
    public static String event44Message;
    public static String event46Message;
    public static String event66Message;
    public static Boolean endEvent = false;
    public static String deserter1Status;
    public static String deserter2Status;

    public static void loseEventAbility() throws IOException {

        if (goldHillsEvent || downhillEvent || lostGunsightEvent || crittersEvent || campfireEvent || familyEvent || indianEvent || prisonerEvent || prisonerRed6) {
            if (goldHillsEvent) {
                goldHillsEvent = false;
                setEndOfEventMessage("There's Gold in them Hills: Event ability lost and Event finished");
            } else if (downhillEvent) {
                downhillEvent = false;
                setEndOfEventMessage("Downhill Ride: Event additional die lost and Event finished");
            } else if (lostGunsightEvent) {
                lostGunsightEvent = false;
                setEndOfEventMessage("Lost Gunsight Mine: Event is now finished. You may use Seek Shelter again");
            } else if (crittersEvent) {
                crittersEvent = false;
                setEndOfEventMessage("Critters Everywhere: Event additional dice lost and Event finished");
            } else if (campfireEvent) {
                campfireEvent = false;
                setEndOfEventMessage("Campfire Songs: Extra Fight die lost and Event finished");
            } else if (indianEvent) {
                indianEvent = false;
                setEndOfEventMessage("Indian Guide: Event ability lost and Event finished");
            }
            if (prisonerEvent || prisonerRed6) {
                // Only ends if reaching a Town, continues if at an event
                if (townSpace) {
                    // Resolve prisoner and set message
                    prisonerResult();
                } else {
                    return;
                }
            }
            endEvent = true;
            App.setRoot("endEvents");
        }
    }

    public static void billyEscapes() throws IOException {

        System.out.println("\nEvent 1-4: Billy Escapes");

        // Billy escapes while your Posse is resting. You are able to track him down, but it costs you some time. If the
        // nearest Town is behind you, add 1 Day. If the nearest Town is ahead of you, move ahead 2 spaces and add 1 Day. If distance is equal,
        // add 1 Day

        // Find closest Town space
        int distance = Math.abs(townSpaces[0] - playerSpace);
        int index = 0;
        for(int i = 1; i < townSpaces.length; i++){
            int newDistance = Math.abs(townSpaces[i] - playerSpace);
            if(newDistance < distance){
                index = i;
                distance = newDistance;
            }
        }
        int nearestTownSpace = townSpaces[index];
        System.out.println("Player is at space " + playerSpace + " and the nearest Town is at space " + nearestTownSpace);

        // Resolve effects of Event
        if (playerSpace < nearestTownSpace) {
            movement = 2;
            for (int i = 0; i < movement; i++) {
                playerSpace++;
                spacesTravelled++;
                spaceChecks();
            }
            System.out.println("The nearest Town is ahead of the player. Player moves forward " + movement + " spaces and loses one day");
            event14Message = "The nearest Town is ahead of you. Move forward " + movement + " spaces and lose one day.\nIt is now Day " + day + " and player is on space " + playerSpace;
        } else {
            System.out.println("The nearest Town is behind the player. Player does not move and loses one day");
            event14Message = "The nearest Town is behind the player. Player does not move and loses one day.\nIt is now Day " + day + " and player is on space " + playerSpace;
        }
        day++;
        finalDayCheck(); // Check it hasn't passed the end of day 40
        rationsFromScene = "events";
        checkRations(); // Rations check before moving onto next day
        System.out.println("It is now Day " + day + " and player is on space " + playerSpace);
        System.out.println("End of Event");
    }

    public static void theresGoldInThemHills() {

        System.out.println("\nEvent 1-6: There's Gold In Them Hills");

        goldHillsEvent = true; // Add icon to player's inventory

        System.out.println("Until you reach the next Town or Event, you may change any one die to a 3 on the first roll of each turn. (Red 5’s and 6’s may not be changed.)");
        event16Message = "There's Gold in them Hills ability added. You now have the ability to change any one die to " +
                "a 3 on the first roll of each turn (excluding Red 5's and 6's).";
        System.out.println("End of Event");
    }

    public static void slowTrainCrossing() {

        System.out.println("\nEvent 2-4: Slow Train Crossing");

        // No further movement can be made on this turn (with 1’s or 4’s).
        movement = 0;
        backroads = 0;
        event24Message = "All movement for the rest of this turn has been stopped. You are on space " + playerSpace;

        System.out.println("End of Event");
    }

    public static void downhillRide() {

        System.out.println("\nEvent 2-5: Downhill Ride");

        // You always have an extra 1 die available until you reach the next Town or Event.

        System.out.println("You always have an extra 1 die available until you reach the next Town or Event.");
        downhillEvent = true;
        event25Message = "Downhill Ride ability added. You will have an extra die to use during the roll phase";

        System.out.println("End of Event");
    }

    public static void lostGunsightMine() {

        System.out.println("\nEvent 2-6: Lost Gunsight Mine");

        // You have found the legendary Lost Gunsight Mine, but the heat of the valley is particularly brutal. You
        // may gain 5 Gold, but if so, then until you reach the next Town or Event, you may not use 4’s to Seek Shelter.

        lostGunsightEvent = true;
        System.out.println("Player gains 5 Gold but cannot use the Seek Shelter ability until the next Town/Event.\nPlayer now has " + gold + " Gold");

        System.out.println("End of Event");
    }

    public static void prisoner() {

        System.out.println("\nEvent 4-4: Prisoner");

        // The next time you roll a Red 6, put the die on this sheet to take it “prisoner”. If you lose a Shootout before you reach
        // the next Town, return the die and lose 1 Gold (if you have it). If you reach the next Town with the die still on this sheet, gain 3 Gold
        // and return the die. While on this sheet, the die does not take part in any Shootouts that occur.

        prisonerEvent = true;
        prisonerShootoutLoss = false;
        event44Message = "Prisoner event started. The next Red 6 you roll will be temporarily removed from the game " +
                "and returned at the next Town or Event. By then, if you have not lost a Shootout, you will gain 3 Gold. " +
                "Otherwise you will lose 1 Gold (if you have it)";

        System.out.println("End of Event");
    }

    public static void prisonerRed6Check() {

        // Check if the player rolled a red 6
        if (redDie1 == 6) {
            prisonerRed6 = true;
            redDie1 = 0;
            rollArray[5] = 0;
            prisonerRedDie1Lock = true; // Lock the red die from being rolled again
            prisonerEvent = false;
            prisonerShootoutLoss = false; // Reset shootout loss tracker
            System.out.println("Red Die 1 taken Prisoner");
        } else if (redDie2 == 6) {
            prisonerRed6 = true;
            redDie2 = 0;
            rollArray[6] = 0;
            prisonerRedDie2Lock = true; // Lock the red die from being rolled again
            prisonerEvent = false;
            prisonerShootoutLoss = false; // Reset shootout loss tracker
            System.out.println("Red Die 2 taken Prisoner");
        } else if (redDie3 == 6) {
            prisonerRed6 = true;
            redDie3 = 0;
            rollArray[7] = 0;
            prisonerRedDie3Lock = true; // Lock the red die from being rolled again
            prisonerEvent = false;
            prisonerShootoutLoss = false; // Reset shootout loss tracker
            System.out.println("Red Die 3 taken Prisoner");
        }
    }

    public static void prisonerResult() {

        // Only to be used at next Town (not Event)
        if (!prisonerRed6) {
            System.out.println("Player did not roll a Red 6 during the Event. Nothing happens");
            setEndOfEventMessage("Prisoner: You did not roll a Red 6 during the Event. The Event has ended");
        } else if (prisonerShootoutLoss) {
            System.out.println("Player lost a Shootout during the Event");
            prisonerShootoutLoss = false;
            if (gold > 0) {
                gold--;
                System.out.println("Player loses one Gold as a result. Player currently has " + gold + " Gold remaining");
                setEndOfEventMessage("Prisoner: You lost a Shootout during the Event and lose one Gold as a result. You now have " + gold + " Gold remaining");
            } else {
                System.out.println("Player currently has no Gold. Nothing happens");
                setEndOfEventMessage("Prisoner: You lost a Shootout during the Event but you do not have any Gold to lose. End of Event");
            }
        } else {
            System.out.println("Player did not lose a Shootout during the Event");
            gold+=3;
            System.out.println("Player gains three Gold as a result. Player currently has " + gold + " Gold remaining");
            setEndOfEventMessage("Prisoner: You did not lose a Shootout during the Event and gain three Gold as a result. You now have " + gold + " Gold");
        }

        // Reset event booleans
        prisonerEvent = false;
        prisonerRed6 = false;
        prisonerRedDie1Lock = false;
        prisonerRedDie2Lock = false;
        prisonerRedDie3Lock = false;
    }

    public static void campfireSongs() {

        System.out.println("\nEvent 4-5: Campfire Songs");

        // You may spend 1 Food to stop early for the night and make a campfire to share stories and sing songs with
        // your Posse, which would be good for their morale. If you do that, add an extra die to your Fight dice pools for all Shootouts you
        // are involved in until you reach the next Event or Town.

        campfireEvent = true;
        System.out.println("Extra Fight die available for every Shootout until the next Town/Event");

        System.out.println("End of Event");
    }

    public static void crittersEverywhere() {

        System.out.println("\nEvent 4-6: Critters Everywhere");

        // You always have an extra 2 die available until you reach the next Town or Event.

        System.out.println("You always have an extra 2 die available until you reach the next Town or Event.");
        crittersEvent = true;
        event46Message = "Critters Everywhere ability added. You will have two extra dice to use during the roll phase";

        System.out.println("End of Event");
    }

    public static void lostFamily() {

        System.out.println("\nEvent 5-5: Lost Family");

        // You come across a pioneer family that is lost. If you stop to help them, add 1 Day. They have nothing to offer in
        // return but their thanks and a blessing. On your next turn you may lock one White die and one Red die on any number, before rolling
        // any dice

        familyEvent = true;
        // Rations and final day check added into choose option code
        System.out.println("Player has chosen to help the family but has lost one day as a result. Ability will be available at the start of the next turn");

        System.out.println("End of Event");
    }

    public static void armyDeserters() {

        System.out.println("\nEvent 5-6: Army Deserters");

        // You come across two Army deserters. You may add them to your Posse. The two circles below represent them.
        // (Do not add to your Posse number on the playsheet.) You may cross out the circles to use them for Posse loss at any time. Before
        // each turn, roll a die for each intact circle on this sheet. If you roll a 5, cross out the circle. That person has deserted you. If you roll a
        // 6, cross out the circle and lose 1 Food and 1 Gold (or as much Food/Gold as you have).

        System.out.println("Army deserters have tagged along with the Posse");
        desertersEvent = true;
        deserter1 = true;
        deserter2 = true;

        System.out.println("End of Event");
    }

    public static void deserter1Sacrifice() {

        deserter1 = false;
        posse++;
        posseMembersLost--; // Stat tracking for posse loss during the game
        System.out.println("Deserter 1 has left the group. Player does not lose a Posse member");
        System.out.println("Player now has " + posse + " Posse remaining");

        // Check if both deserters have left yet
        if (!deserter1 && !deserter2) {
            desertersEvent = false;
        }
    }

    public static void deserter2Sacrifice() {

        deserter2 = false;
        posse++;
        posseMembersLost--; // Stat tracking for posse loss during the game
        System.out.println("Deserter 2 has left the group. Player does not lose a Posse member");
        System.out.println("Player now has " + posse + " Posse remaining");

        // Check if both deserters have left yet
        if (!deserter1 && !deserter2) {
            desertersEvent = false;
        }
    }

    public static void deserterRoll() {

        if (deserter1) {
            int deserterDie = (int) (Math.random()*6+1);
            if (deserterDie > 4) {
                deserter1 = false; // Lose deserter 1
                System.out.println("Player rolled a " + deserterDie + ". Deserter 1 has deserted the Posse");
                goldChange = 0;
                foodChange = 0;
                if (deserterDie == 6) {
                    if (gold > 0) {
                        gold--; // If the player has gold, lose one gold
                        goldChange++;
                        System.out.println("The deserter took one Gold with them. Player now has " + gold + " Gold remaining");
                    }
                    if (food > 0) {
                        food--; // If the player has food, lose one food
                        foodChange++;
                        System.out.println("The deserter took one Food with them. Player now has " + food + " Food remaining");
                    }
                    deserter1Status = "You rolled a " + deserterDie + ". The first deserter has left the Posse and took " + goldChange + " Gold and " + foodChange + " Food with them";
                } else {
                    deserter1Status = "You rolled a " + deserterDie + ". The first deserter has left the Posse";
                }
            } else {
                System.out.println("Player rolled a " + deserterDie + ". Deserter 1 remains with the Posse");
                deserter1Status = "You rolled a " + deserterDie + ". The first deserter is staying with the Posse";
            }
        }

        if (deserter2) {
            int deserterDie = (int) (Math.random()*6+1);
            if (deserterDie > 4) {
                deserter2 = false; // Lose deserter 2
                System.out.println("Player rolled a " + deserterDie + ". Deserter 2 has deserted the Posse");
                goldChange = 0;
                foodChange = 0;
                if (deserterDie == 6) {
                    if (gold > 0) {
                        gold--; // If the player has gold, lose one gold
                        goldChange++;
                        System.out.println("The deserter took one Gold with them. Player now has " + gold + " Gold remaining");
                    }
                    if (food > 0) {
                        food--; // If the player has food, lose one food
                        foodChange++;
                        System.out.println("The deserter took one Food with them. Player now has " + food + " Food remaining");
                    }
                    deserter2Status = "You rolled a " + deserterDie + ". The second deserter has left the Posse and took " + goldChange + " Gold and " + foodChange + " Food with them";
                } else {
                    deserter2Status = "You rolled a " + deserterDie + ". The second deserter has left the Posse";
                }
            } else {
                System.out.println("Player rolled a " + deserterDie + ". Deserter 2 remains with the Posse");
                deserter2Status = "You rolled a " + deserterDie + ". The second deserter is staying with the Posse";
            }
        }

        // If both deserters have left
        if (!deserter1 && !deserter2) {
            desertersEvent = false;
        }
    }

    public static void indianGuide() {

        System.out.println("\nEvent 6-6: Indian Guide");

        System.out.println("You come across an Indian who knows this area very well. He joins you for part of your journey, but he is never considered to be part of your Posse. Before each turn, take one Red die and lock it, with whatever number you choose. You may not unlock that die this turn. The Indian Guide moves on when you reach the next Event or Town.");

        // You come across an Indian who knows this area very well. He joins you for part of your journey, but he is never
        // considered to be part of your Posse. Before each turn, take one Red die and lock it, with whatever number you choose. You may
        // not unlock that die this turn. The Indian Guide moves on when you reach the next Event or Town.

        indianEvent = true;
        event66Message = "Indian Guide ability added. Before each turn, you will be able to lock one Red die at any value" +
                " for the rest of that turn";

        System.out.println("End of Event");
    }
}

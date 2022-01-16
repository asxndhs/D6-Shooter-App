package org.openjfx.Logic.EventMethods;

import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.GameScreens.Events.eventProgress;
import static org.openjfx.Logic.Map.*;
import static org.openjfx.Logic.Scoring.eventsStarted;

public class EventCall {

    // Event variables
    public static int[] eventSpaces = new int[]{10, 20, 30, 40, 56, 66, 76, 86, 92};
    public static boolean eventTypeFull = true;
    public static boolean eventSpace = false;
    public static int simpleEventDie;
    public static int fullEventNumber;

    public static void checkEvent() throws IOException {

        for (int i = 0; i < eventSpaces.length; i++) {
            if (playerSpace == eventSpaces[i]) {
                eventSpace = true;
                extraMovement = (currentSpace + movement) - playerSpace;
                movement = 0;
                eventProgress = 1;
                System.out.println("\nPlayer has reached an Event on space " + eventSpaces[i]);
                eventsStarted++;
                App.setRoot("events");
                return;
            }
        }
    }

    public static void eventType() {

        if (eventTypeFull) {
            // Roll dice, and order and concatenate values to determine Event
            int fullEventsDie1 = (int)(Math.random()*6+1);
            int fullEventsDie2 = (int)(Math.random()*6+1);
            if (fullEventsDie1 >= fullEventsDie2) {
                fullEventNumber = Integer.parseInt((fullEventsDie2) + String.valueOf(fullEventsDie1));
            } else {
                fullEventNumber = Integer.parseInt((fullEventsDie1) + String.valueOf(fullEventsDie2));
            }
            System.out.println("Picking Event " + fullEventNumber);
        } else {
            simpleEventDie = (int)(Math.random()*6+1); // Roll die to determine Event
        }
    }
}

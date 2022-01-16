package org.openjfx.Logic.RollMethods;

import static org.openjfx.Logic.EventMethods.FullEvents.eventDie1Lock;
import static org.openjfx.Logic.EventMethods.FullEvents.eventDie2Lock;
import static org.openjfx.Logic.RollMethods.Roll.*;

public class Locking {

    // Insert methods that toggle boolean for locking and unlocking
    public static void lockUnlockWhiteDie1() {

        whiteDie1Lock = !whiteDie1Lock;
        System.out.println("White Die 1 Lock: " + whiteDie1Lock);
    }

    public static void lockUnlockWhiteDie2() {

        whiteDie2Lock = !whiteDie2Lock;
        System.out.println("White Die 2 Lock: " + whiteDie2Lock);
    }

    public static void lockUnlockWhiteDie3() {

        whiteDie3Lock = !whiteDie3Lock;
        System.out.println("White Die 3 Lock: " + whiteDie3Lock);
    }

    public static void lockUnlockWhiteDie4() {

        whiteDie4Lock = !whiteDie4Lock;
        System.out.println("White Die 4 Lock: " + whiteDie4Lock);
    }

    public static void lockUnlockWhiteDie5() {

        whiteDie5Lock = !whiteDie5Lock;
        System.out.println("White Die 5 Lock: " + whiteDie5Lock);
    }

    public static void lockUnlockRedDie1() {

        if (redDie1ForceLock) {
            System.out.println("Red Die 1 cannot be unlocked");
        } else {
            redDie1Lock = !redDie1Lock;
        }
        System.out.println("Red Die 1 Lock: " + redDie1Lock);
    }

    public static void lockUnlockRedDie2() {

        if (redDie2ForceLock) {
            System.out.println("Red Die 2 cannot be unlocked");
        } else {
            redDie2Lock = !redDie2Lock;
        }
        System.out.println("Red Die 2 Lock: " + redDie2Lock);
    }

    public static void lockUnlockRedDie3() {

        if (redDie3ForceLock) {
            System.out.println("Red Die 3 cannot be unlocked");
        } else {
            redDie3Lock = !redDie3Lock;
        }
        System.out.println("Red Die 3 Lock: " + redDie3Lock);
    }

    public static void lockUnlockEventDie1() {

        eventDie1Lock = !eventDie1Lock;
        System.out.println("Event Die 1 Lock: " + eventDie1Lock);
    }

    public static void lockUnlockEventDie2() {

        eventDie2Lock = !eventDie2Lock;
        System.out.println("Event Die 2 Lock: " + eventDie2Lock);
    }
}

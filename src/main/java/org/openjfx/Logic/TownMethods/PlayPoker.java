package org.openjfx.Logic.TownMethods;

import java.util.Arrays;

import static org.openjfx.GameScreens.Poker.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Scoring.*;

public class PlayPoker {

    // Poker variables
    public static boolean pokerPlayed = false;
    static int handToBeatDie;
    public static String opponentHand;
    static String[] pokerHands = new String[]{"High Card (1 3 4 5 6)", "Pair (4 4 6 3 2)", "Full House (4 4 4 2 2)",
            "Straight (1 2 3 4 5)", "Four of a Kind (3 3 3 3 6)", "Five of a Kind (4 4 4 4 4)"};

    public static int pokerCard1;
    public static int pokerCard2;
    public static int pokerCard3;
    public static int pokerCard4;
    public static int pokerCard5;
    public static boolean pokerCard1Lock = false;
    public static boolean pokerCard2Lock = false;
    public static boolean pokerCard3Lock = false;
    public static boolean pokerCard4Lock = false;
    public static boolean pokerCard5Lock = false;

    static int[] cardCombination = new int[5];
    static int playerPokerScore;
    static int opponentPokerScore;
    public static String playerHandCombination;

    public static int ammoWager;
    public static int foodWager;
    public static int goldWager;

    public static Boolean pokerWin = false;
    public static Boolean pokerTie = false;
    public static Boolean pokerLoss = false;

    // POKER HAND RANKINGS:
    // High Card - 1
    // Pair - 2
    // Two Pair - 3
    // Three of a Kind - 4
    // Full House - 5
    // Straight - 6
    // Four of a Kind - 7
    // Five of a Kind - 8

    public static void resetPoker() {

        // Reset poker variables
        pokerCard1 = 0;
        pokerCard2 = 0;
        pokerCard3 = 0;
        pokerCard4 = 0;
        pokerCard5 = 0;
        pokerCard1Lock = false;
        pokerCard2Lock = false;
        pokerCard3Lock = false;
        pokerCard4Lock = false;
        pokerCard5Lock = false;
        pokerWin = false;
        pokerTie = false;
        pokerLoss = false;
        ammoWager = 0;
        foodWager = 0;
        goldWager = 0;
        pokerRollCounter = 1;
        wagerSet = false;
        opponentHandRevealed = false;
        pokerResultDone = false;
    }

    public static void playerHand() {

        // Roll for the player's poker hand
        if (!pokerCard1Lock) {
            cardCombination[0] = pokerCard1 = (int) (Math.random() * 6 + 1);
        }
        System.out.println("First poker card: " + pokerCard1);
        if (!pokerCard2Lock) {
            cardCombination[1] = pokerCard2 = (int) (Math.random() * 6 + 1);
        }
        System.out.println("Second poker card: " + pokerCard2);
        if (!pokerCard3Lock) {
            cardCombination[2] = pokerCard3 = (int) (Math.random() * 6 + 1);
        }
        System.out.println("Third poker card: " + pokerCard3);
        if (!pokerCard4Lock) {
            cardCombination[3] = pokerCard4 = (int) (Math.random() * 6 + 1);
        }
        System.out.println("Fourth poker card: " + pokerCard4);
        if (!pokerCard5Lock) {
            cardCombination[4] = pokerCard5 = (int) (Math.random() * 6 + 1);
        }
        System.out.println("Fifth poker card: " + pokerCard5);

        // Final hand
        System.out.println("Player's hand is " + Arrays.toString(cardCombination));
    }

    public static void handToBeat() {

        // Roll for the poker hand to beat
        handToBeatDie = (int)(Math.random()*6+1);
        opponentHand = pokerHands[handToBeatDie-1];
        System.out.println("The hand to beat is " + opponentHand);

        // Determine opponent's poker hand score
        if (handToBeatDie == 1) {
            opponentPokerScore = 1; // High card
        } else if (handToBeatDie == 2) {
            opponentPokerScore = 2; // Pair
        } else if (handToBeatDie == 3) {
            opponentPokerScore = 5; // Full house
        } else if (handToBeatDie == 4) {
            opponentPokerScore = 6; // Straight
        } else if (handToBeatDie == 5) {
            opponentPokerScore = 7; // Four of a kind
        } else if (handToBeatDie == 6) {
            opponentPokerScore = 8; // Five of a kind
        } else {
            System.out.println("Error rolling for hand to beat. Please check error");
        }
    }

    public static void pokerHandsLogic() {

        // Create variables
        boolean duplicateCard1 = false;
        boolean duplicateCard2 = false;
        boolean straight;
        int duplicateValue1 = 0;
        int duplicateIndex = 0;

        // order array from low to high
        Arrays.sort(cardCombination);

        // Check if there is any duplicate card
        for (int i = 0; i < cardCombination.length-1; i++) {
            if (cardCombination[i] == cardCombination[i+1]) {
                duplicateCard1 = true;
                duplicateValue1 = cardCombination[i];
                duplicateIndex = i+1;
                break; // End loop at first duplicate
            }
        }

        // If no duplicates search for a Straight
        if (!duplicateCard1) {
            // Code to check for a straight
            straight = true; // Assume straight and then check if it doesn't meet criteria
            for (int i = 0; i < cardCombination.length-1; i++) {
                if (cardCombination[i] != (cardCombination[i + 1] - 1)) {
                    straight = false;
                    break;
                }
            }
            if (straight) {
                playerPokerScore = 6; // Straight
            } else {
                playerPokerScore = 1; // High Card
            }
        } else {
            // Code to check other combinations
            // If next value is the same as previous, three of a kind or more. If not, its one or two pair
            if (duplicateIndex < 4 && duplicateValue1 == cardCombination[duplicateIndex+1]) {
                if (duplicateIndex < 3 && duplicateValue1 == cardCombination[duplicateIndex+2]) {
                    if (duplicateIndex < 2 && duplicateValue1 == cardCombination[duplicateIndex+3]) {
                        playerPokerScore = 8; // Five of a Kind
                    } else {
                        playerPokerScore = 7; // Four of a Kind
                    }
                } else if (duplicateIndex < 2 && cardCombination[duplicateIndex+2] == cardCombination[duplicateIndex+3]) {
                    playerPokerScore = 5; // Full House
                } else {
                    playerPokerScore = 4; // Three of a Kind
                }
            } else if (duplicateIndex < 3) {
                for (int i = duplicateIndex+1; i < cardCombination.length-1; i++) {
                    if (cardCombination[i] == cardCombination[i+1]) {
                        // Two pair or full house
                        duplicateCard2 = true;
                        duplicateIndex = i+1;
                        break; // End loop at next duplicate
                    }
                }
                if (duplicateCard2) {
                    // Two pair or full house
                    if (duplicateIndex < 4 && cardCombination[duplicateIndex] == cardCombination[duplicateIndex+1]) {
                        playerPokerScore = 5; // Full House
                    } else {
                        playerPokerScore = 3; // Two Pair
                    }
                } else {
                    playerPokerScore = 2; // Pair
                }
            } else {
                playerPokerScore = 2; // Pair
            }
        }

        // Assign score to hand name
        if (playerPokerScore == 1) {
            playerHandCombination = "High Card";
        } else if (playerPokerScore == 2) {
            playerHandCombination = "a Pair";
        } else if (playerPokerScore == 3) {
            playerHandCombination = "Two Pair";
        } else if (playerPokerScore == 4) {
            playerHandCombination = "Three of a Kind";
        } else if (playerPokerScore == 5) {
            playerHandCombination = "a Full House";
        } else if (playerPokerScore == 6) {
            playerHandCombination = "a Straight";
        } else if (playerPokerScore == 7) {
            playerHandCombination = "Four of a Kind";
        } else if (playerPokerScore == 8) {
            playerHandCombination = "Five of a Kind";
        } else {
            System.out.println("Error assigning player score to valid card combination. Please check error");
        }

        System.out.println("Player's hand is " + playerHandCombination);

        // Best hand stat tracking
        if (playerPokerScore > bestPokerHand[0]) {
            bestPokerHand[0] = playerPokerScore;
            System.out.println("New best poker hand!");
            for (int i = 0; i < 5; i++) {
                bestPokerHand[i+1] = cardCombination[i];
            }
        }
    }

    public static void pokerResult() {

        // Check which hand won and resolve wager accordingly
        if (playerPokerScore > opponentPokerScore) {
            System.out.println("Player wins!");
            pokerWin = true;
            pokerWins++;
            // Set max ammo
            if ((2*ammoWager)+ammo > 5) {
                ammo = 5;
                System.out.println("Player has reached max Ammo");
            } else {
                ammo += 2 * ammoWager;
            }
            // Set max food
            if ((2*foodWager)+food > 12) {
                food = 12;
                System.out.println("Player has reached max Food");
            } else {
                food += 2 * foodWager;
            }
            gold+= 2*goldWager;

            // Track wager winning
            wagerWinnings[0] += ammoWager;
            wagerWinnings[1] += foodWager;
            wagerWinnings[2] += goldWager;
        } else if (playerPokerScore == opponentPokerScore) {
            System.out.println("Poker game ended in a tie! Wager is returned to player");
            pokerTie = true;
            pokerTies++;
            ammo+= ammoWager;
            food+= foodWager;
            gold+= goldWager;
        } else {
            System.out.println("Player has lost to opponent.");
            pokerLoss = true;
            pokerLosses++;

            // Track wager losses
            wagerLosses[0] += ammoWager;
            wagerLosses[1] += foodWager;
            wagerLosses[2] += goldWager;
        }
    }
}

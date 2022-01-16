package org.openjfx.Logic.TownMethods;

import static org.openjfx.GameScreens.Shop.*;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;

public class BuyItems {

    public static String specialItemName;
    public static int specialItemCost;
    static int specialItemDie;
    public static Boolean specialItemBought = false;
    public static Boolean specialItemsAllPurchased = false;

    public static void resetSpecialItem() {

        compassShop = false;
        hunterShop = false;
        prospectorsMapShop = false;
        binocularsShop = false;
        medicineBandagesShop = false;
        betterGunsShop = false;
        specialItemBought = false;
    }


    public static void pickSpecialItem() {

        specialItemsAllPurchased = false; // Reset

        // If the player already has all special items
        if (compassInventory && hunterInventory && prospectorsMapInventory && binocularsInventory &&
                medicineBandagesInventory && betterGunsInventory) {
            specialItemsAllPurchased = true;
            System.out.println("Player already has all special items. Special item will not be available in the shop");
            return;
        }

        // Pick special item randomly, re-roll if item is already in player's inventory
        specialItemDie = (int)(Math.random()*6+1); // Determine special item available in shop - will not change until next Town
        if (specialItemDie == 1) {
            if (compassInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                compassShop = true;
                specialItemCost = 2;
                specialItemName = "Compass";
                setShowSpecialItem("Compass (2) Red 1's count as 2 moves");
            }
        } else if (specialItemDie == 2) {
            if (hunterInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                hunterShop = true;
                specialItemCost = 3;
                specialItemName = "Hunter";
                setShowSpecialItem("Hunter (3) Counts as a Posse member; when Finding Food/Water, any Red 2's count as 2 dice" +
                        " (Hunter is lost if Posse is ever reduced to 1)");
            }
        } else if (specialItemDie == 3) {
            if (prospectorsMapInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                prospectorsMapShop = true;
                specialItemCost = 3;
                specialItemName = "Prospector's Map";
                setShowSpecialItem("Prospector's Map (3) Gain one Gold for every two 3's rolled");
            }
        } else if (specialItemDie == 4) {
            if (binocularsInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                binocularsShop = true;
                specialItemCost = 2;
                specialItemName = "Binoculars";
                setShowSpecialItem("Binoculars (2) May re-roll one Red 5 rolled from the first roll");
            }
        } else if (specialItemDie == 5) {
            if (medicineBandagesInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                medicineBandagesShop = true;
                specialItemCost = 2;
                specialItemName = "Medicine/Bandages";
                setShowSpecialItem("Medicine/Bandages (2) Prevent all Posse loss from a single source, then discard");
            }
        } else if (specialItemDie == 6) {
            if (betterGunsInventory) {
                pickSpecialItem(); // Re-roll
            } else {
                betterGunsShop = true;
                specialItemCost = 1;
                specialItemName = "Better Guns";
                setShowSpecialItem("Better Guns (*) Add +* to your final total during Shootouts (* = the Gold you spent)");
            }
        } else {
            System.out.println("Error selecting special item. Please check error");
        }
    }

    public static void purchaseSpecialItem() {

        // Method to purchase special item depending on which one was randomly selected to appear in the shop
        if (compassShop && !compassInventory) {
            compassInventory = true;
            gold-= specialItemCost;
            specialItemBought = true;
            System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
        } else if (hunterShop) {
            // Player can only buy hunter if posse < 12
            if (posse < 12) {
                hunterInventory = true;
                posse++;
                gold -= specialItemCost;
                specialItemBought = true;
                System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
                System.out.println("Player now has " + posse + " Posse");
            } else {
                System.out.println("Player has maximum number of Posse members. There is no space for Hunter to join the Posse");
            }
        } else if (prospectorsMapShop && !prospectorsMapInventory) {
            prospectorsMapInventory = true;
            gold-= specialItemCost;
            specialItemBought = true;
            System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
        } else if (binocularsShop && !binocularsInventory) {
            binocularsInventory = true;
            gold-= specialItemCost;
            specialItemBought = true;
            System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
        } else if (medicineBandagesShop && !medicineBandagesInventory) {
            medicineBandagesInventory = true;
            gold-= specialItemCost;
            specialItemBought = true;
            System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
        } else {
            System.out.println("Error in purchasing Special Item. Please check error");
        }
    }
}

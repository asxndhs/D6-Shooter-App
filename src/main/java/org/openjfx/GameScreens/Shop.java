package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Scoring.*;
import static org.openjfx.Logic.TownMethods.BuyItems.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.*;
import static org.openjfx.Logic.TownMethods.SpecialItems.betterGunsModifier;

public class Shop implements Initializable {

    public Label shopMessage;
    public Button buySpecialItemButton;
    public Label specialItemMessage;
    public VBox popUpBox;
    public Slider betterGunsSlider;
    public VBox specialItemsBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (specialItemsAllPurchased) {
            specialItemsBox.setVisible(false); // No special item is available to buy
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "shop";
        App.setRoot("pause");
    }

    @FXML
    private void returnToTown() throws IOException {
        App.setRoot("town");
    }

    // TODO: Grey out buy buttons if option is not available to player

    @FXML
    private void buy2Food() {
        if (gold > 0) {
            if (food < 11) {
                gold--;
                goldSpent++; // Gold spent stat tracking
                food+=2;
                System.out.println("Player has bought two Food for one Gold");
                shopMessage.setText("You bought two Food for one Gold. You now have " + gold + " Gold and " + food + " Food");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void buy5Food() {
        if (gold > 1) {
            if (food < 8) {
                gold-=2;
                goldSpent+=2; // Gold spent stat tracking
                food+=5;
                System.out.println("Player has bought five Food for two Gold");
                shopMessage.setText("You bought five Food for two Gold. You now have " + gold + " Gold and " + food + " Food");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void buyPosse() {
        if (gold > 0) {
            if (posse < 12 && posseChange < 3) {
                gold--;
                goldSpent++; // Gold spent stat tracking
                posse++;
                posseChange++;
                System.out.println("Player has bought one Posse member for one Gold");
                shopMessage.setText("You bought one Posse member for one Gold. You now have " + gold + " Gold and " + posse + " Posse members");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void buy2Ammo() {
        if (gold > 0) {
            if (ammo < 4) {
                gold--;
                goldSpent++; // Gold spent stat tracking
                ammo+=2;
                System.out.println("Player has bought two Ammo for one Gold");
                shopMessage.setText("You bought two Ammo for one Gold. You now have " + gold + " Gold and " + ammo + " Ammo");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void buy5Ammo() {
        if (gold > 1) {
            if (ammo < 1) {
                gold-=2;
                goldSpent+=2; // Gold spent stat tracking
                ammo+=5;
                System.out.println("Player has bought five Ammo for two Gold");
                shopMessage.setText("You bought five Ammo for two Gold. You now have " + gold + " Gold and " + ammo + " Ammo");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void buySpecialItem() {
        if (specialItemBought) {
            shopMessage.setText(specialItemName + " already purchased");
        } else if (specialItemCost > gold) {
            shopMessage.setText("You do not have enough Gold to buy " + specialItemName);
        } else if (betterGunsShop) {
            popUpBox.setVisible(true);
            // Set slider values depending on Gold available
            if (gold == 2) {
                betterGunsSlider.setMax(2);
            } else if (gold == 1) {
                betterGunsSlider.setMax(1);
                betterGunsSlider.setValue(1);
                // TODO: Doesn't look great with an empty slider, maybe switch for radio buttons?
            }
        } else if (hunterShop && posse > 11) {
            shopMessage.setText(specialItemName + " cannot be bought as you have too many Posse members");
        } else {
            purchaseSpecialItem();
            specialItemsBought++; // Special item bought stat tracking
            goldSpent+= specialItemCost; // Gold spent stat tracking
            shopMessage.setText(specialItemName + " bought for " + specialItemCost + " Gold. You now have " + gold + " Gold");
        }
    }

    @FXML
    private void buyBetterGuns() {
        specialItemCost = (int)betterGunsSlider.getValue();
        gold-= specialItemCost;
        goldSpent+= specialItemCost; // Gold spent stat tracking
        betterGunsModifier = specialItemCost; // Set strength of better guns based on cost
        betterGunsInventory = true;
        System.out.println("Player has purchased " + specialItemName + " for " + specialItemCost + " Gold");
        shopMessage.setText(specialItemName + " bought for " + specialItemCost + " Gold. You now have " + gold + " Gold");
        specialItemBought = true;
        specialItemsBought++; // Special item bought stat tracking
        popUpBox.setVisible(false);
    }

    @FXML
    private void closePopUp() {
        popUpBox.setVisible(false);
    }

    private void buyMessage1() {
        shopMessage.setText("You do not have enough Gold to buy this item");
        // TODO: Add in flashing/red text to get player's attention
        // TODO: Find way to add pause before reverting message without Java pausing/sleeping
    }

    private void buyMessage2() {
        shopMessage.setText("You cannot buy any more of this item");
        // TODO: Add in flashing/red text to get player's attention
    }

    public static StringProperty showSpecialItem = new SimpleStringProperty("Special Item details");

    public String getShowSpecialItem() {
        return showSpecialItem.get();
    }

    public StringProperty showSpecialItemProperty() {
        return showSpecialItem;
    }

    public static void setShowSpecialItem(String showSpecialItem) {
        Shop.showSpecialItem.set(showSpecialItem);
    }
}
package org.openjfx.GameScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Events.specialEvent;
import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.Inventory.*;
import static org.openjfx.Logic.Map.*;

public class WagonTrain implements Initializable {

    public Label wagonTrainMessage;
    public VBox movementPopUpBox;
    public Label movementMessage;

    public static int wagonTrainSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (wagonTrainSceneProgress == 1) {
            movementPopUpBox.setVisible(true);
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "wagonTrain";
        App.setRoot("pause");
    }

    @FXML
    private void returnToScene() throws IOException {
        eventSpace = false;
        if (extraMovement > 0) {
            wagonTrainSceneProgress = 1;
            moveForward();
            movementPopUpBox.setVisible(true);
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
        } else {
            // If event is over, button will move to next scene
            wagonTrainSceneProgress = 0;
            if (specialEvent) {
                specialEvent = false;
                setPlayerSpaceLabel("Player space: " + playerSpace);
                App.setRoot("game");
            } else {
                App.setRoot("resolveDice");
            }
        }
    }

    // TODO: Grey out buy buttons if option is not available to player

    @FXML
    private void buy2Food() {
        if (gold > 0) {
            if (food < 11) {
                gold--;
                food+=2;
                System.out.println("Player has bought two Food for one Gold");
                wagonTrainMessage.setText("You bought two Food for one Gold. You now have " + gold + " Gold and " + food + " Food");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void sell2Food() {
        if (food > 1) {
            gold++;
            food-=2;
            System.out.println("Player has sold two Food for one Gold");
            wagonTrainMessage.setText("You sold two Food for one Gold. You now have " + gold + " Gold and " + food + " Food");
        } else {
            buyMessage3();
        }
    }

    @FXML
    private void buy2Ammo() {
        if (gold > 0) {
            if (ammo < 4) {
                gold--;
                ammo+=2;
                System.out.println("Player has bought two Ammo for one Gold");
                wagonTrainMessage.setText("You bought two Ammo for one Gold. You now have " + gold + " Gold and " + ammo + " Ammo");
            } else {
                buyMessage2();
            }
        } else {
            buyMessage1();
        }
    }

    @FXML
    private void sell2Ammo() {
        if (ammo > 1) {
            gold++;
            ammo-=2;
            System.out.println("Player has sold two Ammo for one Gold");
            wagonTrainMessage.setText("You sold two Ammo for one Gold. You now have " + gold + " Gold and " + ammo + " Ammo");
        } else {
            buyMessage3();
        }
    }

    private void buyMessage1() {
        wagonTrainMessage.setText("You do not have enough Gold to buy this item");
        // TODO: Add in flashing/red text to get player's attention
        // TODO: Find way to add pause before reverting message without Java pausing/sleeping
    }

    private void buyMessage2() {
        wagonTrainMessage.setText("You cannot buy any more of this item");
        // TODO: Add in flashing/red text to get player's attention
    }

    private void buyMessage3() {
        wagonTrainMessage.setText("You cannot sell any more of this item");
        // TODO: Add in flashing/red text to get player's attention
    }
}
package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Game.setPlayerSpaceLabel;
import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.Map.*;

public class PickRoute implements Initializable {

    public static String pickRouteFromScene;

    public RadioButton townRadioButton;
    public RadioButton shortcutRadioButton;
    public Label routeChosenMessage;
    public VBox popUpBox;
    public VBox movementPopUpBox;
    public Label movementMessage;

    public static int pickRouteSceneProgress = 0;
    public static String routeMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radioButton();
        if (pickRouteSceneProgress == 1) {
            routeChosenMessage.setText(routeMessage);
            popUpBox.setVisible(true);
        } else if (pickRouteSceneProgress == 2) {
            movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            movementPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "pickRoute";
        App.setRoot("pause");
    }

    @FXML
    private void switchToNextPhase() throws IOException {
        if (townRadioButton.isSelected()) {
            pickRouteSceneProgress = 1;
            shortcutTaken = false;
            routeMessage = "You have decided to take the trip through the Town";
            routeChosenMessage.setText(routeMessage);
            System.out.println("You have decided to take the trip through the Town");
        } else if (shortcutRadioButton.isSelected()) {
            pickRouteSceneProgress = 1;
            shortcutTaken = true;
            routeMessage = "You have decided to take the shortcut and skip the Town";
            routeChosenMessage.setText(routeMessage);
            System.out.println("You have decided to take the shortcut and skip the Town");
        } else {
            return;
        }

        if (!popUpBox.isVisible()) {
            popUpBox.setVisible(true);
        } else {
            checkRoute();
            pickRouteSpace = false;
            if (extraMovement > 0) {
                pickRouteSceneProgress = 2;
                moveForward();
                movementPopUpBox.setVisible(true);
                movementMessage.setText("Moving additional spaces onto space " + playerSpace);
            } else {
                setPlayerSpaceLabel("Player space: " + playerSpace);
                pickRouteSceneProgress = 0;
                App.setRoot(pickRouteFromScene);
            }
        }
    }

    @FXML
    private void radioButton() {
        ToggleGroup group = new ToggleGroup();
        townRadioButton.setToggleGroup(group);
        shortcutRadioButton.setToggleGroup(group);
    }

    public static StringProperty pickRouteOptions = new SimpleStringProperty();

    public String getPickRouteOptions() {
        return pickRouteOptions.get();
    }

    public static StringProperty pickRouteOptionsProperty() {
        return pickRouteOptions;
    }

    public static void setPickRouteOptions(String pickRouteOptions) {
        PickRoute.pickRouteOptions.set(pickRouteOptions);
    }
}
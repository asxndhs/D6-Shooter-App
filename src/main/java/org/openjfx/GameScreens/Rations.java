package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.GameScreens.PosseLoss.stopPosseLoss;
import static org.openjfx.Logic.GameOverMethods.rationsFoodCheck;
import static org.openjfx.Logic.RationsMethods.*;

public class Rations implements Initializable {

    public VBox handOutRationsBox;
    public Button continueButton;

    public static int rationsSceneProgress = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (rationsSceneProgress == 1) {
            continueButton.setText("Continue");
            handOutRationsBox.setVisible(true);
        }
    }

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "rations";
        App.setRoot("pause");
    }

    @FXML
    private void backToGame() throws IOException {
        rationsSceneProgress = 1;
        rationsFoodCheck();
        if (stopPosseLoss) {
            stopPosseLoss = false;
            App.setRoot("posseLoss");
            // Doesn't need a posse check as Posse has to be > 0
        } else if (!handOutRationsBox.isVisible()) {
            continueButton.setText("Continue");
            handOutRationsBox.setVisible(true);
        } else {
            rationsDay = false;
            rationsSceneProgress = 0;
            App.setRoot(rationsFromScene);
        }
    }
    public static StringProperty rationsRequirements = new SimpleStringProperty();

    public String getRationsRequirements() {
        return rationsRequirements.get();
    }

    public static StringProperty rationsRequirementsProperty() {
        return rationsRequirements;
    }

    public static void setRationsRequirements(String rationsRequirements) {
        Rations.rationsRequirements.set(rationsRequirements);
    }

    public static StringProperty rationsResult = new SimpleStringProperty();

    public String getRationsResult() {
        return rationsResult.get();
    }

    public static StringProperty rationsResultProperty() {
        return rationsResult;
    }

    public static void setRationsResult(String rationsResult) {
        Rations.rationsResult.set(rationsResult);
    }
}
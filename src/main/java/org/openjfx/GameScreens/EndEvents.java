package org.openjfx.GameScreens;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.openjfx.App;

import java.io.IOException;

import static org.openjfx.GameScreens.Pause.fromScene;
import static org.openjfx.Logic.EventMethods.EventCall.eventSpace;
import static org.openjfx.Logic.TownMethods.FindTown.townSpace;

public class EndEvents {

    @FXML
    private void switchToPause() throws IOException {
        fromScene = "endEvents";
        App.setRoot("pause");
    }

    @FXML
    private void continueToNextScene() throws IOException {
        // Only pops up before a town or event so determine which scene should be changed to next
        if (eventSpace) {
            App.setRoot("events");
        } else if (townSpace) {
            App.setRoot("town");
        }
    }

    public Label eventMessage;
    public static StringProperty endOfEventMessage = new SimpleStringProperty("Event has ended");

    public String getEndOfEventMessage() {
        return endOfEventMessage.get();
    }

    public StringProperty endOfEventMessageProperty() {
        return endOfEventMessage;
    }

    public static void setEndOfEventMessage(String endOfEventMessage) {
        EndEvents.endOfEventMessage.set(endOfEventMessage);
    }
}
package org.openjfx.SettingsScreens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import org.openjfx.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Audio implements Initializable {

    public static Boolean audioFromPauseMenu = false;

    public RadioButton musicOnRadioButton;
    public RadioButton musicOffRadioButton;
    public RadioButton soundEffectsOnRadioButton;
    public RadioButton soundEffectsOffRadioButton;
    public Slider masterVolumeSlider;
    public Label masterVolumeSliderLabel;

    // TODO: Add accessibility from pause menu
    // TODO: Add in functionality of audio settings

    /**
     * Toggle option for music
     * Toggle option for sound effects
     * Slider for master volume
     * Confirm button to save settings and return to main settings screen
     * Cancel button to discard changes and return to main settings screen
     * */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Music
        ToggleGroup music = new ToggleGroup();
        musicOnRadioButton.setToggleGroup(music);
        musicOffRadioButton.setToggleGroup(music);
        // if () long game
        musicOnRadioButton.setSelected(true);

        // Sound effects
        ToggleGroup soundEffects = new ToggleGroup();
        soundEffectsOnRadioButton.setToggleGroup(soundEffects);
        soundEffectsOffRadioButton.setToggleGroup(soundEffects);
        // if () long game
        soundEffectsOnRadioButton.setSelected(true);

        // Master volume
        masterVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue, Number newValue) {
                masterVolumeSliderLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
            }
        });
    }

    @FXML
    private void switchBack() throws IOException {
        if (audioFromPauseMenu) {
            audioFromPauseMenu = false;
            App.setRoot("pause");
        } else {
            App.setRoot("settings");
        }
    }

    @FXML
    private void saveAndClose() throws IOException {
        // Save audio settings
        App.setRoot("settings");
    }

    @FXML
    private void resetToDefaults() {
        // Reset options to defaults
        musicOnRadioButton.setSelected(true);
        soundEffectsOnRadioButton.setSelected(true);
        masterVolumeSlider.setValue(100);
        masterVolumeSliderLabel.setText("100");
    }
}
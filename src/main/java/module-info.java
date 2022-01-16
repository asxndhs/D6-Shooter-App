module org.openjfx {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens org.openjfx.MainMenuScreens to javafx.fxml;
    opens org.openjfx.GameScreens to javafx.fxml;
    opens org.openjfx.HighScoreScreens to javafx.fxml;
    opens org.openjfx.SettingsScreens to javafx.fxml;
    exports org.openjfx;
}
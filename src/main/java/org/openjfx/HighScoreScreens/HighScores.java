package org.openjfx.HighScoreScreens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.openjfx.App;
import org.openjfx.ScoresLeaderboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.openjfx.Logic.Reset.resetScoresStats;
import static org.openjfx.Logic.Scoring.leaderboard;
import static org.openjfx.Logic.Scoring.updateLeaderboard;

public class HighScores implements Initializable {

    public static Boolean gameFinishedLeaderboard = false;

    // Leaderboard FXML variables
    public TableView<ScoresLeaderboard> highScoreLeaderboard;
    public TableColumn<ScoresLeaderboard, String> positionLeaderboardColumn;
    public TableColumn<ScoresLeaderboard, String> initialsLeaderboardColumn;
    public TableColumn<ScoresLeaderboard, String> scoreLeaderboardColumn;

    // Other FXML variables
    public Button menuButton;
    public Button statsButton;
    public Button resetScoresButton;
    public VBox resetPopUpBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Update leaderboard
        updateLeaderboard();
        updateTable();

        // If going to high scores leaderboard from game over screen
        if (gameFinishedLeaderboard) {
            gameFinishedLeaderboard = false;
            menuButton.setVisible(false);
            statsButton.setVisible(false);
            resetScoresButton.setText("Continue");
            resetScoresButton.setOnAction((event) -> {
                try {
                    App.setRoot("mainMenu");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void switchToStats() throws IOException {
        App.setRoot("stats");
    }

    @FXML
    private void openCloseResetPopUp() {
        if (resetPopUpBox.isVisible()) {
            resetPopUpBox.setVisible(false);
        } else {
            resetPopUpBox.setVisible(true);
        }
    }

    @FXML
    private void resetScoresAndStats() throws IOException {
        // Method to reset high scores and all stats
        resetScoresStats();
        App.setRoot("highScores"); // Reload scene if scores are reset to reflect new leaderboard
    }

    private void updateTable() {
        positionLeaderboardColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        initialsLeaderboardColumn.setCellValueFactory(new PropertyValueFactory<>("initials"));
        scoreLeaderboardColumn.setCellValueFactory(new PropertyValueFactory<>("scores"));
        highScoreLeaderboard.setItems(leaderboard);
    }
}
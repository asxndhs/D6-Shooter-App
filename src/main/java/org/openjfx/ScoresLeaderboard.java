package org.openjfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoresLeaderboard {
    private final SimpleStringProperty position;
    private final SimpleStringProperty initials;
    private final SimpleStringProperty scores;

    public ScoresLeaderboard(String position, String initials, String scores) {
        this.position = new SimpleStringProperty(position);
        this.initials = new SimpleStringProperty(initials);
        this.scores = new SimpleStringProperty(scores);
    }

    public String getPosition() {
        return position.get();
    }
    public void setPosition(String fName) {
        position.set(fName);
    }
    public StringProperty positionProperty(){
        return position;
    }

    public String getInitials() {
        return initials.get();
    }
    public void setInitials(String fName) {
        initials.set(fName);
    }
    public StringProperty initialsProperty(){
        return initials;
    }

    public String getScores() {
        return scores.get();
    }
    public void setScores(String fName) {
        scores.set(fName);
    }
    public StringProperty scoresProperty(){
        return scores;
    }
}

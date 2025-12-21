package com.game.backend.model;

public class HighscoreEntry {
    private int score;
    private long timestamp;

    public HighscoreEntry() {}

    public HighscoreEntry(int score) {
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public long getTimestamp() { return timestamp; }
}

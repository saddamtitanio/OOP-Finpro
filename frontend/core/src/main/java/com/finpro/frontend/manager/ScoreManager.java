package com.finpro.frontend.manager;

import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.Array;

public class ScoreManager {
    private int score;

    public interface ScoreListener {
        void onScoreChanged(int newScore);
    }

    private final Array<ScoreListener> listeners = new Array<>();

    public ScoreManager() {
        score = 0;
    }


    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
        notifyListeners();
    }

    public void setScore(int amount) {
        if (score == amount) return;
        score = amount;
        notifyListeners();
    }

    public void addScore(int amount) {
        if (amount == 0) return;
        score += amount;
        notifyListeners();
    }

    /* ================= LISTENERS ================= */

    public void addListener(ScoreListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ScoreListener listener) {
        listeners.removeValue(listener, true);
    }

    public void clearListeners() {
        listeners.clear();
    }

    private void notifyListeners() {
        for (ScoreListener listener : listeners) {
            listener.onScoreChanged(score);
        }
    }
}


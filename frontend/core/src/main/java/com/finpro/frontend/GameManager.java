package com.finpro.frontend;

import com.finpro.frontend.manager.ScoreManager;
import com.finpro.frontend.state.Game.GameStateManager;

public class GameManager {
    private static GameManager instance;
    private ScoreManager scoreManager;
    private boolean gameActive;

    private GameStateManager gsm;

    private GameManager() {
        this.scoreManager = new ScoreManager();
        this.gameActive = false;
        this.gsm = new GameStateManager();

    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    public void startGame() {
        scoreManager.setScore(0);
        gameActive = true;
        System.out.println("Game Started!");
    }

    public int getScore() {
        return this.scoreManager.getScore();
    }

    public void endGame() {
        gameActive = false;
    }

}

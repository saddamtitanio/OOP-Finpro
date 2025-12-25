package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.finpro.frontend.manager.LevelManager;
import com.finpro.frontend.manager.ScoreManager;
import com.finpro.frontend.manager.ZombieManager;
import com.finpro.frontend.service.BackendService;
import com.finpro.frontend.state.Game.GameStateManager;

import java.util.UUID;

public class GameManager {
    private static GameManager instance;
    private ScoreManager scoreManager;
    private boolean gameActive;
    private BackendService backendService;
    private GameStateManager gsm;
    private float elapsedSeconds;

    private UUID playerId;
    private String username;

    private GameManager() {
        this.elapsedSeconds = 0f;
        this.gameActive = false;
        this.gsm = new GameStateManager();
        this.backendService = new BackendService();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setPlayer(UUID playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public boolean hasPlayer() {
        return playerId != null;
    }

    public void startGame() {
        elapsedSeconds = 0f;
        scoreManager = new ScoreManager();
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void update(float delta) {
        if (gameActive) {
            elapsedSeconds += delta;
        }
    }
    public int getSecondsPlayed() {
        return (int) elapsedSeconds;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public int getScore() {
        return this.scoreManager.getScore();
    }

    public void endGame(ZombieManager zombieManager) {
        gameActive = false;
        backendService.submitRun(
            GameManager.getInstance().getPlayerId(),
            scoreManager.getScore(),
            GameManager.getInstance().getSecondsPlayed(),
            zombieManager.getKillStats(),
            new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    Gdx.app.log("Backend", "Run saved!");
                }

                @Override
                public void onError(int statusCode, String error) {
                    Gdx.app.error("Backend", error);
                }
            }
        );

    }

}

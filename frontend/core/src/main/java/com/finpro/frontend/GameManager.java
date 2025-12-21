package com.finpro.frontend;

import com.finpro.frontend.manager.ScoreManager;
import com.finpro.frontend.state.Game.GameStateManager;

public class GameManager {
    private static GameManager instance;
    private ScoreManager scoreManager;
    private boolean gameActive;
//    private BackendService backendService;
    private String currentPlayerId = null;
    private Integer coinsCollected = 0;

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

//    public void registerPlayer(String username) {
//        backendService.createPlayer(username, new BackendService.RequestCallback() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JsonValue json = new JsonReader().parse(response);
//                    currentPlayerId = json.getString("playerId");
//                    Gdx.app.log("Saved ID", currentPlayerId);
//                } catch (Exception e) {
//                    Gdx.app.error("Error", e.toString());
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                Gdx.app.error("Error", error);
//            }
//
//        });
//    }


    public void startGame() {
        scoreManager.setScore(0);
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void setScore(int distance) {
        if (gameActive) {
            scoreManager.setScore(distance);
        }
    }

    public int getScore() {
        return this.scoreManager.getScore();
    }

    public void endGame() {
        gameActive = false;
    }
}

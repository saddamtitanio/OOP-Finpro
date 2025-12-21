package com.finpro.frontend.state.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.finpro.frontend.manager.ScoreManager;

public class GameStateManager implements GameState{
    private GameState currentState;
    private GameState previousState;

    public void startGame() {
        if (currentState != null) {
            currentState.dispose();
        }

        if (previousState != null) {
            previousState.dispose();
            previousState = null;
        }

        currentState = new PlayState(this);
    }

    public void setWin(ScoreManager scoreManager){
        setState(new WinState(this, scoreManager));
    }

    public void setState(GameState state){
        if(currentState != null){
            currentState.dispose();
        }

        currentState = state;
    }

    public void pauseGame(PlayState playState) {
        previousState = playState;
        currentState = new PauseState(this, playState);
    }

    public void resumeGame() {
        if (previousState != null && previousState instanceof PlayState) {
            if (currentState != null) {
                currentState.dispose();
            }
            currentState = previousState;
            previousState = null;
        }
    }

    public void startMenu() {
        setState(new MenuState(this));
    }

    public void exitToMenu() {
        if (previousState != null) {
            previousState.dispose();
            previousState = null;
        }
        setState(new MenuState(this));
    }

    @Override
    public void update(float delta) {
        if(currentState != null){
            currentState.update(delta);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(currentState != null){
            currentState.render(batch);
        }
    }

    @Override
    public void dispose() {
        if(currentState != null){
            currentState.dispose();
        }

        if (previousState != null) {
            previousState.dispose();
        }

    }

}

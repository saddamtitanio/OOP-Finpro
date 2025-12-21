package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.finpro.frontend.manager.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.state.Game.GameStateManager;

public class Main extends Game {
    private GameStateManager gsm;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

        spriteBatch = new SpriteBatch();
        gsm = new GameStateManager();
        gsm.startMenu();
    }


    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();

        gsm.update(delta);
        gsm.render(spriteBatch);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }
}

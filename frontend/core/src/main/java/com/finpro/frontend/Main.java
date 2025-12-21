package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.state.Game.GameStateManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameStateManager gsm;


    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        gsm.startMenu();
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float delta = Gdx.graphics.getDeltaTime();

        gsm.update(delta);
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gsm.dispose();
    }
}

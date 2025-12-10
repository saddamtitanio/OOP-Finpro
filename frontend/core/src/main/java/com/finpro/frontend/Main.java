package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.command.InputHandler;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private InputHandler inputHandler;

    @Override
    public void create() {
        player = new Player(new Vector2(0, 0));
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float delta = Gdx.graphics.getDeltaTime();
        inputHandler.handleInput(player);
        player.update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.renderShape(shapeRenderer);
        shapeRenderer.end();

//        batch.begin();
//        player.render(batch);
//        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}

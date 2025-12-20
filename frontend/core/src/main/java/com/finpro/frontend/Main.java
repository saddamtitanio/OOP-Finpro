package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Boss boss;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private float screenWidth;
    private float screenHeight;

    @Override
    public void create() {

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        player = new Player(new Vector2(400, 300));
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Vector2 bossStartPosition = new Vector2(screenWidth / 2, screenHeight / 2);
        boss = new Boss(bossStartPosition, screenWidth, screenHeight,shapeRenderer);

    }
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float delta = Gdx.graphics.getDeltaTime();

        player.update(delta);

        Vector2 playerPosition = player.getPosition();
        boss.update(delta,playerPosition);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);
        boss.render(shapeRenderer);

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

package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.manager.DifficultyManager;
import com.finpro.frontend.manager.LevelManager;
import com.finpro.frontend.manager.ZombieManager;
import com.finpro.frontend.manager.TileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {

    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private LevelManager levelManager;
    private DifficultyManager difficultyManager;
    private ZombieManager zombieManager;
    private ZombieFactory zombieFactory;
    private TileManager tileManager;
    private WorldBounds worldBounds;



    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);


        tileManager = new TileManager(2f); // 16px → 32px
        tileManager.load("maps/test1.tmx");
        worldBounds = new WorldBounds(
            tileManager.getWorldWidth(),
            tileManager.getWorldHeight(),
            10f
        );

        camera.position.set(
            tileManager.getWorldWidth() / 2f,
            tileManager.getWorldHeight() / 2f,
            0
        );
        camera.update();

        shapeRenderer = new ShapeRenderer();

        player = new Player(new Vector2(5 * 32, 5 * 32), worldBounds);

        shapeRenderer = new ShapeRenderer();

        tileManager = new TileManager(2f);
        tileManager.load("maps/test1.tmx");

        zombieManager = new ZombieManager(worldBounds);
        zombieFactory = new ZombieFactory();
        difficultyManager = new DifficultyManager();
        levelManager = new LevelManager(zombieManager, zombieFactory, difficultyManager, worldBounds);

        zombieManager.setTarget(player);
    }



    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();

        // ------ UPDATE ------

        player.update(delta);
        levelManager.update(delta);

        camera.update();

        // ------ RENDER MAP ------
        tileManager.render(camera);

        // ------ RENDER SHAPES ------
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);
        levelManager.renderShape(shapeRenderer);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}

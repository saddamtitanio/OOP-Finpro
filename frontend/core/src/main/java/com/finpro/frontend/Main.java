package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.manager.LevelManager;
import com.finpro.frontend.manager.ZombieManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {

    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private LevelManager levelManager;
    private ZombieFactory zombieFactory;
    private ZombieManager zombieManager;

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        player = new Player(new Vector2(400, 300));  // example pos

        zombieManager = new ZombieManager();
        zombieFactory = new ZombieFactory();
        levelManager = new LevelManager(zombieManager, zombieFactory);

        zombieManager.setTarget(player);
    }



    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();





        // ------ UPDATE ------
        player.update(delta);
        levelManager.update(delta);


        // ------ RENDER ------
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

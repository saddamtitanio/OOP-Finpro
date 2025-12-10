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
import com.finpro.frontend.manager.ZombieManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {

    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private ZombieFactory zombieFactory;
    private ZombieManager zombieManager;

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        shapeRenderer = new ShapeRenderer();
        player = new Player(new Vector2(400, 300));  // example pos

        zombieFactory = new ZombieFactory();
        zombieManager = new ZombieManager();

        // ---------- SET FACTORY WEIGHTS ----------
        Map<String, Integer> weights = new HashMap<>();
        weights.put("FastZombie", 1);
        weights.put("JumpingZombie", 1);
        weights.put("BasicZombie", 1);
        zombieFactory.setWeights(weights);
    }

    private void spawnZombieAtMouse() {
        // Convert mouse to world coords
        Vector3 mouse3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse3);

        // Pick random zombie from factory (weighted)
        BaseZombie z = zombieFactory.createRandomZombie(mouse3.y, mouse3.x);

        if (z == null) return;

        z.setTarget(player);
        z.setActive(true);

        zombieManager.addZombie(z);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta);

        if (Gdx.input.justTouched()) {
            spawnZombieAtMouse();
        }


        // ------ UPDATE ZOMBIES ------
        zombieManager.update(delta);


        // ------ RENDER ------
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);

        for (BaseZombie z : zombieManager.getZombies()) {
            z.render(shapeRenderer);  // uses each subclass' drawShape()
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}

package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.command.InputHandler;
import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.observer.listener.ShootingListener;
import com.finpro.frontend.pool.BulletPool;

public class Main extends ApplicationAdapter {

    private Player player;
    private ShapeRenderer shapeRenderer;
    private InputHandler inputHandler;
    private BulletFactory bulletFactory;
    private BulletPool bulletPool;
    private EventManager eventManager;
    private ShootingListener shootingListener;
    private float strategyTimer = 0f;
    private boolean toggle = false;
    private final Array<Bullet> activeBullets = new Array<>();
    private OrthographicCamera camera;

    private LevelManager levelManager;
    private DifficultyManager difficultyManager;
    private ZombieManager zombieManager;
    private ZombieFactory zombieFactory;
    private TileManager tileManager;
    private WorldBounds worldBounds;

    @Override
    public void create() {
        bulletPool = new BulletPool();
        bulletFactory = new BulletFactory(bulletPool);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

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

        eventManager = new EventManager();
        shootingListener = new ShootingListener(bulletFactory);
        eventManager.subscribe(FireEvent.class, shootingListener);

        player = new Player(new Vector2(5 * 32, 5 * 32), eventManager, worldBounds);

        zombieManager = new ZombieManager(worldBounds);
        zombieFactory = new ZombieFactory();
        difficultyManager = new DifficultyManager();
        levelManager = new LevelManager(zombieManager, zombieFactory, difficultyManager, worldBounds);

        zombieManager.setTarget(player);

        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();
    }


    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();

        // ------ UPDATE ------

        inputHandler.handleInput(player);
        player.update(delta);
        levelManager.update(delta);

        camera.update();

        // ------ RENDER MAP ------
        tileManager.render(camera);

        // ------ RENDER SHAPES ------
        bulletPool.getActiveBullets(activeBullets);

        for (Bullet bullet : activeBullets) {
            bullet.update(delta);
            if (bullet.isOffScreen(tileManager)) {
                bulletPool.release(bullet);
            }
        }

        // testing powerups
//        strategyTimer += delta;
//        if (strategyTimer >= 5f && !toggle) {
//            strategyTimer = 0f;
//            toggle = true;
//            player.applyTemporaryStrategy(new SpreadShot());
//        }
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);
        levelManager.renderShape(shapeRenderer);

        bulletPool.getActiveBullets(activeBullets);

        for (Bullet bullet : activeBullets) {
            bullet.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}

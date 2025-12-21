package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.manager.*;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.command.InputHandler;
import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.observer.listener.ShootingListener;
import com.finpro.frontend.pool.BulletPool;
import com.finpro.frontend.pool.PowerUpPool;

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

    private PowerUpPool powerUpPool;
    private PowerUpManager powerUpManager;
    private CollisionSystem collisionSystem;

    @Override
    public void create() {
        tileManager = new TileManager(2f); // 16px → 32px
        tileManager.load("maps/test1.tmx");
        worldBounds = new WorldBounds(
            tileManager.getWorldWidth(),
            tileManager.getWorldHeight(),
            10f
        );

        bulletPool = new BulletPool();
        bulletFactory = new BulletFactory(bulletPool);

        powerUpPool = new PowerUpPool();
        powerUpManager = new PowerUpManager(powerUpPool, tileManager);

        camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(
            tileManager.getWorldWidth() / 2f,
            tileManager.getWorldHeight() / 2f,
            0
        );
        camera.update();

        eventManager = new EventManager();
        shootingListener = new ShootingListener(bulletFactory);
        eventManager.subscribe(FireEvent.class, shootingListener);

        player = new Player(new Vector2(5 * 32, 5 * 32), eventManager);

        zombieManager = new ZombieManager(worldBounds);
        zombieFactory = new ZombieFactory();
        difficultyManager = new DifficultyManager();
        levelManager = new LevelManager(zombieManager, zombieFactory, difficultyManager, worldBounds, powerUpManager);

        zombieManager.setTarget(player);

        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();

        collisionSystem = new CollisionSystem();
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

        collisionSystem.update(player, powerUpManager, zombieManager, tileManager, activeBullets);
        // ------ RENDER MAP ------
        tileManager.render(camera);

        // ------ RENDER SHAPES ------
        activeBullets.clear();
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

        levelManager.renderShape(shapeRenderer);

        bulletPool.getActiveBullets(activeBullets);

        for (Bullet bullet : activeBullets) {
            bullet.render(shapeRenderer);
        }
        player.renderShape(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}

package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.*;
import com.finpro.frontend.command.InputHandler;
import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.manager.*;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.observer.listener.ShootingListener;
import com.finpro.frontend.pool.BulletPool;
import com.finpro.frontend.pool.PowerUpPool;

public class PlayState implements GameState {

    private final GameStateManager gsm;

    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private InputHandler inputHandler;

    private BulletPool bulletPool;
    private BulletFactory bulletFactory;
    private final Array<Bullet> activeBullets = new Array<>();

    private EventManager eventManager;
    private ShootingListener shootingListener;

    private TileManager tileManager;
    private WorldBounds worldBounds;
    private ZombieManager zombieManager;
    private ZombieFactory zombieFactory;
    private DifficultyManager difficultyManager;
    private LevelManager levelManager;

    private PowerUpPool powerUpPool;
    private PowerUpManager powerUpManager;
    private CollisionSystem collisionSystem;

    private float worldWidth;
    private float worldHeight;

    private Boss boss;


    public PlayState(GameStateManager gsm) {
        this.gsm = gsm;

        // ---- MAP ----
        tileManager = new TileManager(2f);
        tileManager.load("maps/test1.tmx");

        worldWidth = tileManager.getWorldWidth();
        worldHeight = tileManager.getWorldHeight();

        worldBounds = new WorldBounds(
            worldWidth,
            worldHeight,
            10f
        );

        // ---- CAMERA ----
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
        camera.position.set(
            tileManager.getWorldWidth() / 2f,
            tileManager.getWorldHeight() / 2f,
            0
        );
        camera.update();

        // ---- BULLETS ----
        bulletPool = new BulletPool();
        bulletFactory = new BulletFactory(bulletPool);

        // ---- EVENTS ----
        eventManager = new EventManager();
        shootingListener = new ShootingListener(bulletFactory);
        eventManager.subscribe(FireEvent.class, shootingListener);

        // ---- PLAYER ----
        player = new Player(new Vector2(5 * 32, 5 * 32), eventManager);

        // ---- ZOMBIES ----
        zombieManager = new ZombieManager(worldBounds);
        zombieFactory = new ZombieFactory();
        difficultyManager = new DifficultyManager();

        powerUpPool = new PowerUpPool();
        powerUpManager = new PowerUpManager(powerUpPool, tileManager);

        levelManager = new LevelManager(
            zombieManager,
            zombieFactory,
            difficultyManager,
            worldBounds,
            powerUpManager
        );

        zombieManager.setTarget(player);

        // ---- SYSTEMS ----
        collisionSystem = new CollisionSystem();
        inputHandler = new InputHandler();
        shapeRenderer = new ShapeRenderer();

        boss = new Boss(
            new Vector2(worldWidth / 2, worldHeight / 2),
            worldWidth,
            worldHeight,
            shapeRenderer);
    }

    @Override
    public void update(float delta) {
        inputHandler.handleInput(player);
        player.update(delta);
        levelManager.update(delta);

        boss.update(delta, player.getPosition());

        bulletPool.getActiveBullets(activeBullets);
        for (Bullet bullet : activeBullets) {
            bullet.update(delta);
            if (bullet.isOffScreen(tileManager)) {
                bulletPool.release(bullet);
            }
        }

        collisionSystem.update(
            player,
            powerUpManager,
            zombieManager,
            tileManager,
            activeBullets
        );

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pauseGame(this);
        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        tileManager.render(camera);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        levelManager.renderShape(shapeRenderer);

        activeBullets.clear();
        bulletPool.getActiveBullets(activeBullets);
        for (Bullet bullet : activeBullets) {
            bullet.render(shapeRenderer);
        }

        player.renderShape(shapeRenderer);
        boss.render(shapeRenderer);
        
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}

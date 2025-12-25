package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.*;
import com.finpro.frontend.command.InputHandler;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.manager.*;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.observer.listener.ShootingListener;
import com.finpro.frontend.pool.BulletPool.BulletPool;
import com.finpro.frontend.pool.PowerUpPool.PowerUpPool;

public class PlayState implements GameState {

    private final GameStateManager gsm;

    private Player player;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private InputHandler inputHandler;

    private BulletPool bulletPool;

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

    private ScoreManager scoreManager;

    private HUD hud;

    private BulletManager bulletManager;

    private float bossSpawnTimer = 0f;
    private boolean bossSpawnPending = false;

    private static final float BOSS_SPAWN_DELAY = 5f;
    private static final float BOSS_MIN_DISTANCE = 300f;

    public PlayState(GameStateManager gsm) {
        this.gsm = gsm;

        this.scoreManager = new ScoreManager();

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
        bulletManager = new BulletManager(bulletPool);

        // ---- EVENTS ----
        eventManager = new EventManager();
        shootingListener = new ShootingListener(bulletManager);
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
        collisionSystem = new CollisionSystem(scoreManager, levelManager);
        inputHandler = new InputHandler();
        shapeRenderer = new ShapeRenderer();

        boss = null;

        levelManager.setOnBossLevelStart(() -> {
            bossSpawnPending = true;
            bossSpawnTimer = 0f;
        });

        GameManager.getInstance().startGame();

        hud = new HUD(scoreManager);

    }

    @Override
    public void update(float delta) {
        if (boss != null) {
            boss.update(delta, player.getPosition());
        }

        if (boss != null && boss.isDead()) {
            gsm.setWin(scoreManager);
            GameManager.getInstance().endGame();
        }

        inputHandler.handleInput(player);
        player.update(delta);
        levelManager.update(delta);

        bulletManager.update(delta, tileManager);

        collisionSystem.update(
            player,
            powerUpManager,
            zombieManager,
            tileManager,
            bulletManager,
            boss
        );

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pauseGame(this);
        }

        camera.update();

        if (!player.isAlive()) {
            GameManager.getInstance().endGame();
            gsm.setState(new GameOverState(gsm, scoreManager));
        }

        hud.update(player, levelManager);

        if (bossSpawnPending && boss == null) {
            bossSpawnTimer += delta;

            if (bossSpawnTimer >= BOSS_SPAWN_DELAY) {
                Vector2 spawnPos = findBossSpawnPosition(player.getPosition());

                boss = new Boss(
                    spawnPos,
                    worldWidth,
                    worldHeight,
                    shapeRenderer
                );

                bossSpawnPending = false;
            }
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        tileManager.render(camera);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        levelManager.renderShape(shapeRenderer);

        for (Bullet bullet : bulletManager.getActive()) {
            bullet.render(shapeRenderer);
        }


        player.renderShape(shapeRenderer);
        if (boss != null) {
            boss.render(shapeRenderer);
        }

        shapeRenderer.end();

        hud.render();

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private Vector2 findBossSpawnPosition(Vector2 playerPos) {
        Vector2 spawn;
        int attempts = 0;

        do {
            spawn = new Vector2(
                (float) Math.random() * worldWidth,
                (float) Math.random() * worldHeight
            );
            attempts++;
        } while (spawn.dst2(playerPos) < BOSS_MIN_DISTANCE * BOSS_MIN_DISTANCE && attempts < 20);

        return spawn;
    }

}

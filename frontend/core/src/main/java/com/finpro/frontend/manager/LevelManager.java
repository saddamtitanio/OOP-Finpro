package com.finpro.frontend.manager;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.finpro.frontend.GameManager;
import com.finpro.frontend.WorldBounds;
import com.finpro.frontend.config.LevelConfig;
import com.finpro.frontend.config.LevelData;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.strategy.powerup.PowerUp;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

import java.util.HashMap;
import java.util.Vector;

public class LevelManager {

    private final Array<LevelConfig> levels;
    private final ZombieFactory zombieFactory;
    private final ZombieManager zombieManager;
    private final DifficultyManager difficultyManager;
    private final WorldBounds worldBounds;
    private final PowerUpManager powerUpManager;
    private final SpawnManager spawnManager;

    private int currentLevelIndex = 0;
    private float levelTimer = 0f;
    private float spawnInterval = 0f;
    private HashMap<String, Integer> weights;

    private Runnable onBossLevelStart;

    public LevelManager(ZombieManager zombieManager, ZombieFactory zombieFactory, DifficultyManager difficultyManager, WorldBounds worldBounds, PowerUpManager powerUpManager) {
        this.powerUpManager = powerUpManager;
        Json json = new Json();

        FileHandle levelFile = Gdx.files.internal("config/LevelConfig.json");
        LevelData data = json.fromJson(LevelData.class, levelFile);

        this.levels = data.levels;
        this.zombieFactory = zombieFactory;
        this.zombieManager = zombieManager;
        this.difficultyManager = difficultyManager;
        this.worldBounds = worldBounds;


        Rectangle spawnBounds = new Rectangle(
            0,
            0,
            worldBounds.getWorldWidth() -32f,
            worldBounds.getWorldHeight() -32f
        );

        this.spawnManager = new SpawnManager(spawnBounds, 1f);
    }

    public boolean isBossLevel() {
        if (currentLevelIndex >= levels.size) return false;
        return levels.get(currentLevelIndex).isBossLevel;
    }

    public void update(float delta){
        updateSpawning(delta);
        zombieManager.update(delta, worldBounds);
        powerUpManager.update(delta);
    }

    private void updateSpawning(float delta) {
        if (currentLevelIndex >= levels.size) return;

        LevelConfig cfg = levels.get(currentLevelIndex);

        levelTimer += delta;
        spawnInterval += delta;

        if (!cfg.isBossLevel) {
            if (spawnInterval >= cfg.spawnInterval && levelTimer <= cfg.levelDuration) {
                spawnDensity(cfg);
            }

            if (levelTimer >= cfg.levelDuration && zombieManager.getZombies().isEmpty()) {
                advanceLevel();
            }

            return;
        }

        if (cfg.spawnZombiesDuringBoss && spawnInterval >= cfg.spawnInterval) {
            spawnDensity(cfg);
        }
    }

    private void spawnDensity(LevelConfig cfg) {
        int base = cfg.spawnDensity;
        int scaled = difficultyManager.scaleInt(base);
        int variation = (int)(Math.random() * 3) - 1;
        int density = Math.max(1, scaled + variation);

        spawnWave(density);
        spawnInterval = 0f;
    }

    public void setOnBossLevelStart(Runnable callback) {
        this.onBossLevelStart = callback;
    }

    private void advanceLevel() {
        if (currentLevelIndex >= levels.size) {
            System.out.println("All levels completed!");
            return;
        }

        currentLevelIndex++;
        levelTimer = 0f;
        spawnInterval = 0f;

        LevelConfig nextLevel = levels.get(currentLevelIndex);

        if (nextLevel.isBossLevel && onBossLevelStart != null) {
            onBossLevelStart.run();
        }

        System.out.println("Level " + nextLevel.levelId + " started!");
    }

    private void spawnWave(int density){
        weights = levels.get(currentLevelIndex).enemySpawnRatio;
        zombieFactory.setWeights(weights);

        for (int i = 0; i < density; i++) {
            Vector2 position = spawnManager.getBorderSpawn();
            addZombie(position.x,position.y);
        }

        System.out.println("Spawned wave with " + density + " zombies");
    }

    public void renderShape(ShapeRenderer shapeRenderer){
        for (PowerUpEntity powerUp : powerUpManager.getActivePowerUps()) {
            powerUp.renderShape(shapeRenderer);
        }
        for (BaseZombie z : zombieManager.getZombies()) {
            z.render(shapeRenderer);
        }
    }

    public void addZombie(float x, float y){
        BaseZombie z = zombieFactory.createRandomZombie(x, y);
        zombieManager.addZombie(z);
    }

    public void killZombie(BaseZombie z){
        zombieManager.removeZombie(z);
        zombieFactory.releaseZombie(z);
        zombieManager.registerKill(z);
    }

    public float getLevelTimer() {
        return levelTimer;
    }

    public float getLevelDuration() {
        if (currentLevelIndex >= levels.size) return 0;
        LevelConfig cfg = levels.get(currentLevelIndex);
        return cfg.levelDuration;
    }
}

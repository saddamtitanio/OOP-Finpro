package com.finpro.frontend.manager;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.finpro.frontend.WorldBounds;
import com.finpro.frontend.config.LevelConfig;
import com.finpro.frontend.config.LevelData;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.factory.ZombieFactory;
import com.finpro.frontend.strategy.powerup.PowerUp;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

import java.util.HashMap;

public class LevelManager {

    private final Array<LevelConfig> levels;
    private final ZombieFactory zombieFactory;
    private final ZombieManager zombieManager;
    private final DifficultyManager difficultyManager;
    private final WorldBounds worldBounds;
    private final PowerUpManager powerUpManager;

    private int currentLevelIndex = 0;
    private float levelTimer = 0f;
    private float spawnInterval = 0f;
    private HashMap<String, Integer> weights;

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
    }

    public void update(float delta){
        updateSpawning(delta);
        zombieManager.update(delta, worldBounds);
        powerUpManager.update(delta);
    }

    private void updateSpawning(float delta) {
        LevelConfig cfg = levels.get(currentLevelIndex);
        levelTimer += delta;
        spawnInterval += delta;

        if (spawnInterval >= cfg.spawnInterval && levelTimer <= cfg.levelDuration) {

            int base = cfg.spawnDensity;

            int scaled = difficultyManager.scaleInt(base);

            int variation = (int)(Math.random() * 3) - 1;
            int density = Math.max(1, scaled + variation);

            spawnWave(density);
            spawnInterval = 0f;
        }

        if(levelTimer >= cfg.levelDuration && zombieFactory.getAllInUseZombies() == null){
            System.out.println("LEVEL COMPLETE!");
        }
    }

    private void spawnWave(int density){
        weights = levels.get(currentLevelIndex).enemySpawnRatio;
        zombieFactory.setWeights(weights);

        for (int i = 0; i < density; i++) {
            float x = (float)Math.random() * 800; // random variable NOT FINAL implement tile grid system
            float y = (float)Math.random() * 600; // NOT FINAL( i want to include pos but better discuss first)
            addZombie(x,y);
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
    }

}

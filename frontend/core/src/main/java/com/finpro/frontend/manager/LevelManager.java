package com.finpro.frontend.manager;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.finpro.frontend.config.LevelConfig;
import com.finpro.frontend.config.LevelData;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.factory.ZombieFactory;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {

    private final Array<LevelConfig> levels;
    private final ZombieFactory zombieFactory;
    private final ZombieManager zombieManager;

    private int currentLevelIndex = 0;
    private int currentWave = 0;
    private float waveTimer = 9f;
    private HashMap<String, Integer> weights;

    public LevelManager(ZombieManager zombieManager, ZombieFactory zombieFactory) {
        Json json = new Json();

        FileHandle file = Gdx.files.internal("config/levelConfig.json");
        LevelData data = json.fromJson(LevelData.class, file);

        this.levels = data.levels;
        this.zombieFactory = zombieFactory;
        this.zombieManager = zombieManager;
    }

    public void update(float delta){
        updateSpawning(delta);
        zombieManager.update(delta);
    }

    private void updateSpawning(float delta) {

        LevelConfig cfg = levels.get(currentLevelIndex);
        waveTimer += delta;

        if (waveTimer >= cfg.waveDuration && currentWave < cfg.waveAmount) {

            int density = cfg.spawnDensity[currentWave];
            spawnWave(density);

            currentWave++;

            waveTimer = 0f;

            // Level complete?
            if (currentWave >= cfg.waveAmount) {
                System.out.println("LEVEL COMPLETE!");
                // idk yet tbh how to go to next level
            }
        }
    }

    private void spawnWave(int density){
        weights = levels.get(currentLevelIndex).enemySpawnRatio;
        zombieFactory.setWeights(weights);

        for (int i = 0; i < density; i++) {
            float x = (float)Math.random() * 800; // random variable NOT FINAL
            float y = (float)Math.random() * 600; // NOT FINAL( i want to include pos but better discuss first)
            addZombie(x,y);
        }

        System.out.println("Spawned wave with " + density + " zombies");
    }

    public void renderShape(ShapeRenderer shapeRenderer){
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

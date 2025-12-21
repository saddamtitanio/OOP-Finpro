package com.finpro.frontend.manager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.factory.PowerUpFactory;
import com.finpro.frontend.pool.PowerUpPool;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;


public class PowerUpManager {
    private final PowerUpFactory powerUpFactory;
    private final Array<PowerUpEntity> activePowerUps = new Array<>();
    private final PowerUpPool powerUpPool;

    private float spawnTimer = 0f;
    private float nextSpawnTime = 0f;

    private final float MIN_SPAWN_TIME = 12f;
    private final float MAX_SPAWN_TIME = 25f;

    private final TileManager tileManager;
    public PowerUpManager(PowerUpPool powerUpPool, TileManager tileManager) {
        this.powerUpPool = powerUpPool;
        this.powerUpFactory = new PowerUpFactory(powerUpPool);
        this.tileManager = tileManager;
        scheduleNextSpawn();
    }

    public Array<PowerUpEntity> getActivePowerUps() {
        return activePowerUps;
    }

    private void scheduleNextSpawn() {
        nextSpawnTime = MathUtils.random(MIN_SPAWN_TIME, MAX_SPAWN_TIME);
        spawnTimer = 0f;
    }

    private void spawnRandom() {
        Vector2 pos = randomPosition();
        PowerUpEntity entity = powerUpFactory.create(pos);
        activePowerUps.add(entity);
    }

    private Vector2 randomPosition() {
        float x = MathUtils.random(0, tileManager.getWorldWidth() - PowerUpEntity.SIZE);

        float y = MathUtils.random(0, tileManager.getWorldHeight() - PowerUpEntity.SIZE);

        return new Vector2(x, y);
    }

    public void update(float delta) {
        spawnTimer += delta;

        if (spawnTimer >= nextSpawnTime) {
            spawnRandom();
            scheduleNextSpawn();
        }
        for (int i = activePowerUps.size - 1; i >= 0; i--) {
            PowerUpEntity p = activePowerUps.get(i);
            p.update(delta);

            if (p.isExpired()) {
                powerUpPool.release(p);
                activePowerUps.removeIndex(i);
            }
        }
    }

    public void collect(PowerUpEntity entity) {
        activePowerUps.removeValue(entity, true);
        powerUpPool.release(entity);
    }
}

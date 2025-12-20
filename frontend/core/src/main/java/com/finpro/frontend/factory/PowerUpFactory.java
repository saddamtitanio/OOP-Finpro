package com.finpro.frontend.factory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.pool.PowerUpPool;
import com.finpro.frontend.strategy.powerup.*;

public class PowerUpFactory {
    private final PowerUpPool powerUpPool;

    public PowerUpFactory(PowerUpPool powerUpPool) {
        this.powerUpPool = powerUpPool;
    }

    public PowerUpEntity create(Vector2 pos) {
        PowerUpEntity entity = powerUpPool.obtain();

        PowerUp powerUp = createRandomPowerUp();

        entity.init(powerUp, pos);
        return entity;
    }

    private PowerUp createRandomPowerUp() {
        float duration = 5f;

        int roll = MathUtils.random(1, 3);

        System.out.println(roll);
        switch (roll) {
            case 1:
                return new SpreadShotPowerUp(duration);
            case 2:
                return new SpeedIncreasePowerUp(duration);
            case 3:
                return new BurstShotPowerUp(duration);
            default:
                return new SpreadShotPowerUp(duration);
        }
    }
}

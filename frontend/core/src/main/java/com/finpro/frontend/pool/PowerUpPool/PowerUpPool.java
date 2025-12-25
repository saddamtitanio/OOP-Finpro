package com.finpro.frontend.pool.PowerUpPool;

import com.finpro.frontend.pool.ObjectPool;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

public class PowerUpPool extends ObjectPool<PowerUpEntity> {
    @Override
    protected PowerUpEntity createObject() {
        return new PowerUpEntity();
    }

    @Override
    protected void resetObject(PowerUpEntity powerUpEntity) {
        powerUpEntity.reset();
    }
}

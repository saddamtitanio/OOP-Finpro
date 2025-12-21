package com.finpro.frontend.pool;

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

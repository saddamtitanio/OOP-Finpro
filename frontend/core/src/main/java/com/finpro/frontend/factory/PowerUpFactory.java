package com.finpro.frontend.factory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.pool.PowerUpPool;
import com.finpro.frontend.strategy.powerup.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PowerUpFactory {
    private final PowerUpPool powerUpPool;
    private final List<Supplier<PowerUp>> powerUpRegistry = new ArrayList<>();

    public PowerUpFactory(PowerUpPool powerUpPool) {
        this.powerUpPool = powerUpPool;

        register(() -> new SpreadShotPowerUp(10f));
        register(() -> new SpeedIncreasePowerUp(10f));
        register(() -> new BurstShotPowerUp(10f));
        register(() -> new RadialShotPowerUp(10f));
        register(() -> new HealthPowerUp(10f));

    }

    public void register(Supplier<PowerUp> supplier) {
        powerUpRegistry.add(supplier);
    }

    public PowerUpEntity create(Vector2 pos) {
        PowerUpEntity entity = powerUpPool.obtain();
        PowerUp powerUp = powerUpRegistry.get(MathUtils.random(powerUpRegistry.size() - 1)).get();
        entity.init(powerUp, pos);
        return entity;
    }
}

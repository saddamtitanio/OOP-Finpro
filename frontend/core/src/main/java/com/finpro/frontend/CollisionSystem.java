package com.finpro.frontend;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.manager.PowerUpManager;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

public class CollisionSystem {
    public CollisionSystem() {
    }

    public void update(Player player, PowerUpManager powerUpManager) {
        handlePlayerPowerUps(player, powerUpManager);
    }

    public void handlePlayerPowerUps(Player player, PowerUpManager powerUpManager) {
        for (int i = powerUpManager.getActivePowerUps().size - 1; i >= 0; i--) {
            PowerUpEntity powerUp = powerUpManager.getActivePowerUps().get(i);

            if (player.getCollider().overlaps(powerUp.getCollider())) {
                player.gainPowerUp(powerUp.getPowerUp());
                powerUpManager.collect(powerUp);
            }
        }
    }
}

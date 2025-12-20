package com.finpro.frontend;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.manager.PowerUpManager;
import com.finpro.frontend.manager.TileManager;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

public class CollisionSystem {
    public void update(Player player, PowerUpManager powerUpManager, TileManager tileManager) {
        handlePlayerPowerUps(player, powerUpManager);
        handlePlayerWorld(player, tileManager);
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

    public void handlePlayerWorld(Player player, TileManager tileManager) {
        float worldWidth = tileManager.getWorldWidth();
        float worldHeight = tileManager.getWorldHeight();

        if (player.getPosition().x < 0) {
            player.getPosition().x = 0;
        } else if (player.getPosition().x + player.getWidth() > worldWidth) {
            player.getPosition().x = worldWidth - player.getWidth();
        }

        if (player.getPosition().y < 0) {
            player.getPosition().y = 0;
        } else if (player.getPosition().y + player.getHeight() > worldHeight) {
            player.getPosition().y = worldHeight - player.getHeight();
        }

        player.updateCollider();
    }

}

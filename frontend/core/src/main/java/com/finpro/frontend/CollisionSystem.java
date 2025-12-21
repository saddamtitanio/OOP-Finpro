package com.finpro.frontend;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.manager.PowerUpManager;
import com.finpro.frontend.manager.ScoreManager;
import com.finpro.frontend.manager.TileManager;
import com.finpro.frontend.manager.ZombieManager;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.strategy.powerup.PowerUp;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

public class CollisionSystem {
    private ScoreManager scoreManager;

    public CollisionSystem(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void update(Player player, PowerUpManager powerUpManager, ZombieManager zombieManager, TileManager tileManager, Array<Bullet> bullets) {
        handlePlayerPowerUps(player, powerUpManager);
        handlePlayerWorld(player, tileManager);
        handlePlayerZombie(player, zombieManager);
        handleZombieBullet(bullets, zombieManager);
    }

    private void handlePlayerPowerUps(Player player, PowerUpManager powerUpManager) {
        for (int i = powerUpManager.getActivePowerUps().size - 1; i >= 0; i--) {
            PowerUpEntity powerUpEntity = powerUpManager.getActivePowerUps().get(i);

            if (player.getCollider().overlaps(powerUpEntity.getCollider())) {
                PowerUp powerUp = powerUpEntity.getPowerUp();
                player.gainPowerUp(powerUp);

                // Remove from the world
                powerUpManager.collect(powerUpEntity);
            }
        }
    }

    private void handlePlayerWorld(Player player, TileManager tileManager) {
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

    private void handlePlayerZombie(Player player, ZombieManager zombieManager) {
        for (int i = zombieManager.getZombies().size() - 1; i >= 0; i--) {
            BaseZombie baseZombie = zombieManager.getZombies().get(i);

            if (player.getCollider().overlaps(baseZombie.getCollider())) {
                zombieManager.removeZombie(baseZombie);
                player.takeDamage(20);
            }
        }
    }

    private void handleZombieBullet(Array<Bullet> bullets, ZombieManager zombieManager) {
        for (Bullet bullet : bullets) {
            Circle bulletCollider = bullet.getCollider();

            for (int i = zombieManager.getZombies().size() - 1; i >= 0; i--) {
                BaseZombie baseZombie = zombieManager.getZombies().get(i);
                if (Intersector.overlaps(bulletCollider, baseZombie.getCollider())) {
                    scoreManager.addScore(10);
                    zombieManager.removeZombie(baseZombie);
                    bullet.deactivate();
                    break;
                }
            }
        }
    }
}

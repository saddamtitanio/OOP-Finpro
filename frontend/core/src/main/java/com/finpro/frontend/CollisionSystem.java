package com.finpro.frontend;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.manager.PowerUpManager;
import com.finpro.frontend.manager.TileManager;
import com.finpro.frontend.manager.ZombieManager;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.obstacle.BossObstacle.BaseBossAttack;
import com.finpro.frontend.obstacle.BossObstacle.BossLaserAttackObstacle;
import com.finpro.frontend.strategy.powerup.PowerUp;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

import java.util.List;

public class CollisionSystem {
    public void update(Player player, PowerUpManager powerUpManager, ZombieManager zombieManager, TileManager tileManager, Array<Bullet> bullets, Boss boss) {
        handlePlayerPowerUps(player, powerUpManager);
        handlePlayerWorld(player, tileManager);
        handlePlayerZombie(player, zombieManager);
        handleZombieBullet(bullets, zombieManager);

        handleBossWorld(boss,tileManager);
        handleBossBullet(bullets, boss);
        handleBossPlayer(boss, player);

        handleBossAttackCollisions(boss, player);
    }

    private void handleBossWorld(Boss boss, TileManager tileManager){
        float worldWidth = tileManager.getWorldWidth();
        float worldHeight = tileManager.getWorldHeight();

        float bossHalfWidth = boss.getWidth() / 2;
        float bossHalfHeight = boss.getHeight() / 2;

        Vector2 bossPos = boss.getPosition();

        if (bossPos.x - bossHalfWidth < 0) {
            bossPos.x = bossHalfWidth;
        } else if (bossPos.x + bossHalfWidth > worldWidth) {
            bossPos.x = worldWidth - bossHalfWidth;
        }

        if (bossPos.y - bossHalfHeight < 0) {
            bossPos.y = bossHalfHeight;
        } else if (bossPos.y + bossHalfHeight > worldHeight) {
            bossPos.y = worldHeight - bossHalfHeight;
        }

        boss.setPosition(bossPos);
        boss.updateCollider();
    }

    private void handleBossBullet(Array<Bullet> bullets, Boss boss) {
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);

            Circle bulletCollider = bullet.getCollider();

            Rectangle bossCollider = boss.getCollider();

            if (Intersector.overlaps(bulletCollider, bossCollider)) {
                int damage = 100;
                boss.takeDamage(damage);
                bullet.deactivate();
                bullets.removeIndex(i);
                break;
            }
        }
    }

    private void handleBossPlayer(Boss boss, Player player) {
        Rectangle bossCollider = boss.getCollider();
        Rectangle playerCollider = player.getCollider();

        if (bossCollider.overlaps(playerCollider)) {
            int bossDamage = 9999;
            player.takeDamage(bossDamage);
        }
    }

    private void handleBossAttackCollisions(Boss boss, Player player){
        if(boss == null){
            return;
        }
        List<BaseBossAttack> attacks = boss.getAttackManager().getActiveAttacks();
        for (BaseBossAttack attack : attacks) {
            if (attack.isActive() && attack.isAttackMode()) {
                checkAttackCollision(attack, player);
            }
        }
    }

    public void checkAttackCollision(BaseBossAttack attack, Player player){
        Rectangle playerCollider = player.getCollider();

        if (attack.getCircleCollider() != null) {
            checkCircleCollision(attack.getCircleCollider(), playerCollider, player);
        }

        else if (attack instanceof BossLaserAttackObstacle) {
            BossLaserAttackObstacle laser = (BossLaserAttackObstacle) attack;
            for (Rectangle laserRect : laser.getLaserColliders()) {
                if (laserRect.overlaps(playerCollider)) {
                    float damage = player.getHP() / 2;
                    player.takeDamage(damage);
                    break;
                }
            }
        }
        else if (attack.getCollider() != null) {
            checkRectangleCollision(attack.getCollider(), playerCollider, player);
        }
    }

    private void checkRectangleCollision(Rectangle attackRect, Rectangle playerRect, Player player) {
        if (attackRect.overlaps(playerRect)) {
            float damage = player.getHP() / 2;
            player.takeDamage(damage);
        }
    }

    private void checkCircleCollision(Circle circle, Rectangle rect, Player player) {
        float closestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float closestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));

        float distanceX = circle.x - closestX;
        float distanceY = circle.y - closestY;

        boolean collision = (distanceX * distanceX + distanceY * distanceY) < (circle.radius * circle.radius);

        if(collision) {
            float damage = player.getHP() / 2;
            player.takeDamage(damage);
        }
    }

    private void handlePlayerPowerUps(Player player, PowerUpManager powerUpManager) {
        for (int i = powerUpManager.getActivePowerUps().size - 1; i >= 0; i--) {
            PowerUpEntity powerUpEntity = powerUpManager.getActivePowerUps().get(i);

            if (player.getCollider().overlaps(powerUpEntity.getCollider())) {
                PowerUp powerUp = powerUpEntity.getPowerUp();
                player.gainPowerUp(powerUp);

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
                    // use the bullet.getDamage() to have an effect on the zombie
                    zombieManager.removeZombie(baseZombie);
                    bullet.deactivate();
                    break;
                }
            }
        }
    }
}

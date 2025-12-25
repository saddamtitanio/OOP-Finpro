package com.finpro.frontend;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.manager.*;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.obstacle.BossObstacle.BaseBossAttack;
import com.finpro.frontend.obstacle.BossObstacle.BossLaserAttackObstacle;
import com.finpro.frontend.strategy.powerup.PowerUp;
import com.finpro.frontend.strategy.powerup.PowerUpEntity;

import java.util.List;

public class CollisionSystem {
    private LevelManager levelManager;

    public CollisionSystem(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void update(
        Player player,
        PowerUpManager powerUpManager,
        ZombieManager zombieManager,
        TileManager tileManager,
        BulletManager bulletManager,
        Boss boss
    ) {
        handlePlayerPowerUps(player, powerUpManager);
        handlePlayerWorld(player, tileManager);
        handlePlayerZombie(player, zombieManager, levelManager);
        handleZombieBullet(bulletManager, zombieManager, levelManager);

        if (boss != null) {
            handleBossWorld(boss, tileManager);
            handleBossBullet(bulletManager, boss);
            handleBossPlayer(boss, player);
            handleBossAttackCollisions(boss, player);
        }
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

    private void handleBossBullet(BulletManager bulletManager, Boss boss) {
        Array<Bullet> bullets = bulletManager.getActive();

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);

            Circle bulletCollider = bullet.getCollider();

            Rectangle bossCollider = boss.getCollider();

            if (Intersector.overlaps(bulletCollider, bossCollider)) {
                int damage = 100;
                boss.takeDamage(damage);
                GameManager.getInstance().getScoreManager().addScore(10);
                bulletManager.destroy(i);
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
                    player.takeDamage(0.1f);
                    break;
                }
            }
        }
    }

    private void checkCircleCollision(Circle circle, Rectangle rect, Player player) {
        float closestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float closestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));

        float distanceX = circle.x - closestX;
        float distanceY = circle.y - closestY;

        boolean collision = (distanceX * distanceX + distanceY * distanceY) < (circle.radius * circle.radius);

        if(collision) {
            player.takeDamage(0.1f);
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

    private void handlePlayerZombie(Player player, ZombieManager zombieManager, LevelManager levelManager) {
        for (int i = zombieManager.getZombies().size() - 1; i >= 0; i--) {
            BaseZombie baseZombie = zombieManager.getZombies().get(i);

            if (player.getCollider().overlaps(baseZombie.getCollider())) {
                levelManager.killZombie(baseZombie);
                player.takeDamage(10);
            }
        }
    }

    private void handleZombieBullet(BulletManager bulletManager, ZombieManager zombieManager, LevelManager levelManager) {
        Array<Bullet> bullets = bulletManager.getActive();

        for (int b = bullets.size - 1; b >= 0; b--) {
            Bullet bullet = bullets.get(b);
            Circle bulletCollider = bullet.getCollider();

            for (int z = zombieManager.getZombies().size() - 1; z >= 0; z--) {
                BaseZombie zombie = zombieManager.getZombies().get(z);

                if (Intersector.overlaps(bulletCollider, zombie.getCollider())) {
                    GameManager.getInstance().getScoreManager().addScore(10);
                    levelManager.killZombie(zombie);
                    bulletManager.destroy(b);
                    break;
                }
            }
        }
    }
}

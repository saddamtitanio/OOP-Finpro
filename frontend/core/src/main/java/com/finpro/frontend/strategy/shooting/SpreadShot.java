package com.finpro.frontend.strategy.shooting;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.manager.BulletManager;

public class SpreadShot extends AttackStrategy {
    public SpreadShot() {
        super(0.8f);
    }

    @Override
    public void shoot(BulletManager bulletManager, Player player, Vector2 dir) {
        final float OFFSET = 10f;
        final int NO_OF_BULLETS = 3;
        final float SPREAD_ANGLE = 30f;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(dir, OFFSET);
        float startAngle = -SPREAD_ANGLE / 2f;

        Vector2 bulletDir = new Vector2(dir);

        for (int i = 0; i < NO_OF_BULLETS; i++) {
            float angle = startAngle + i * (SPREAD_ANGLE / (NO_OF_BULLETS - 1));
            bulletDir.set(dir).rotateDeg(angle);
            bulletManager.spawn(spawnPos, bulletDir);
        }

        resetCooldown();
    }
}

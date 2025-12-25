package com.finpro.frontend.strategy.shooting;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.manager.BulletManager;

public class RadialShot extends AttackStrategy {
    public RadialShot() {
        super(0.3f);
    }

    @Override
    public void shoot(BulletManager bulletManager, Player player, Vector2 dir) {
        final int NO_OF_BULLETS = 8;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(new Vector2(1, 0), 0f);
        Vector2 bulletDir = new Vector2();

        for (int i = 0; i < NO_OF_BULLETS; i++) {
            float angle = i * (360f / NO_OF_BULLETS);
            bulletDir.set(1, 0).rotateDeg(angle);
            bulletManager.spawn(spawnPos, bulletDir);
        }

        resetCooldown();
    }
}

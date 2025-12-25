package com.finpro.frontend.strategy.shooting;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.manager.BulletManager;

public class BurstShot extends AttackStrategy {
    public BurstShot() {
        super(0.1f);
    }

    @Override
    public void shoot(BulletManager bulletManager, Player player, Vector2 dir) {
        final float OFFSET = 20f;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(dir, OFFSET);
        bulletManager.spawn(spawnPos, dir);

        resetCooldown();
    }
}

package com.finpro.frontend.strategy.shooting;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.manager.BulletManager;

public class SingleShot extends AttackStrategy {
    public SingleShot() {
        super(0.5f);
    }

    @Override
    public void shoot(BulletManager bulletManager, Player player, Vector2 dir) {
        final float OFFSET = 10f;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(dir, OFFSET);
        bulletManager.spawn(spawnPos, dir);

        resetCooldown();
    }
}

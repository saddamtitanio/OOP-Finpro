package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.factory.BulletFactory;

public class BurstShot extends AttackStrategy {
    public BurstShot() {
        super(0.1f);
    }

    @Override
    public void shoot(BulletFactory bulletFactory, Player player, Vector2 dir) {
        final float OFFSET = 20f;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(dir, OFFSET);
        bulletFactory.create(spawnPos, dir);

        resetCooldown();
    }
}

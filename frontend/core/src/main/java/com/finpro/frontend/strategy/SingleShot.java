package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.factory.BulletFactory;

public class SingleShot extends AttackStrategy {
    public SingleShot() {
        super(0.5f);
    }

    @Override
    public void shoot(BulletFactory bulletFactory, Player player, Vector2 dir) {
        final float OFFSET = 10f;

        if (!canShoot()) return;

        Vector2 spawnPos = player.getBulletSpawnPos(dir, OFFSET);
        bulletFactory.create(spawnPos, dir);

        resetCooldown();
    }
}

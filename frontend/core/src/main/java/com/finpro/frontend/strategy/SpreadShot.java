package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.factory.BulletFactory;

public class SpreadShot implements AttackStrategy {
    @Override
    public void shoot(BulletFactory bulletFactory, Vector2 pos, Vector2 dir) {
        float centerX = pos.x;
        float centerY = pos.y;

        Vector2 playerCenter = new Vector2(centerX, centerY);
        Vector2 offsetVector = new Vector2(dir);
        Vector2 spawnPos = playerCenter.add(offsetVector);

        bulletFactory.create(spawnPos, dir);
    }
}

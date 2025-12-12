package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.factory.BulletFactory;

public class SingleShot implements AttackStrategy {
    private final float playerWidth;
    private final float playerHeight;
    private final float OFFSET = 30f;

    public SingleShot(float playerWidth, float playerHeight) {
        this.playerWidth = playerWidth;
        this.playerHeight = playerHeight;
    }

    @Override
    public void shoot(BulletFactory bulletFactory, Vector2 pos, Vector2 dir) {
        Vector2 spawnPos = new Vector2(
            pos.x + playerWidth / 2f + dir.x * OFFSET,
            pos.y + playerHeight / 2f + dir.y * OFFSET
        );
        bulletFactory.create(spawnPos, dir);
    }
}

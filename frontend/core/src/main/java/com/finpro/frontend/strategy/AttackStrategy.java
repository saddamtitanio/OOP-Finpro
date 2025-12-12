package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.factory.BulletFactory;

public interface AttackStrategy {
    void shoot(BulletFactory bulletFactory, Vector2 pos, Vector2 dir);
}

package com.finpro.frontend.factory;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Bullet;
import com.finpro.frontend.pool.BulletPool.BulletPool;

public class BulletFactory {
    private final BulletPool bulletPool;

    public BulletFactory(BulletPool bulletPool) {
        this.bulletPool = bulletPool;
    }

    public Bullet create(Vector2 pos, Vector2 dir) {
        Bullet bullet = bulletPool.obtain();
        bullet.initialize(pos, dir);
        return bullet;
    }
}

package com.finpro.frontend.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.Bullet;
import com.finpro.frontend.pool.BulletPool.BulletPool;

public class BulletManager {
    private final BulletPool pool;
    private final Array<Bullet> active = new Array<>();

    public BulletManager(BulletPool pool) {
        this.pool = pool;
    }

    public void update(float delta, TileManager tileManager) {
        for (int i = active.size - 1; i >= 0; i--) {
            Bullet bullet = active.get(i);
            bullet.update(delta);

            if (bullet.isOffScreen(tileManager)) {
                destroy(i);
            }
        }
    }

    public void spawn(Vector2 pos, Vector2 vel) {
        Bullet bullet = pool.obtain();
        bullet.initialize(pos, vel);
        active.add(bullet);
    }

    public void destroy(int index) {
        Bullet bullet = active.removeIndex(index);
        pool.release(bullet);
    }

    public Array<Bullet> getActive() {
        return active;
    }
}

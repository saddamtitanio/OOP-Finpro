package com.finpro.frontend.pool.BulletPool;

import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.Bullet;
import com.finpro.frontend.pool.ObjectPool;

public class BulletPool extends ObjectPool<Bullet> {
    @Override
    protected Bullet createObject() {
        return new Bullet();
    }

    @Override
    protected void resetObject(Bullet bullet) {
        bullet.reset();
    }

    public Bullet obtain() {
        Bullet bullet = super.obtain();
        bullet.setActive(true);
        return bullet;
    }

    public void getActiveBullets(Array<Bullet> activeBullets) {
        activeBullets.clear();
        Array<Bullet> inUse = getInUse();

        for (int i = 0; i < inUse.size; i++) {
            Bullet bullet = inUse.get(i);
            if (bullet.isActive()) {
                activeBullets.add(bullet);
            }
        }
    }
}

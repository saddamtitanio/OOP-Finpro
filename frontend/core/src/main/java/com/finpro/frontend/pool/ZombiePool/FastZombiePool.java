package com.finpro.frontend.pool.ZombiePool;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.FastZombie;

public class FastZombiePool extends ZombiePool<FastZombie> {
    @Override
    protected FastZombie createZombie() {
        return new FastZombie(new Vector2());
    }

    @Override
    protected void resetZombie(FastZombie zombie) {
        zombie.setTarget(null);
        zombie.setPosition(0,0);
        zombie.setActive(false);
        zombie.setHp(10);
        zombie.setVelocity(0,0);
    }

    public FastZombie obtain(Vector2 position) {
        FastZombie fastZombie = super.obtain();
        fastZombie.initialize(position);
        fastZombie.setActive(true);
        return fastZombie;
    }
}

package com.finpro.frontend.pool;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.BasicZombie;


public class BasicZombiePool extends ZombiePool<BasicZombie> {

    @Override
    protected BasicZombie createZombie() {
        return new BasicZombie(new Vector2());
    }

    @Override
    protected void resetZombie(BasicZombie zombie) {
        zombie.setTarget(null);
        zombie.setPosition(0,0);
        zombie.setActive(false);
        zombie.setHp(10);
        zombie.setVelocity(0,0);
    }

    public BasicZombie obtain(Vector2 position) {
        BasicZombie zombie = super.obtain();
        zombie.initialize(position);
        zombie.setActive(true);
        return zombie;
    }
}

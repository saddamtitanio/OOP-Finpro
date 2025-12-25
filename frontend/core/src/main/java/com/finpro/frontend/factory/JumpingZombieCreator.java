package com.finpro.frontend.factory;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.enemies.JumpingZombie;
import com.finpro.frontend.factory.ZombieFactory.ZombieCreator;
import com.finpro.frontend.pool.ZombiePool.JumpingZombiePool;

import java.util.List;

public class JumpingZombieCreator implements ZombieCreator {
    private final JumpingZombiePool pool = new JumpingZombiePool();

    @Override
    public BaseZombie create(float x, float y) {
        return pool.obtain(new Vector2(x,y));
    }

    @Override
    public void release(BaseZombie zombie) {
        if (zombie instanceof JumpingZombie) pool.release((JumpingZombie) zombie);
    }

    @Override
    public void releaseAll() {
        pool.releaseAll();
    }

    @Override
    public List<? extends BaseZombie> getInUse() {
        return pool.getInUse();
    }

    @Override
    public boolean supports(BaseZombie zombie) {
        return zombie instanceof JumpingZombie;
    }

    @Override
    public String getName() {
        return "JumpingZombie";
    }
}

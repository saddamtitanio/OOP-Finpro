package com.finpro.frontend.factory;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.BaseZombie;
import com.finpro.frontend.enemies.BasicZombie;
import BasicZombiePool;
import FastZombiePool;
import com.finpro.frontend.factory.ZombieFactory.ZombieCreator;

import java.util.List;

public class BasicZombieCreator implements ZombieCreator {
    private final BasicZombiePool pool = new BasicZombiePool();

    @Override
    public BaseZombie create(float x, float y) {
        return pool.obtain(new Vector2(x,y));
    }

    @Override
    public void release(BaseZombie zombie) {
        if (zombie instanceof BasicZombie) pool.release((BasicZombie) zombie);
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
        return zombie instanceof BasicZombie;
    }

    @Override
    public String getName() {
        return "BasicZombie";
    }
}

package com.finpro.frontend.factory;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.BaseZombie;

import java.util.List;

import com.finpro.frontend.enemies.FastZombie;
import com.finpro.frontend.factory.ZombieFactory.ZombieCreator;
import FastZombiePool;

public class FastZombieCreator implements ZombieCreator {
    private final FastZombiePool pool = new FastZombiePool();

    @Override
    public BaseZombie create(float x, float y) {
        return pool.obtain(new Vector2(x,y));
    }

    @Override
    public void release(BaseZombie zombie) {
        if (zombie instanceof FastZombie) pool.release((FastZombie) zombie);
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
        return zombie instanceof FastZombie;
    }

    @Override
    public String getName() {
        return "FastZombie";
    }
}

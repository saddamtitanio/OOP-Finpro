package com.finpro.frontend.pool.BossPool;

import com.finpro.frontend.obstacle.BossObstacle.BossAreaAttackObstacle;
import com.finpro.frontend.pool.ObjectPool;

public class BossAreaAttackPool extends ObjectPool<BossAreaAttackObstacle> {

    public BossAreaAttackPool() {
        super();
    }

    @Override
    protected BossAreaAttackObstacle createObject() {
        return new BossAreaAttackObstacle();
    }

    @Override
    protected void resetObject(BossAreaAttackObstacle attack) {
        attack.reset();
    }
}

package com.finpro.frontend.manager.BossManager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.obstacle.BossObstacle.BaseBossAttack;
import com.finpro.frontend.obstacle.BossObstacle.BossAreaAttackObstacle;
import com.finpro.frontend.obstacle.BossObstacle.BossLaserAttackObstacle;
import com.finpro.frontend.pool.BossPool.BossAreaAttackPool;

import java.util.ArrayList;
import java.util.List;

public class BossAttackManager {
    private final List<BaseBossAttack> baseBossAttacks;
    private final BossAreaAttackPool areaAttackPool;
    private ShapeRenderer shapeRenderer;


    public BossAttackManager(ShapeRenderer shapeRenderer){
        this.baseBossAttacks = new ArrayList<>();
        this.areaAttackPool = new BossAreaAttackPool();
        this.shapeRenderer = shapeRenderer;
    }

    public void createAreaAttack(Vector2 position){
        if(position == null){
            return;
        }

        BossAreaAttackObstacle areaAttackObstacle = areaAttackPool.obtain();
        if(areaAttackObstacle != null){
            areaAttackObstacle.activate(position);
            baseBossAttacks.add(areaAttackObstacle);
        }
    }

    public void createLaserAttack(Vector2 position){
        if(position == null){
            return;
        }

        BossLaserAttackObstacle laserAttackObstacle = new BossLaserAttackObstacle();
        laserAttackObstacle.activate(position);
        baseBossAttacks.add(laserAttackObstacle);
    }

    public void update(float delta){
        for(int i = baseBossAttacks.size() - 1; i >= 0; i--){
            BaseBossAttack baseBossAttack = baseBossAttacks.get(i);
            if(baseBossAttack != null){
                baseBossAttack.update(delta);

                if(baseBossAttack.isComplete()){
                    releaseAttack(baseBossAttack);
                    baseBossAttacks.remove(i);
                }
            }
        }
    }

    private void releaseAttack(BaseBossAttack baseBossAttack){
        if(baseBossAttack instanceof BossAreaAttackObstacle){
            areaAttackPool.release((BossAreaAttackObstacle) baseBossAttack);
        }
    }

    public void render(){
        if(shapeRenderer == null){
            return;
        }
        for(BaseBossAttack baseBossAttack : baseBossAttacks){
            baseBossAttack.render(shapeRenderer);
        }
    }

    public List<BaseBossAttack> getActiveAttacks() {
        return baseBossAttacks;
    }
}

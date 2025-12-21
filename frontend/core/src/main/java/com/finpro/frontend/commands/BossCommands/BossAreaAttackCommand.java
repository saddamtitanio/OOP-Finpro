package com.finpro.frontend.commands.BossCommands;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;
import com.finpro.frontend.state.BossState.BossNormalState;

public class BossAreaAttackCommand implements BossCommand{
    private float cooldown = 0;
    private Vector2 targetPosition;

    public void setTargetPosition(Vector2 position){
        if(position != null){
            this.targetPosition = position;
        }
    }

    @Override
    public void execute(Boss boss) {
        if(targetPosition != null){
            boss.createAreaAttack(targetPosition);
            cooldown = 7.5f;
        }
    }

    @Override
    public boolean isExecutable(Boss boss) {
        return cooldown <= 0 &&
            targetPosition != null &&
            boss.getCurrentBehavior() instanceof BossNormalState;
    }

    @Override
    public void update(float delta) {
        if(cooldown > 0){
            cooldown -= delta;
        }
    }

}

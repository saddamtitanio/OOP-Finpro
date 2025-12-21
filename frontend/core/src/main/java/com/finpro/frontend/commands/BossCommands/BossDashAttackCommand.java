package com.finpro.frontend.commands.BossCommands;

import com.finpro.frontend.Boss;
import com.finpro.frontend.state.BossState.BossDashingState;
import com.finpro.frontend.state.BossState.BossNormalState;

public class BossDashAttackCommand implements BossCommand{
    private float cooldown = 0;

    @Override
    public void execute(Boss boss) {
        boss.setBehavior(new BossDashingState());
        cooldown = 9f;
    }

    @Override
    public boolean isExecutable(Boss boss) {
        return cooldown <= 0 &&
            boss.getCurrentBehavior() instanceof BossNormalState;
    }

    @Override
    public void update(float delta) {
        if(cooldown > 0){
            cooldown -= delta;
        }
    }

}

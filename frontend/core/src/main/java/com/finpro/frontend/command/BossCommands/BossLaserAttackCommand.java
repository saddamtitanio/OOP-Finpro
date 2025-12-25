package com.finpro.frontend.command.BossCommands;

import com.finpro.frontend.Boss;
import com.finpro.frontend.state.BossState.BossLaserState;
import com.finpro.frontend.state.BossState.BossNormalState;

public class BossLaserAttackCommand implements BossCommand{
    private float cooldown = 0;

    @Override
    public void execute(Boss boss) {
        boss.setBehavior(new BossLaserState());
        cooldown = 15f;
    }

    @Override
    public boolean isExecutable(Boss boss) {
        return cooldown <= 0 &&
            boss.getCurrentBehavior() instanceof BossNormalState;
    }

    @Override
    public void update(float delta){
        if(cooldown > 0){
            cooldown -= delta;
        }
    }

}

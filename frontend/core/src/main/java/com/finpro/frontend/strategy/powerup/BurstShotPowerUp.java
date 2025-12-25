package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;
import com.finpro.frontend.strategy.shooting.BurstShot;
import com.finpro.frontend.strategy.shooting.SingleShot;

public class BurstShotPowerUp extends PowerUp {
    public BurstShotPowerUp(float duration) {
        super(duration);
    }

    @Override
    public void apply(Player player) {
        player.setAttackStrategy(new BurstShot());
    }

    @Override
    public void deactivate(Player player) {
        player.setAttackStrategy(new SingleShot());
        super.deactivate(player);
    }
}

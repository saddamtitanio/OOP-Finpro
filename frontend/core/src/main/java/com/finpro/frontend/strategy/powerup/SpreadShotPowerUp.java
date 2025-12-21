package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;
import com.finpro.frontend.strategy.SingleShot;
import com.finpro.frontend.strategy.SpreadShot;

public class SpreadShotPowerUp extends PowerUp {
    public SpreadShotPowerUp(float duration) {
        super(duration);
    }

    @Override
    public void apply(Player player) {
        player.setAttackStrategy(new SpreadShot());
    }

    @Override
    public void deactivate(Player player) {
        player.setAttackStrategy(new SingleShot());
        super.deactivate(player);
    }
}

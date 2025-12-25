package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;
import com.finpro.frontend.strategy.shooting.RadialShot;
import com.finpro.frontend.strategy.shooting.SingleShot;

public class RadialShotPowerUp extends PowerUp {
    public RadialShotPowerUp(float duration) {
        super(duration);
    }

    @Override
    public void apply(Player player) {
        player.setAttackStrategy(new RadialShot());
    }

    @Override
    public void deactivate(Player player) {
        player.setAttackStrategy(new SingleShot());
        super.deactivate(player);
    }
}

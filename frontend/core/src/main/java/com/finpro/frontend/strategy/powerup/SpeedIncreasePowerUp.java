package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;

public class SpeedIncreasePowerUp extends PowerUp {
    public SpeedIncreasePowerUp(float duration) {
        super(duration);
    }

    @Override
    public void apply(Player player) {
        player.setSpeed(2f);
    }

    @Override
    public void deactivate(Player player) {
        player.setSpeed(1f);
        super.deactivate(player);
    }
}

package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;

public class HealthPowerUp extends PowerUp {
    public HealthPowerUp(float duration) {
        super(duration);
    }

    @Override
    public void apply(Player player) {
        player.addHP(30);
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}

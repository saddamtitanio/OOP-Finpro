package com.finpro.frontend.strategy.powerup;

import com.finpro.frontend.Player;

public abstract class PowerUp {
    protected float duration;
    protected float timer = 0f;
    protected boolean expired = false;

    public PowerUp(float duration) {
        this.duration = duration;
    }

    public abstract void apply(Player player);

    public void deactivate(Player player) {
        expired = true;
    }

    public void update(Player player, float deltaTime) {
        timer += deltaTime;
        if (timer >= duration && !expired) {
            deactivate(player);
        }
    }

    public boolean isExpired() {
        return expired;
    }
}

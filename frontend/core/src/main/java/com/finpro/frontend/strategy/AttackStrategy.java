package com.finpro.frontend.strategy;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.factory.BulletFactory;

public abstract class AttackStrategy {
    protected float fireCooldown;
    protected float timeSinceLastShot = 0f;

    public AttackStrategy(float fireCooldown) {
        this.fireCooldown = fireCooldown;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canShoot() {
        return timeSinceLastShot >= fireCooldown;
    }

    protected void resetCooldown() {
        timeSinceLastShot = 0f;
    }

    public abstract void shoot(BulletFactory bulletFactory, Player player, Vector2 dir);
}

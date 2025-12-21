package com.finpro.frontend.strategy.zombie.movement;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.enemies.BaseZombie;

public class SprintZombieMovementStrategy implements ZombieMovementStrategy {
    private static final float SPRINT_SPEED = 450f;

    @Override
    public void move(BaseZombie zombie, Player target, float delta) {

        Vector2 pos = zombie.getPosition();
        Vector2 vel = zombie.getVelocity();

        vel.set(target.getPosition()).sub(pos).nor().scl(SPRINT_SPEED);

    }
}



package com.finpro.frontend.strategy.zombie.movement;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.enemies.BaseZombie;

public class WalkZombieMovementStrategy implements ZombieMovementStrategy {


    @Override
    public void move(BaseZombie zombie, Player target, float delta) {
        float speed = zombie.getBASE_SPEED();

        Vector2 vel = zombie.getVelocity();

        vel.set(target.getPosition())
            .sub(zombie.getPosition())
            .nor()
            .scl(speed);
    }
}



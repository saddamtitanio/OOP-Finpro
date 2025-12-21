package com.finpro.frontend.strategy.zombie.movement;

import com.finpro.frontend.Player;
import com.finpro.frontend.enemies.BaseZombie;

public interface ZombieMovementStrategy {
    void move(BaseZombie zombie, Player target, float delta);
}

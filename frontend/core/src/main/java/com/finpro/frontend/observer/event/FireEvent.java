package com.finpro.frontend.observer.event;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.strategy.shooting.AttackStrategy;

public class FireEvent {
    public final Player player;
    public final Vector2 direction;
    public final AttackStrategy strategy;

    public FireEvent(Player player, Vector2 direction, AttackStrategy strategy) {
        this.player = player;
        this.direction = direction;
        this.strategy = strategy;
    }
}

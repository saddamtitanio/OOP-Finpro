package com.finpro.frontend.observer.event;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.strategy.AttackStrategy;

public class FireEvent {
    public final Vector2 position;
    public final Vector2 direction;
    public final AttackStrategy strategy;

    public FireEvent(Vector2 position, Vector2 direction, AttackStrategy strategy) {
        this.position = new Vector2(position);
        this.direction = new Vector2(direction);
        this.strategy = strategy;
    }
}

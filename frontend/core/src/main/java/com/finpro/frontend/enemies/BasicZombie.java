package com.finpro.frontend.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.strategy.zombie.movement.WalkZombieMovementStrategy;

public class BasicZombie extends BaseZombie {

    private final WalkZombieMovementStrategy walkStrategy = new WalkZombieMovementStrategy();

    public BasicZombie(Vector2 startPosition) {
        super(startPosition);
        this.hp = 5;
        this.BASE_SPEED = 100f;
        setMovementStrategy(walkStrategy);
    }

    @Override
    public void initialize(Vector2 startPosition) {
        super.initialize(startPosition);
        this.hp = 5;
        this.BASE_SPEED = 100f;
        setMovementStrategy(walkStrategy);
    }

    @Override
    public void update(float delta) {
        if (!active || target == null) return;

        movementStrategy.move(this, target, delta);
        updateCollider();
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, position.y, width, height);
    }
}

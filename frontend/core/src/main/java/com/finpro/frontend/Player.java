package com.finpro.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.strategy.AttackStrategy;
import com.finpro.frontend.strategy.SingleShot;

import java.util.Stack;

public class Player {
    private Vector2 position;
    private Rectangle collider;
    private Vector2 velocity;
    private final float BASE_SPEED = 250f;
    private float height = 32f;
    private float width = 32f;

    private float fireCooldown = 0f;
    private float fireDelay = 0.3f;

    private EventManager eventManager;

    private AttackStrategy attackStrategy = new SingleShot(width, height);
    private Stack<AttackStrategy> strategyStack = new Stack<>();

    private final boolean hasPowerUp = false;
    private final float powerUpDuration = 5f;
    private float powerUpElapsedTime = 0f;

    // In case we still wanna do the inverted movement mechanic
    private final int MOVEMENT_DIRECTION = 1;

    public Player(Vector2 startPosition, EventManager eventManager) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(0, 0);
        this.eventManager = eventManager;
        this.strategyStack.push(attackStrategy);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    public AttackStrategy getCurrentStrategy() {
        return strategyStack.peek();
    }

    public void applyTemporaryStrategy(AttackStrategy tempStrategy) {
        System.out.println("POWERUP");
        powerUpElapsedTime = powerUpDuration;
        strategyStack.push(tempStrategy);
    }

    public void update(float delta) {
//        if (powerUpElapsedTime > 0f) {
//            powerUpElapsedTime -= delta;
//
//            if (powerUpElapsedTime <= 0f && strategyStack.size() > 1) {
//                strategyStack.pop();
//            }
//        }
        updatePosition(delta);
        fireCooldown -= delta;
    }

    private void updatePosition(float delta) {
        velocity.nor().scl(BASE_SPEED);

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        // reset velocity every frame
        velocity.set(0, 0);
    }


    public void move(Vector2 dir) {
        velocity.add(dir.x * MOVEMENT_DIRECTION, dir.y * MOVEMENT_DIRECTION);
    }

    public void fire(Vector2 bulletDir) {
        if (fireCooldown > 0) return;
        fireCooldown = fireDelay;

        FireEvent fireEvent = new FireEvent(position, bulletDir, attackStrategy);
        eventManager.notify(fireEvent);
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
}

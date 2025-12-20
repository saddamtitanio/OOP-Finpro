package com.finpro.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.strategy.*;
import com.finpro.frontend.strategy.powerup.PowerUp;

import java.util.Stack;

public class Player {
    private WorldBounds bounds;

    private final Vector2 position;
    private final Rectangle collider;
    private final Vector2 velocity;
    private final float BASE_SPEED = 250f;
    private final float HEIGHT = 16f;
    private final float WIDTH = 16f;
    private PowerUp activePowerUp;
    private PowerUp storedPowerUp;

    private EventManager eventManager;

    private AttackStrategy attackStrategy;
    private Stack<AttackStrategy> strategyStack = new Stack<>();

    private final boolean hasPowerUp = false;
    private final float powerUpDuration = 5f;
    private float powerUpElapsedTime = 0f;

    // In case we still wanna do the inverted movement mechanic
    private final int MOVEMENT_DIRECTION = 1;

    public Player(Vector2 startPosition, EventManager eventManager, WorldBounds worldBounds) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(0, 0);
        this.eventManager = eventManager;
        this.attackStrategy = new SingleShot();
        this.strategyStack.push(attackStrategy);
        this.collider = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        this.bounds = worldBounds;
    }

    public void gainPowerUp(PowerUp powerUp) {
        if (storedPowerUp == null && activePowerUp == null) {
            storedPowerUp = powerUp;
        } else {
            equipPowerUp(powerUp);
        }
    }

    public void activatePowerUp() {
        if (storedPowerUp == null) return;

        if (activePowerUp != null) {
            activePowerUp.deactivate(this);
        }

        activePowerUp = storedPowerUp;
        storedPowerUp = null;
        activePowerUp.apply(this);

        System.out.println("TEST");
    }

    private void equipPowerUp(PowerUp powerUp) {
        // deactivate any active power up
        if (activePowerUp != null) {
            activePowerUp.deactivate(this);
        }

        // equip the newly obtained power-up
        activePowerUp = powerUp;
        activePowerUp.apply(this);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        updatePosition(delta);
        attackStrategy.update(delta);
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
        FireEvent fireEvent = new FireEvent(this, bulletDir, attackStrategy);
        eventManager.notify(fireEvent);
    }

    public Vector2 getBulletSpawnPos(Vector2 dir, float offset) {
        return new Vector2(
            position.x + getWidth() / 2f + dir.x * offset,
            position.y + getHeight() / 2f + dir.y * offset
        );
    }

    public float getHeight() {
        return HEIGHT;
    }

    public float getWidth() {
        return WIDTH;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void setSpeed(float multiplier) {
        velocity.scl(multiplier);
    }

    public Vector2 getPosition() {
        return position;
    }
}

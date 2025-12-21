package com.finpro.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.strategy.*;
import com.finpro.frontend.strategy.powerup.PowerUp;

import java.util.Stack;

public class Player {
    private final Vector2 position;
    private final Rectangle collider;
    private final Vector2 velocity;
    private float HP = 40f;
    private final float BASE_SPEED = 250f;
    private final float HEIGHT = 24f;
    private final float WIDTH = 24f;
    private PowerUp activePowerUp;
    private PowerUp storedPowerUp;

    private EventManager eventManager;

    private AttackStrategy attackStrategy;

    private float speedMultiplier = 1f; // default 1x

    // In case we still wanna do the inverted movement mechanic
    private final int MOVEMENT_DIRECTION = 1;

    private float activePowerUpDuration = 0f;

    public Player(Vector2 startPosition, EventManager eventManager) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(0, 0);
        this.eventManager = eventManager;
        this.attackStrategy = new SingleShot();
        this.collider = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }

    public void gainPowerUp(PowerUp newPowerUp) {
        if (newPowerUp.isInstant()) {
            equipPowerUp(newPowerUp);
            return;
        }

        if (storedPowerUp == null) {
            storedPowerUp = newPowerUp;
            System.out.println("Stored power-up: " + newPowerUp.getClass().getSimpleName());
        } else {
            activePowerUp = newPowerUp;
            activePowerUpDuration = 0f;
            equipPowerUp(activePowerUp);
        }
    }

    public void activateStoredPowerUp() {
        if (storedPowerUp == null) return;

        equipPowerUp(storedPowerUp);
        storedPowerUp = null;

        activePowerUpDuration = 0f;
    }

    private void equipPowerUp(PowerUp newPowerUp) {
        System.out.println("Equipping power-up: " + newPowerUp.getClass().getSimpleName());

        if (!newPowerUp.isInstant() && activePowerUp != null) {
            activePowerUp.deactivate(this);
            activePowerUp = newPowerUp;
            activePowerUpDuration = 0f;
            activePowerUp.apply(this);
        } else if (newPowerUp.isInstant()) {
            newPowerUp.apply(this);
        } else {
            activePowerUp = newPowerUp;
            activePowerUpDuration = 0f;
            activePowerUp.apply(this);
        }
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void update(float delta) {
        updatePosition(delta);
        attackStrategy.update(delta);

        if (activePowerUp != null && !activePowerUp.isInstant()) {
            activePowerUpDuration += delta;
            if (activePowerUpDuration >= activePowerUp.getDuration()) {
                deactivatePowerUp();
            }
        }
    }

    public void deactivatePowerUp() {
        if (activePowerUp != null) {
            activePowerUp.deactivate(this);
            activePowerUp = null;
            activePowerUpDuration = 0f;
        }
    }

    private void updatePosition(float delta) {
        velocity.nor().scl(BASE_SPEED * speedMultiplier);

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        updateCollider();

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
        this.speedMultiplier = multiplier;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getCollider() {
        return collider;
    }

    public void updateCollider() {
        collider.setPosition(position);
    }

    public void addHP(float amount) {
        System.out.println("Before HP: " + this.HP);
        this.HP = MathUtils.clamp(this.HP + amount, 0, 100);
        System.out.println("After HP: " + this.HP);
    }

    public void takeDamage(float amount) {
        this.HP -= amount;
    }

    public float getHP() {
        return HP;
    }


}

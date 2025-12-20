package com.finpro.frontend.strategy.powerup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PowerUpEntity {
    private PowerUp powerUp;
    private final Vector2 position = new Vector2();
    private final Rectangle collider = new Rectangle();
    private boolean active;

    private float lifetime;
    private float elapsed;
    private boolean expired;

    public static final float SIZE = 24f;

    public void init(PowerUp powerUp, Vector2 spawnPos) {
        this.powerUp = powerUp;
        this.position.set(spawnPos);
        this.collider.set(spawnPos.x, spawnPos.y, SIZE, SIZE);

        this.lifetime = powerUp.getDuration();
        this.elapsed = 0f;
        this.expired = false;
        this.active = true;
    }

    public void update(float delta) {
        elapsed += delta;
        updateCollider();

        if (elapsed >= lifetime) {
            expired = true;
        }
    }

    public Rectangle getCollider() {
        return collider;
    }

    private void updateCollider() {
        collider.setPosition(position);
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void reset() {
        powerUp = null;
        active = false;
        elapsed = 0f;
        lifetime = 0f;
        expired = false;
    }

    public boolean isExpired() {
        return expired;
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        if (!active) return;

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(position.x, position.y, SIZE, SIZE);
    }

    public float getSize() {
        return SIZE;
    }
}



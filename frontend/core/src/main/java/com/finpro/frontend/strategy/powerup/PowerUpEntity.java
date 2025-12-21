package com.finpro.frontend.strategy.powerup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private boolean visible = true;
    private float blinkTimer = 0f;

    public static final float SIZE = 24f;
    private static final float BLINK_THRESHOLD = 3f;
    private static final float BLINK_INTERVAL = 0.2f;

    private Texture texture;


    public void init(PowerUp powerUp, Vector2 spawnPos) {
        this.powerUp = powerUp;
        this.position.set(spawnPos);
        this.collider.set(spawnPos.x, spawnPos.y, SIZE, SIZE);

        this.lifetime = powerUp.getDuration();
        this.elapsed = 0f;
        this.expired = false;
        this.active = true;

        this.visible = true;
        this.blinkTimer = 0f;
    }

    public void update(float delta) {
        elapsed += delta;
        updateCollider();

        if (elapsed >= lifetime) {
            expired = true;
            active = false;
        }

        float timeLeft = lifetime - elapsed;
        if (timeLeft <= BLINK_THRESHOLD) {
            blinkTimer += delta;
            if (blinkTimer >= BLINK_INTERVAL) {
                visible = !visible;
                blinkTimer = 0f;
            }
        }
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        if (!active || !visible) return;

        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(position.x, position.y, SIZE, SIZE);
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
        visible = true;
        blinkTimer = 0f;
    }

    public boolean isExpired() {
        return expired;
    }
}

package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private boolean active = false;
    private final float BULLET_SPEED = 600f;
    private final float BULLET_RADIUS = 5f;

    public void initialize(Vector2 pos, Vector2 dir) {
        position.set(pos);
        velocity.set(dir).scl(BULLET_SPEED);
        active = true;
    }

    public void update(float delta) {
        if (!active) return;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void reset() {
        this.active = false;
        velocity.set(0, 0);
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return BULLET_RADIUS;
    }

    public boolean isOffScreen() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float leftCircleEdge = position.x - BULLET_RADIUS;
        float rightCircleEdge = position.x + BULLET_RADIUS;

        float bottomCircleEdge = position.y - BULLET_RADIUS;
        float topCircleEdge = position.y + BULLET_RADIUS;

        boolean outsideHorizontal = rightCircleEdge < 0 || leftCircleEdge > screenWidth;
        boolean outsideVertical = topCircleEdge < 0 || bottomCircleEdge > screenHeight;

        return (outsideHorizontal || outsideVertical);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(position.x, position.y, BULLET_RADIUS);
    }
}

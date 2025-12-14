package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private final WorldBounds bounds;

    private final Vector2 position;
    private final Vector2 velocity = new Vector2();
    private final Rectangle collider;

    private final float BASE_SPEED = 300f;
    private final float width = 32f;
    private final float height = 32f;

    public Player(Vector2 startPosition, WorldBounds worldBounds) {
        this.position = new Vector2(startPosition);
        this.collider = new Rectangle(position.x, position.y, width, height);
        this.bounds = worldBounds;
    }

    public void update(float delta) {
        readInput();
        applyMovement(delta);
    }

    private void readInput() {
        velocity.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) velocity.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) velocity.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) velocity.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) velocity.x -= 1;

        if (!velocity.isZero()) {
            velocity.nor().scl(BASE_SPEED);
        }
    }

    private void applyMovement(float delta) {
        // ---- X axis ----
        Vector2 nextX = new Vector2(
            position.x + velocity.x * delta,
            position.y
        );

        Rectangle xCollider = new Rectangle(
            nextX.x, nextX.y, width, height
        );

        if (!bounds.collides(xCollider)) {
            position.x = nextX.x;
        } else {
            velocity.x = 0;
        }

        // ---- Y axis ----
        Vector2 nextY = new Vector2(
            position.x,
            position.y + velocity.y * delta
        );

        Rectangle yCollider = new Rectangle(
            nextY.x, nextY.y, width, height
        );

        if (!bounds.collides(yCollider)) {
            position.y = nextY.y;
        } else {
            velocity.y = 0;
        }

        collider.setPosition(position);
    }

    public void renderShape(ShapeRenderer renderer) {
        renderer.setColor(Color.WHITE);
        renderer.rect(position.x, position.y, width, height);
    }

    public Rectangle getCollider() {
        return collider;
    }

    public Vector2 getPosition() {
        return position;
    }
}

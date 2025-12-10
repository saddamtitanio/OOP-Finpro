package com.finpro.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private Rectangle collider;
    private Vector2 velocity;
    private final float BASE_SPEED = 300f;
    private float height = 64f;
    private float width = 64f;

    // In case we still wanna do the inverted movement mechanic
    private final int MOVEMENT_DIRECTION = 1;

    public Player(Vector2 startPosition) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(0, 0);
    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    public void update(float delta) {
        updatePosition(delta);
    }

    private void updatePosition(float delta) {
        velocity.nor().scl(BASE_SPEED);

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        // reset velocity every frame
        velocity.set(0, 0);
    }
    public void moveRight() {
        velocity.x += MOVEMENT_DIRECTION;
    }

    public void moveLeft() {
        velocity.x -= MOVEMENT_DIRECTION;
    }

    public void moveUp() {
        velocity.y += MOVEMENT_DIRECTION;
    }

    public void moveDown() {
        velocity.y -= MOVEMENT_DIRECTION;
    }
}

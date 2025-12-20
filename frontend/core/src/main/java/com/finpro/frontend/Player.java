package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private Rectangle collider;
    private Vector2 velocity;
    private final float BASE_SPEED = 300f;
    private float height = 30f;
    private float width = 30f;
    private Texture texture;

    public Player(Vector2 startPosition) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(BASE_SPEED, BASE_SPEED);
//        this.texture = new Texture("player.png");
    }

//    public void render(SpriteBatch spriteBatch) {
//        spriteBatch.draw(texture, position.x, position.y, width, height);
//    }

    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    public void update(float delta) {
        updatePosition(delta);
    }

    private void updatePosition(float delta) {
        Vector2 direction = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y++;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y--;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x++;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x--;

        if (!direction.isZero()) {
            direction.nor();
            position.mulAdd(direction, BASE_SPEED * delta);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}

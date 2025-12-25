package com.finpro.frontend.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.WorldBounds;
import com.finpro.frontend.strategy.zombie.attack.ZombieAttackStrategy;
import com.finpro.frontend.strategy.zombie.movement.ZombieMovementStrategy;

public abstract class BaseZombie {

    protected Vector2 position;
    protected Rectangle collider;
    protected Vector2 velocity;
    protected boolean active = false;

    protected float width = 32f;
    protected float height = 32f;

    protected int hp = 10; // default hp???? idk tbh saddam never made the damage
    protected float BASE_SPEED = 100f;

    protected Player target;

    protected ZombieAttackStrategy attackStrategy;
    protected ZombieMovementStrategy movementStrategy;

    public BaseZombie(Vector2 startPosition ){
        this.position = startPosition;
        this.velocity = new Vector2();
        updateCollider();
    }

    public void initialize(Vector2 startPosition) {
        this.position.set(startPosition);
        this.velocity.set(0,0);
        updateCollider();
    }

    public void setAttackStrategy(ZombieAttackStrategy strategy) {
        this.attackStrategy = strategy;
    }

    public void setMovementStrategy(ZombieMovementStrategy strategy){
        this.movementStrategy = strategy;
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (!active) return;
        drawShape(shapeRenderer);
    }

    public boolean isColliding(Rectangle playerCollider) {
        return active && collider.overlaps(playerCollider);
    }


    public abstract void update(float delta);
    protected abstract void updateCollider();
    protected abstract void drawShape(ShapeRenderer renderer);


    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Vector2 getPosition() { return position; }
    public void setPosition(float x, float y){
        this.position.set(x,y);
        updateCollider();
    }

    public Vector2 getVelocity(){return velocity;}
    public void setVelocity(float x, float y){velocity.set(x,y);}

    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getHp(){return hp;}

    public float getBASE_SPEED(){
        return BASE_SPEED;
    }
    public void setBASE_SPEED(float newSpeed){
        BASE_SPEED = newSpeed;
    }

    public void setTarget(Player target){
        this.target = target;
    }

    public float getWidth(){
        return width;
    }
    public void setWidth(float width){
        this.width = width;
    }

    public void syncCollider(){updateCollider();}

    public boolean canCollide(){
        return true;
    }

    public Rectangle getCollider(){
        return collider;
    }

    public void moveWithBounds(float delta, WorldBounds worldBounds) {
        Vector2 nextX = new Vector2(position.x + velocity.x * delta, position.y);
        Rectangle xCollider = new Rectangle(nextX.x, nextX.y, width, height);

        if (!worldBounds.collides(xCollider)) {
            position.x = nextX.x;
        } else {
            velocity.x = 0;
        }

        Vector2 nextY = new Vector2(position.x, position.y + velocity.y * delta);
        Rectangle yCollider = new Rectangle(nextY.x, nextY.y, width, height);

        if (!worldBounds.collides(yCollider)) {
            position.y = nextY.y;
        } else {
            velocity.y = 0;
        }

        syncCollider();
    }



}

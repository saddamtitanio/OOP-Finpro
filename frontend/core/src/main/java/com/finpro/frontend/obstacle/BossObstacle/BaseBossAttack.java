package com.finpro.frontend.obstacle.BossObstacle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseBossAttack {
    protected boolean active;
    protected boolean complete;
    protected float duration;
    protected float currentTime;
    protected Vector2 position;

    protected float attackTime;
    protected boolean attackMode;

    protected Circle circleCollider;
    protected Rectangle collider;

    public BaseBossAttack(){
        this.active = false;
        this.complete = false;
        this.position = new Vector2();
    }

    public abstract void initialize(Vector2 startPosition);
    public abstract void update(float delta);
    public abstract void render(ShapeRenderer shapeRenderer);

    public void reset(){
        active = false;
        complete = false;
        currentTime = 0f;
        attackTime = 0f;
        attackMode = false;
    }

    public void activate(Vector2 startPosition){
        this.active = true;
        this.complete = false;
        this.currentTime = 0f;

        this.attackMode = false;
        this.attackTime = 0f;
        initialize(startPosition);
    }

    protected void switchToAttack(){
        attackMode = true;
        attackTime = currentTime;
    }

    protected boolean attackFinished(float activeTime){
        return  currentTime - attackTime >= activeTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public float getDuration() { return duration;}

    public boolean isAttackMode(){
        return attackMode;
    }

    public boolean isActive() {
        return active && !complete;
    }

    public Circle getCircleCollider() {
        return circleCollider;
    }

    public Rectangle getCollider() {
        return collider;
    }
}

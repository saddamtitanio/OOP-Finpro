package com.finpro.frontend.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.strategy.zombie.movement.FastZombieMovementStrategy;
import com.finpro.frontend.strategy.zombie.movement.SprintZombieMovementStrategy;
import com.finpro.frontend.strategy.zombie.movement.WalkZombieMovementStrategy;

public class FastZombie extends BaseZombie {

    public FastZombie(Vector2 startPosition){
        super(startPosition);
        this.hp = 10;
        this.BASE_SPEED = 250f;
    }

    @Override
    public void initialize(Vector2 startPosition){
        super.initialize(startPosition);
        this.hp = 10;
        this.BASE_SPEED = 250f;
    }

    public void movementLogic(){
        float targetDistance = position.dst(target.getPosition());

        if(targetDistance > 250f){
            chooseMovementStrategy(FastZombieMovementStrategy.SPRINT);
        } else{
            chooseMovementStrategy(FastZombieMovementStrategy.WALK);
        }
    }

    public void chooseMovementStrategy(FastZombieMovementStrategy strategy){
        switch (strategy){
            case SPRINT:
                setMovementStrategy(new SprintZombieMovementStrategy());
                break;

            case WALK:
                setMovementStrategy(new WalkZombieMovementStrategy());
                break;
        }
    }


    public boolean isTargeting(){
        return target != null;
    }

    @Override
    public void update(float delta) {
        if (!active || !isTargeting()) return;

        movementLogic();
        movementStrategy.move(this, target, delta);  // APPLY STRATEGY
        updateCollider();
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(position.x, position.y, width, height);
    }
}


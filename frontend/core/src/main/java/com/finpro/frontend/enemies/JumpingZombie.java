package com.finpro.frontend.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.WorldBounds;
import com.finpro.frontend.strategy.zombie.movement.*;

public class JumpingZombie extends BaseZombie {
    private float lungeCooldownTimer = 1f;
    private boolean canLunge = false;
    private final LungeZombieMovementStrategy lungeStrategy = new LungeZombieMovementStrategy();
    private final WalkZombieMovementStrategy walkStrategy = new WalkZombieMovementStrategy();



    public JumpingZombie(Vector2 startPosition){
        super(startPosition);
        this.hp = 10;
        this.BASE_SPEED = 200f;
    }

    @Override
    public void initialize(Vector2 startPosition){
        super.initialize(startPosition);
        this.hp = 10;
        this.BASE_SPEED = 200f;
    }

    public void movementLogic(float delta){
        float targetDistance = position.dst(target.getPosition());

        if (!canLunge) {
            lungeCooldownTimer -= delta;
            if (lungeCooldownTimer <= 0) {
                canLunge = true;
            }
        }


        if (movementStrategy == lungeStrategy && !lungeStrategy.isFinished()) {
            return;
        }

        if (!canLunge) {
            chooseMovementStrategy(JumpingZombieMovementStrategy.WALK);
            return;
        }

        chooseMovementStrategy(JumpingZombieMovementStrategy.LUNGE);
        canLunge = false;
        lungeCooldownTimer = 5f;
    }



    public void chooseMovementStrategy(JumpingZombieMovementStrategy strategy){
        switch (strategy){
            case LUNGE:
                setMovementStrategy(lungeStrategy);
                break;

            case WALK:
                setMovementStrategy(walkStrategy);
                break;
        }
    }

    public boolean isTargeting(){
        return target != null;
    }

    @Override
    public void update(float delta) {
        if (!active || !isTargeting()) return;

        movementLogic(delta);
        movementStrategy.move(this, target, delta);  // APPLY STRATEGY
        updateCollider();
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    public boolean canCollide() {
        return lungeStrategy.isFinished();
    }

    @Override
    public void moveWithBounds(float delta, WorldBounds worldBounds) {
        Vector2 nextPos = new Vector2(position).mulAdd(velocity, delta);

        Rectangle nextCollider = new Rectangle( nextPos.x, nextPos.y, width, height);

        if (!worldBounds.collides(nextCollider)) {
            if(lungeStrategy.shouldMove() || movementStrategy == walkStrategy){
                position.set(nextPos);
            }
            syncCollider();
        } else {
            velocity.setZero();
        }
    }

}

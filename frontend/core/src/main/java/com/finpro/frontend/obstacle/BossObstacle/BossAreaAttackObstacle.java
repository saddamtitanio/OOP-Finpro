package com.finpro.frontend.obstacle.BossObstacle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BossAreaAttackObstacle extends BaseBossAttack {
    private float radius;
    private float maxRadius;

    private float warningTime = 1f;
    private float activeTime = 1.5f;

    public BossAreaAttackObstacle(){
        super();
        this.maxRadius = 150f;
        this.duration = warningTime + activeTime;
    }

    @Override
    public void initialize(Vector2 startPosition) {
        this.position.set(startPosition);
        this.radius = 0f;
    }

    @Override
    public void update(float delta) {
        if(!active){
            return;
        }

        currentTime += delta;

        if(!attackMode){
            radius = (currentTime / warningTime) * maxRadius * 0.3f;

            if(currentTime >= warningTime){
                switchToAttack();
                radius = maxRadius;
            }
        } else {
            radius = maxRadius + MathUtils.sin((currentTime - attackTime) * 10f) * 20f;

            if(attackFinished(activeTime)){
                complete = true;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if(!active){
            return;
        }

        if(!attackMode){
            shapeRenderer.setColor(1f, 0.5f, 0f, 0.5f);
        } else {
            shapeRenderer.setColor(1f, 0f, 0f, 0.7f);
        }

        shapeRenderer.circle(position.x, position.y, radius);
    }
}

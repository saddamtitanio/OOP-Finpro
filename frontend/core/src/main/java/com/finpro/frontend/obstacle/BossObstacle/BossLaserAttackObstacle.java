package com.finpro.frontend.obstacle.BossObstacle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BossLaserAttackObstacle extends BaseBossAttack{
    private int patternType;

    private float laserRotation;
    private float warningTime = 1f;
    private float activeTime = 6f;
    private float laserLength = 900f;


    public BossLaserAttackObstacle(){
        super();
        this.duration = warningTime + activeTime;
    }

    @Override
    public void initialize(Vector2 startPosition) {
        this.position.set(startPosition);
        this.laserRotation = 0f;
        this.patternType = MathUtils.random(2);
    }

    @Override
    public void update(float delta) {
        if(!active){
            return;
        }

        currentTime += delta;

        if(!attackMode){
            if(currentTime >= warningTime){
                switchToAttack();
            }
        } else {
            laserRotation += 60f * delta;
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
            shapeRenderer.setColor(1f, 1f, 0f, 0.6f);
            drawLaser(shapeRenderer, laserRotation, 4f);
        } else {
            shapeRenderer.setColor(1f, 0f, 0f, 1f);
            drawLaser(shapeRenderer, laserRotation, 10f);
        }
    }

    private void drawLaser(ShapeRenderer shapeRenderer, float angle, float thickness){
        switch(patternType){
            case 0:
                drawLine(shapeRenderer, angle, thickness);
                drawLine(shapeRenderer, angle + 120, thickness);
                drawLine(shapeRenderer, angle + 240, thickness);
                break;

            case 1:
                drawLine(shapeRenderer, angle, thickness);
                drawLine(shapeRenderer, angle + 180, thickness);
                break;

            case 2:
                drawLine(shapeRenderer, angle, thickness);
                break;
            case 3:
                drawLine(shapeRenderer, angle, thickness);
                drawLine(shapeRenderer, angle + 90, thickness);
                drawLine(shapeRenderer, angle + 180, thickness);
                drawLine(shapeRenderer, angle + 270, thickness);
                break;
        }
    }

    private void drawLine(ShapeRenderer shapeRenderer, float angleDegree, float thickness){
        float angleRad = angleDegree * MathUtils.degreesToRadians;
        float endX = position.x + laserLength * MathUtils.cos(angleRad);
        float endY = position.y + laserLength * MathUtils.sin(angleRad);

        shapeRenderer.rectLine(position.x, position.y, endX, endY, thickness);
    }
}

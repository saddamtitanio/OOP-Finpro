package com.finpro.frontend.obstacle.BossObstacle;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BossLaserAttackObstacle extends BaseBossAttack{
    private int patternType;

    private float laserRotation;
    private float warningTime = 1f;
    private float activeTime = 6f;
    private float laserLength = 900f;

    private List<Rectangle> laserColliders;


    public BossLaserAttackObstacle(){
        super();
        this.duration = warningTime + activeTime;
        this.laserColliders = new ArrayList<>();
        this.collider = new Rectangle();
    }

    @Override
    public void initialize(Vector2 startPosition) {
        this.position.set(startPosition);
        this.laserRotation = 0f;
        this.patternType = MathUtils.random(2);
        this.laserColliders.clear();
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

    public void renderDebug(ShapeRenderer shapeRenderer) {
        if (!attackMode) return;

        shapeRenderer.setColor(0f, 1f, 0f, 0.5f); // green, semi-transparent

        for (Rectangle rect : laserColliders) {
            shapeRenderer.rect(
                rect.x,
                rect.y,
                rect.width,
                rect.height
            );
        }
    }
    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if(!active){
            return;
        }

        renderDebug(shapeRenderer);
        if(!attackMode){
            shapeRenderer.setColor(1f, 1f, 0f, 0.6f);
            drawLaser(shapeRenderer, laserRotation, 4f);
        } else {
            shapeRenderer.setColor(1f, 0f, 0f, 1f);
            drawLaser(shapeRenderer, laserRotation, 10f);
        }
    }

    public List<Rectangle> getLaserColliders() {
        return laserColliders;
    }

    private void drawLaser(ShapeRenderer shapeRenderer, float angle, float thickness){
        laserColliders.clear();

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

        if(attackMode){
            int segments = 20;
            for (float i = 0; i < segments; i++) {
                float t1 = i / segments;
                float t2 = (i + 1) / segments;

                float X1 = position.x + (endX - position.x) * t1;
                float Y1 = position.y + (endY - position.y) * t1;
                float X2 = position.x + (endX - position.x) * t2;
                float Y2 = position.y + (endY - position.y) * t2;

                float segLength = thickness * 2;
                float centerX = (X1 + X2) / 2;
                float centerY = (Y1 + Y2) / 2;

                laserColliders.add(new Rectangle(
                    centerX - segLength/2,
                    centerY - segLength/2,
                    segLength,
                    segLength
                ));
            }
        }

    }

}

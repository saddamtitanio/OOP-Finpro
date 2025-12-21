package com.finpro.frontend.state.BossState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;
import com.finpro.frontend.obstacle.BossObstacle.BossLaserAttackObstacle;

public class BossLaserState implements BossBehaviorState{
    private float timer;
    private float moveSpeed = 300f;

    private float initialLaserTime;
    private float laserDuration;
    private Vector2 centerPosition;

    private boolean laserCreated;
    private boolean reachedCenter;


    @Override
    public void enter(Boss boss) {
        System.out.println("Boss entering " + getName() + " state");
        timer = 0f;
        laserCreated = false;
        reachedCenter = false;

        initialLaserTime = 0f;

        laserDuration = new BossLaserAttackObstacle().getDuration();

        centerPosition = new Vector2(
            boss.getWorldWidth() / 2,
            boss.getWorldHeight() / 2
        );
    }

    @Override
    public void update(Boss boss, Vector2 playerPosition, float delta) {
        timer += delta;

        if(!reachedCenter){
            Vector2 direction = new Vector2(centerPosition).sub(boss.getPosition());
            float distance = direction.len();

            if(distance > 5f){
                direction.nor();
                boss.setPosition(boss.getPosition().add(direction.scl(moveSpeed * delta)));
            } else {
                reachedCenter = true;
                boss.setPosition(centerPosition);
            }
        } else {
            boss.setPosition(centerPosition);

            if(!laserCreated){
                boss.getAttackManager().createLaserAttack(centerPosition);
                laserCreated = true;
                initialLaserTime = timer;
            }

            if(initialLaserTime > 0 && timer >= initialLaserTime + laserDuration){
                boss.setBehavior(new BossNormalState());
            }

        }
    }

    @Override
    public String getName() {
        return "Laser";
    }

    @Override
    public Color getBossColor() {
        return new Color(0f, 1f, 0f, 1f);
    }
}

package com.finpro.frontend.state.BossState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;

import java.util.ArrayList;

public class BossDashingState implements BossBehaviorState{
    private ArrayList<Vector2> dashPath;
    private int currentDashPoint;
    private float dashSpeed = 800f;

    @Override
    public void enter(Boss boss) {
        System.out.println("Boss entering " + getName() + " state");
        currentDashPoint = 0;
        generateRandomDashPattern(boss.getWorldWidth(), boss.getWorldHeight());
    }

    @Override
    public void update(Boss boss, Vector2 playerPosition, float delta) {
        if(dashPath.isEmpty() || currentDashPoint >= dashPath.size()){
            boss.setBehavior(new BossNormalState());
            return;
        }

        Vector2 target = dashPath.get(currentDashPoint);
        Vector2 direction = new Vector2(target).sub(boss.getPosition()).nor();
        boss.setPosition(boss.getPosition().add(direction.scl(dashSpeed * delta)));

        if(boss.getPosition().dst(target) < 10f){
            currentDashPoint++;
        }
    }

    private void generateRandomDashPattern(float worldWidth, float worldHeight){
        dashPath = new ArrayList<>();
        float padding = Math.min(worldWidth, worldHeight) * 0.1f;

        int patternType = MathUtils.random(3);

        switch(patternType){
            case 0 :
                dashPath.add(new Vector2(padding, worldHeight - padding));
                dashPath.add(new Vector2(worldWidth - padding, padding));
                dashPath.add(new Vector2(worldWidth - padding, worldHeight - padding));
                dashPath.add(new Vector2(padding, padding));
                break;
            case 1 :
                dashPath.add(new Vector2(padding, worldHeight - padding));
                dashPath.add(new Vector2(worldWidth - padding, worldHeight / 2));
                dashPath.add(new Vector2(padding, padding));
                break;
            case 2 :
                dashPath.add(new Vector2(worldWidth - padding, worldHeight - padding));
                dashPath.add(new Vector2(padding, worldHeight / 2));
                dashPath.add(new Vector2(worldWidth - padding, padding));
                break;
            case 3 :
                dashPath.add(new Vector2(worldWidth / 2, worldHeight - padding));
                dashPath.add(new Vector2(padding, worldHeight / 2));
                dashPath.add(new Vector2(worldWidth / 2, padding));
                dashPath.add(new Vector2(worldWidth - padding, worldHeight / 2));
                dashPath.add(new Vector2(worldWidth / 2, worldHeight / 2));
                break;
        }

    }

    @Override
    public String getName() {
        return "Dashing";
    }

    @Override
    public Color getBossColor() {
        return new Color(0f, 1f, 1f, 1f);
    }
}

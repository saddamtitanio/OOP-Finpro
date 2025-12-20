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
        generateRandomDashPattern(boss.getScreenWidth(), boss.getScreenHeight());
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

    private void generateRandomDashPattern(float screenWidth, float screenHeight){
        dashPath = new ArrayList<>();
        float padding = 100f;

        int patternType = MathUtils.random(3);

        switch(patternType){
            case 0 :
                dashPath.add(new Vector2(padding, screenHeight - padding));
                dashPath.add(new Vector2(screenWidth - padding, padding));
                dashPath.add(new Vector2(screenWidth - padding, screenHeight - padding));
                dashPath.add(new Vector2(padding, padding));
                break;
            case 1 :
                dashPath.add(new Vector2(padding, screenHeight - padding));
                dashPath.add(new Vector2(screenWidth - padding, screenHeight / 2));
                dashPath.add(new Vector2(padding, padding));
                break;
            case 2 :
                dashPath.add(new Vector2( screenWidth - padding, screenHeight - padding));
                dashPath.add(new Vector2(padding, screenHeight / 2));
                dashPath.add(new Vector2(screenWidth - padding, padding));
                break;
            case 3 :
                dashPath.add(new Vector2( screenWidth / 2, screenHeight - padding));
                dashPath.add(new Vector2(padding, screenHeight / 2));
                dashPath.add(new Vector2(screenWidth / 2, padding));
                dashPath.add(new Vector2(screenWidth - padding, screenHeight / 2));
                dashPath.add(new Vector2(screenWidth / 2, screenHeight / 2));
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

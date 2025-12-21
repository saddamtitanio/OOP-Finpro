package com.finpro.frontend.state.BossState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;

public class BossNormalState implements BossBehaviorState{
    private float movementSpeed = 50f;

    @Override
    public void enter(Boss boss) {
        System.out.println("Boss entering " + getName() + " state");
    }

    @Override
    public void update(Boss boss, Vector2 playerPosition, float delta) {
        Vector2 direction = new Vector2(playerPosition).sub(boss.getPosition()).nor();
        Vector2 velocity = direction.scl(movementSpeed * delta);

        boss.setPosition(boss.getPosition().add(velocity));
    }

    @Override
    public String getName() {
        return "Normal";
    }

    @Override
    public Color getBossColor() {
        return Color.PURPLE;
    }
}

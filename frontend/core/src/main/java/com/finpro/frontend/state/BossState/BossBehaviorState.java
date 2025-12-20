package com.finpro.frontend.state.BossState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;

public interface BossBehaviorState {
    void enter(Boss boss);
    void update(Boss boss, Vector2 playerPosition, float delta);
    String getName();
    Color getBossColor();
}

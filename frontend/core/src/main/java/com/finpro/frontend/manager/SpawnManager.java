package com.finpro.frontend.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpawnManager {

    private final Rectangle bounds;
    private final float inset;

    public SpawnManager(Rectangle bounds, float inset) {
        this.bounds = bounds;
        this.inset = inset;
    }

    public Vector2 getBorderSpawn() {
        float left   = bounds.x + inset;
        float right  = bounds.x + bounds.width  - inset;
        float bottom = bounds.y + inset;
        float top    = bounds.y + bounds.height - inset;

        int edge = MathUtils.random(3); // 0–3

        float x, y;

        switch (edge) {
            case 0: // bottom
                x = MathUtils.random(left, right);
                y = bottom;
                break;

            case 1: // top
                x = MathUtils.random(left, right);
                y = top;
                break;

            case 2: // left
                x = left;
                y = MathUtils.random(bottom, top);
                break;

            default: // right
                x = right;
                y = MathUtils.random(bottom, top);
                break;
        }

        return new Vector2(x, y);
    }
}


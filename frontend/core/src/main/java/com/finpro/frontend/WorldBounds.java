package com.finpro.frontend;

import com.badlogic.gdx.math.Rectangle;

public class WorldBounds {

    private final Rectangle left;
    private final Rectangle right;
    private final Rectangle top;
    private final Rectangle bottom;

    private final float worldWidth;
    private final float worldHeight;

    public WorldBounds(float worldWidth, float worldHeight, float thickness) {

        left   = new Rectangle(-thickness, 0, thickness, worldHeight);
        right  = new Rectangle(worldWidth, 0, thickness, worldHeight);
        bottom = new Rectangle(0, -thickness, worldWidth, thickness);
        top    = new Rectangle(0, worldHeight, worldWidth, thickness);

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public boolean collides(Rectangle rect) {
        return rect.overlaps(left)
            || rect.overlaps(right)
            || rect.overlaps(top)
            || rect.overlaps(bottom);
    }

    public void clamp(Rectangle rect) {
        if (rect.x < 0) rect.x = 0;
        if (rect.y < 0) rect.y = 0;
        if (rect.x + rect.width > worldWidth)
            rect.x = worldWidth - rect.width;
        if (rect.y + rect.height > worldHeight)
            rect.y = worldHeight - rect.height;
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }
}

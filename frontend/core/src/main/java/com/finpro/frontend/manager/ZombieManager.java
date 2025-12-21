package com.finpro.frontend.manager;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.finpro.frontend.Bullet;
import com.finpro.frontend.Player;
import com.finpro.frontend.WorldBounds;
import com.finpro.frontend.enemies.BaseZombie;
import com.badlogic.gdx.math.Rectangle;
import com.finpro.frontend.enemies.JumpingZombie;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ZombieManager {
    private final WorldBounds worldBounds;

    private final ArrayList<BaseZombie> activeZombies = new ArrayList<>();
    private Player target;

    public ZombieManager(WorldBounds worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void addZombie(BaseZombie zombie){
        activeZombies.add(zombie);
        zombie.setActive(true);
        zombie.setTarget(target);
    }

    public void removeZombie(BaseZombie zombie){
        activeZombies.remove(zombie);
    }

    public List<BaseZombie> getZombies() {
        return activeZombies;
    }

    public void update(float delta, WorldBounds worldBounds) {
        if (!target.isAlive()) return;

        for (BaseZombie z : activeZombies){
            z.update(delta);
            z.moveWithBounds(delta,worldBounds);
        }

        handleCollisions();
    }

    private void handleCollisions() {
        int n = activeZombies.size();

        for (int i = 0; i < n; i++) {
            BaseZombie a = activeZombies.get(i);
            if (!a.canCollide()) continue;

            for (int j = i + 1; j < n; j++) {
                BaseZombie b = activeZombies.get(j);
                if (!b.canCollide()) continue;

                resolveCollision(a, b);
            }
        }
    }

    private void resolveCollision(BaseZombie a, BaseZombie b) {
        float dx = b.getPosition().x - a.getPosition().x;
        float dy = b.getPosition().y - a.getPosition().y;

        float minDist = (a.getWidth() + b.getWidth()) * 0.60f;

        float dist2 = dx*dx + dy*dy;
        float minDist2 = minDist * minDist;

        if (dist2 >= minDist2) return;

        float dist = (float)Math.sqrt(dist2);
        if (dist == 0) dist = 0.01f;

        float overlap = (minDist - dist) * 0.5f;

        float nx = dx / dist;
        float ny = dy / dist;


        a.getPosition().sub(nx * overlap, ny * overlap);
        b.getPosition().add(nx * overlap, ny * overlap);

        a.syncCollider();
        b.syncCollider();

        worldBounds.clamp(a.getCollider());
        worldBounds.clamp(b.getCollider());

    }

    public void setTarget(Player target){
        this.target = target;
    }
}


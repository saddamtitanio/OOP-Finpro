package com.finpro.frontend.pool;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.JumpingZombie;

public class JumpingZombiePool extends ZombiePool<JumpingZombie> {

    @Override
    protected JumpingZombie createZombie() {
        return new JumpingZombie(new Vector2());
    }

    @Override
    protected void resetZombie(JumpingZombie zombie) {
        zombie.setTarget(null);
        zombie.setPosition(0,0);
        zombie.setActive(false);
        zombie.setHp(10);
        zombie.setVelocity(0,0);
    }

    public JumpingZombie obtain(Vector2 position) {
        JumpingZombie jumpingZombie = super.obtain();
        jumpingZombie.initialize(position);
        jumpingZombie.setActive(true);
        return jumpingZombie;
    }
}

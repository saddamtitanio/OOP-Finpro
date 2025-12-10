package com.finpro.frontend.strategy.zombie.movement;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;
import com.finpro.frontend.enemies.BaseZombie;

public class LungeZombieMovementStrategy implements ZombieMovementStrategy {

    private enum State { WINDUP, LUNGE, COOLDOWN }

    private State state = State.WINDUP;
    private float timer = 0;

    private static final float WINDUP_TIME = 1.0f;
    private static final float LUNGE_TIME = 0.35f;
    private static final float COOLDOWN_TIME = 1.0f;

    private static final float LUNGE_SPEED = 900f;
    private static final float ARC_HEIGHT = 60f;

    private final Vector2 direction = new Vector2();

    @Override
    public void move(BaseZombie zombie, Player target, float delta) {

        timer += delta;

        switch (state) {
            case WINDUP:
                if (timer >= WINDUP_TIME) {
                    timer = 0;

                    direction.set(target.getPosition()).sub(zombie.getPosition()).nor();
                    state = State.LUNGE;
                }
                break;

            case LUNGE:
                float t = timer / LUNGE_TIME;

                if (timer >= LUNGE_TIME) {
                    timer = 0;
                    state = State.COOLDOWN;
                    break;
                }

                zombie.getVelocity().set(direction).scl(LUNGE_SPEED);
                zombie.getPosition().mulAdd(zombie.getVelocity(), delta);

                float arcOffset = (float)(Math.sin(Math.PI * t) * ARC_HEIGHT);
                zombie.getPosition().add(0, arcOffset * delta);
                break;

            case COOLDOWN:
                if (timer >= COOLDOWN_TIME) {
                    timer = 0;
                    state = State.WINDUP;
                }
                break;
        }
    }

    public boolean isFinished() {
        return state == State.WINDUP && timer == 0;
    }
}

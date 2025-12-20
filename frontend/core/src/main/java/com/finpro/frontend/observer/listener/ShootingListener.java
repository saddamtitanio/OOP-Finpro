package com.finpro.frontend.observer.listener;

import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.observer.event.FireEvent;

public class ShootingListener implements EventListener<FireEvent> {
    private final BulletFactory bulletFactory;

    public ShootingListener(BulletFactory bulletFactory) {
        this.bulletFactory = bulletFactory;
    }

    @Override
    public void update(FireEvent event) {
        event.strategy.shoot(
            bulletFactory,
            event.player,
            event.direction
        );
    }
}

package com.finpro.frontend.observer.listener;

import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.manager.BulletManager;
import com.finpro.frontend.observer.event.FireEvent;

public class ShootingListener implements EventListener<FireEvent> {

    private final BulletManager bulletManager;

    public ShootingListener(BulletManager bulletManager) {
        this.bulletManager = bulletManager;
    }

    @Override
    public void update(FireEvent event) {
        event.strategy.shoot(
            bulletManager,
            event.player,
            event.direction
        );
    }
}

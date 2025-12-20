package com.finpro.frontend;

import com.finpro.frontend.observer.EventManager;

import java.util.ArrayList;
import java.util.List;

public class PowerUpManager {
    private List<PowerUpEntity> activePowerUps = new ArrayList<>();
    private EventManager eventManager;

    public PowerUpManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void spawnRandomPowerUp(float x, float y) {

    }
}

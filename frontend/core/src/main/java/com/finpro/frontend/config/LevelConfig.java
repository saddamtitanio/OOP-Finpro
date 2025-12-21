package com.finpro.frontend.config;

import java.util.HashMap;

public class LevelConfig {
    public int levelId;
    public int levelDuration;
    public int spawnInterval;
    public HashMap<String, Integer> enemySpawnRatio;
    public int spawnDensity;
    public boolean isBossLevel = false;
}

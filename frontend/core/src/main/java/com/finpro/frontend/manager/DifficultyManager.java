package com.finpro.frontend.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.finpro.frontend.config.DifficultyConfig;

public class DifficultyManager {
    private DifficultyConfig diffConfig;
    private float currentDifficulty;

    public DifficultyManager(){
        Json json = new Json();

        FileHandle file = Gdx.files.internal("config/DifficultyConfig.json");
        diffConfig = json.fromJson(DifficultyConfig.class, file);
        setMode("Normal"); // default
    }


    public void setMode(String modeName) {
        if (diffConfig.difficultyModes.containsKey(modeName)) {
            currentDifficulty = diffConfig.difficultyModes.get(modeName);
            System.out.println("Difficulty set to: " + modeName + " (" + currentDifficulty + ")");
        } else {
            System.err.println("Unknown difficulty mode: " + modeName);
        }
    }

    public float getMultiplier() {
        return currentDifficulty;
    }

    public int scaleInt(int base) {
        return Math.max(1, Math.round(base * currentDifficulty));
    }

    public float scaleFloat(float value) {
        return value * currentDifficulty;
    }
}

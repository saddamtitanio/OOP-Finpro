package com.game.backend.service;

import com.game.backend.model.Settings;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private Settings settings = new Settings();

    public Settings getSettings() {
        return settings;
    }

    public void updateSettings(Settings newSettings) {
        this.settings = newSettings;
    }
}

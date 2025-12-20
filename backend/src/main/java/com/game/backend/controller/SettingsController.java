package com.game.backend.controller;

import com.game.backend.model.Settings;
import com.game.backend.service.SettingsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService service;

    public SettingsController(SettingsService service) {
        this.service = service;
    }

    @GetMapping
    public Settings getSettings() {
        return service.getSettings();
    }

    @PostMapping
    public void updateSettings(@RequestBody Settings settings) {
        service.updateSettings(settings);
    }
}

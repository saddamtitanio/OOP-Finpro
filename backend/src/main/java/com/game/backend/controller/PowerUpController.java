package com.game.backend.controller;

import com.game.backend.model.PowerUp;
import com.game.backend.service.PowerUpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/powerups")
public class PowerUpController {

    private final PowerUpService service;

    public PowerUpController(PowerUpService service) {
        this.service = service;
    }

    @GetMapping
    public List<PowerUp> getAll() {
        return service.getAll();
    }
}

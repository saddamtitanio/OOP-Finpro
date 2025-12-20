package com.game.backend.controller;

import com.game.backend.service.DeathMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/death")
public class DeathMessageController {

    private final DeathMessageService service;

    public DeathMessageController(DeathMessageService service) {
        this.service = service;
    }

    @GetMapping("/random")
    public String random() {
        return service.random();
    }
}

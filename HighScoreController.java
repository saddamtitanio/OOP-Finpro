package com.game.backend.controller;

import com.game.backend.model.HighscoreEntry;
import com.game.backend.service.HighScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class HighScoreController {

    private final HighScoreService service;

    public HighScoreController(HighScoreService service) {
        this.service = service;
    }

    @PostMapping
    public void submit(@RequestParam int value) {
        service.submitScore(value);
    }

    @GetMapping("/leaderboard")
    public List<HighscoreEntry> leaderboard() {
        return service.getLeaderboard();
    }
}

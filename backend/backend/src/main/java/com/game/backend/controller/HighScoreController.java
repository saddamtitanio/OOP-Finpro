package com.game.backend.controller;

import com.game.backend.model.HighscoreEntry;
import com.game.backend.service.HighScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class HighScoreController {

    @Autowired
    private HighScoreService highScoreService;

    @PostMapping
    public String submitScore(@RequestBody HighscoreEntry entry) {
        highScoreService.addScore(entry);
        return "Score saved for " + entry.getUsername();
    }

    @GetMapping
    public List<HighscoreEntry> getScores() {
        return highScoreService.getAllScores();
    }
}

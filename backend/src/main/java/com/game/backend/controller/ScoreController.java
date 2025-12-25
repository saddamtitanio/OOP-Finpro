package com.game.backend.controller;

import com.game.backend.model.Score;
import com.game.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")

public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Score scoreNew = scoreService.createScore(score);
            return ResponseEntity.status(HttpStatus.CREATED).body(scoreNew);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {
        Optional<Score> score = scoreService.getScoreById(scoreId);
        if (score.isPresent()) {
            return ResponseEntity.ok(score);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score not found");
        }
    }

    @PutMapping("/{scoreId}")
    public ResponseEntity<?> updateScore(@PathVariable UUID scoreId, @RequestBody Score score) {
        try {
            Score updatedScore = scoreService.updateScore(scoreId, score);
            return ResponseEntity.ok(updatedScore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score not found");
        }
    }

    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok("Deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Score>> getScoresByPlayerId(@PathVariable UUID playerId) {
        List<Score> scores = scoreService.getScoresByPlayerId(playerId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/player/{playerId}/ordered")
    public ResponseEntity<List<Score>> getScoresByPlayerIdOrdered(@PathVariable UUID playerId) {
        return ResponseEntity.ok(scoreService.getScoresByPlayerId(playerId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(scoreService.getLeaderboard(limit));
    }

    @GetMapping("/player/{playerId}/highest")
    public ResponseEntity<?>getHighestScoreByPlayerId(@PathVariable UUID playerId) {
        Optional<Score> highestScore = scoreService.getHighestScoreByPlayerId(playerId);
        if (highestScore.isPresent()) {
            return ResponseEntity.ok(highestScore.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No scores found...");
    }

    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable Integer minValue) {
        return ResponseEntity.ok(scoreService.getScoresAboveValue(minValue));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores() {
        return ResponseEntity.ok(scoreService.getRecentScores());
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}

package com.game.backend.controller;
import com.game.backend.model.GameRun;
import com.game.backend.service.GameRunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/runs")
public class GameRunController {
    private final GameRunService gameRunService;

    public GameRunController(GameRunService gameRunService) {
        this.gameRunService = gameRunService;
    }

    @PostMapping
    public ResponseEntity<GameRun> saveRun(@RequestBody Map<String, Object> payload) {
        GameRun run = new GameRun();
        run.setPlayerId(UUID.fromString(payload.get("playerId").toString()));
        run.setScore((Integer) payload.get("score"));
        run.setDurationSeconds((Integer) payload.get("durationSeconds"));

        Map<String, Integer> killsMap = (Map<String, Integer>) payload.get("kills");

        return ResponseEntity.ok(gameRunService.saveRun(run, killsMap));
    }


    @GetMapping
    public ResponseEntity<List<GameRun>> getAllRuns() {
        return ResponseEntity.ok(gameRunService.getAllRuns());
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<GameRun>> getRunsByPlayer(@PathVariable UUID playerId) {
        return ResponseEntity.ok(gameRunService.getRunsByPlayer(playerId));
    }

    @GetMapping("/{runId}")
    public ResponseEntity<GameRun> getRun(@PathVariable UUID runId) {
        return ResponseEntity.ok(gameRunService.getRunById(runId));
    }
}

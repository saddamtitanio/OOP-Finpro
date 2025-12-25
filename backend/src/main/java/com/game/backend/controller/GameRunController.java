package com.game.backend.controller;
import com.game.backend.model.GameRun;
import com.game.backend.service.GameRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        run.setDistanceTravelled((Integer) payload.get("distanceTravelled"));

        Map<String, Integer> kills = (Map<String, Integer>) payload.get("kills");

        return ResponseEntity.ok(gameRunService.saveRun(run, kills));
    }



}

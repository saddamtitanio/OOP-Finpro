package com.game.backend.controller;

import com.game.backend.model.GameRun;
import com.game.backend.model.KillStats;
import com.game.backend.model.Player;
import com.game.backend.repository.GameRunRepository;
import com.game.backend.repository.KillStatRepository;
import com.game.backend.repository.PlayerRepository;
import com.game.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameRunRepository gameRunRepository;

    @Autowired
    private KillStatRepository killStatRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        try {
            Player createdPlayer = playerService.createPlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable UUID playerId) {
        Optional<Player> player = playerService.getPlayerById(playerId);
        if (player.isPresent()) {
            return ResponseEntity.ok(player.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Player not found with ID: " + playerId + "\"}");
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getPlayerByUsername(@PathVariable String username) {
        Optional<Player> player = playerService.getPlayerByUsername(username);
        if (player.isPresent()) {
            return ResponseEntity.ok(player.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Player not found with username: " + username + "\"}");
        }
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayer(@PathVariable UUID playerId, @RequestBody Player player) {
        try {
            Player updatedPlayer = playerService.updatePlayer(playerId, player);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<?> updatePlayerByUsername(@PathVariable String username, @RequestBody Player player) {
        try {
            Optional<Player> existingPlayer = playerService.getPlayerByUsername(username);
            if (existingPlayer.isPresent()) {
                Player updatedPlayer = playerService.updatePlayer(existingPlayer.get().getPlayerId(), player);
                return ResponseEntity.ok(updatedPlayer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Player not found with username: " + username + "\"}");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable UUID playerId) {
        try {
            playerService.deletePlayer(playerId);
            return ResponseEntity.ok("{\"message\": \"Player deleted successfully\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<?> deletePlayerByUsername(@PathVariable String username) {
        try {
            playerService.deletePlayerByUsername(username);
            return ResponseEntity.ok("{\"message\": \"Player deleted successfully\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean exists = playerService.isUsernameExists(username);
        return ResponseEntity.ok("{\"exists\": " + exists + "}");
    }

    @GetMapping("/leaderboard/global")
    public ResponseEntity<List<Object>> getGlobalLeaderboardByPlayer() {
        List<Player> players = playerRepository.findAll();

        List<Object> leaderboard = new ArrayList<>();

        for (Player player : players) {
            List<GameRun> topRuns = gameRunRepository.findTop1ByPlayerIdOrderByScoreDesc(player.getPlayerId());

            if (!topRuns.isEmpty()) {
                GameRun run = topRuns.get(0);

                Map<String, Object> runData = new HashMap<>();
                runData.put("playerId", player.getPlayerId());
                runData.put("username", player.getUsername());
                runData.put("score", run.getScore());
                runData.put("createdAt", run.getCreatedAt());

                leaderboard.add(runData);
            }
        }

        leaderboard.sort((runEntryAObj, runEntryBObj) -> {
            Map<String, Object> runEntryA = (Map<String, Object>) runEntryAObj;
            Map<String, Object> runEntryB = (Map<String, Object>) runEntryBObj;

            Integer scoreA = (Integer) runEntryA.get("score");
            Integer scoreB = (Integer) runEntryB.get("score");

            return scoreB.compareTo(scoreA);
        });

        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/leaderboard/{playerId}")
    public ResponseEntity<List<Object>> getTopRunsForPlayer(
            @PathVariable UUID playerId,
            @RequestParam(defaultValue = "10") int limit) {

        List<GameRun> allRuns = gameRunRepository.findByPlayerIdOrderByScoreDesc(playerId);

        List<GameRun> topRuns = allRuns.stream().limit(limit).toList();

        List<Object> leaderboard = new ArrayList<>();

        for (GameRun run : topRuns) {
            Map<String, Object> runEntry = new HashMap<>();
            runEntry.put("runId", run.getRunId());
            runEntry.put("score", run.getScore());
            runEntry.put("durationSeconds", run.getDurationSeconds());
            runEntry.put("createdAt", run.getCreatedAt());

            List<KillStats> kills = killStatRepository.findByRunId(run.getRunId());
            Map<String, Integer> killsMap = new HashMap<>();
            for (KillStats kill : kills) {
                killsMap.put(kill.getZombieType(), kill.getCount());
            }
            runEntry.put("kills", killsMap);

            leaderboard.add(runEntry);
        }

        return ResponseEntity.ok(leaderboard);
    }
}
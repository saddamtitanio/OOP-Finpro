package com.game.backend.service;

import com.game.backend.model.GameRun;
import com.game.backend.model.KillStats;
import com.game.backend.repository.GameRunRepository;
import com.game.backend.repository.KillStatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GameRunService {

    private final GameRunRepository gameRunRepository;
    private final KillStatRepository killStatRepository;

    public GameRunService(GameRunRepository gameRunRepository,
                          KillStatRepository killStatRepository) {
        this.gameRunRepository = gameRunRepository;
        this.killStatRepository = killStatRepository;
    }

    @Transactional
    public GameRun saveRun(GameRun run, Map<String, Integer> kills) {

        // Save run first
        GameRun savedRun = gameRunRepository.save(run);

        // Convert kills map to KillStats
        List<KillStats> killStatsList = kills.entrySet().stream()
                .map(e -> new KillStats(savedRun.getRunId(), e.getKey(), e.getValue()))
                .toList();

        // Save all kills
        killStatRepository.saveAll(killStatsList);

        // Attach kills to GameRun so returned JSON includes them
        savedRun.setKills(killStatsList);

        return savedRun;
    }


    public List<GameRun> getAllRuns() {
        return gameRunRepository.findAll();
    }

    public List<GameRun> getRunsByPlayer(UUID playerId) {
        return gameRunRepository.findByPlayerId(playerId);
    }

    public GameRun getRunById(UUID runId) {
        return gameRunRepository.findById(runId).orElseThrow(() -> new RuntimeException("Run not found"));
    }
}

package com.game.backend.service;

import com.game.backend.model.GameRun;
import com.game.backend.model.KillStats;
import com.game.backend.model.Player;
import com.game.backend.repository.GameRunRepository;
import com.game.backend.repository.KillStatRepository;
import com.game.backend.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GameRunService {

    private final GameRunRepository gameRunRepository;
    private final KillStatRepository killStatRepository;
    private final PlayerRepository playerRepository;

    public GameRunService(GameRunRepository gameRunRepository,
                          KillStatRepository killStatRepository, PlayerRepository playerRepository) {
        this.gameRunRepository = gameRunRepository;
        this.killStatRepository = killStatRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public GameRun saveRun(GameRun run, Map<String, Integer> kills) {
        GameRun savedRun = gameRunRepository.save(run);

        List<KillStats> killStatsList = kills.entrySet().stream()
                .map(e -> new KillStats(savedRun.getRunId(), e.getKey(), e.getValue()))
                .toList();

        killStatRepository.saveAll(killStatsList);
        savedRun.setKills(killStatsList);

        Player player = playerRepository.findById(savedRun.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (savedRun.getScore() > player.getHighScore()) {
            player.setHighScore(savedRun.getScore());
            playerRepository.save(player);
        }

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

package com.game.backend.service;

import com.game.backend.model.GameRun;
import com.game.backend.model.KillStats;
import com.game.backend.repository.GameRunRepository;
import com.game.backend.repository.KillStatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

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

        GameRun savedRun = gameRunRepository.save(run);

        kills.forEach((zombieType, count) -> {
            killStatRepository.save(
                    new KillStats(savedRun.getRunId(), zombieType, count)
            );
        });

        return savedRun;
    }
}

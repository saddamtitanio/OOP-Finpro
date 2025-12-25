package com.game.backend.repository;

import com.game.backend.model.GameRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRunRepository extends JpaRepository<GameRun, UUID> {
    List<GameRun> findByPlayerId(UUID playerId);
    List<GameRun> findTop10ByPlayerIdOrderByCreatedAtDesc(UUID playerId);
}

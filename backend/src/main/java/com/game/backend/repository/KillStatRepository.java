package com.game.backend.repository;

import com.game.backend.model.KillStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KillStatRepository extends JpaRepository<KillStats, UUID> {
    List<KillStats> findByRunId(UUID runId);
    List<KillStats> findByZombieType(String zombieType);
}

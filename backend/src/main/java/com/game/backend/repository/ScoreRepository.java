package com.game.backend.repository;

import com.game.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
    List<Score> findByPlayerId(UUID playerId);
    List<Score> findByValueGreaterThan(Integer minScore);
    List<Score> findAllByOrderByCreatedAtDesc();

    @Query("SELECT s FROM Score s ORDER BY s.value DESC")
    List<Score> findTopScores(int limit);

    @Query("SELECT s FROM Score s WHERE s.playerId = :playerId ORDER BY s.value DESC")
    List<Score> findHighestScoreByPlayerId(@Param("playerId") UUID playerId);
}

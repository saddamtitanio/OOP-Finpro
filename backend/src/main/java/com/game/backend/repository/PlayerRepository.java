package com.game.backend.repository;

import com.game.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Optional<Player> findByUsername(String username);
    boolean existsByUsername(String username);
    @Query("SELECT p FROM Player p ORDER BY p.highScore DESC")
    List<Player> findTopPlayersByHighScore(@Param("limit") int limit);
}

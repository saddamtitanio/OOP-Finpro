package com.game.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_runs")
public class GameRun {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID runId;

    @Column(nullable = false)
    private UUID playerId;

    @Column(nullable = false)
    private Integer score;

    private Integer durationSeconds;

    private Integer distanceTravelled;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // ===== Constructors =====
    public GameRun() {}

    // ===== Getters & Setters =====

    public UUID getRunId() {
        return runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Integer distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

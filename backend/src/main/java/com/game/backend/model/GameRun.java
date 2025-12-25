package com.game.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // NEW: link to KillStats
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "run_id", referencedColumnName = "runId")
    private java.util.List<KillStats> kills = new java.util.ArrayList<>();

    public GameRun() {}

    // ===== Getters & Setters =====
    public UUID getRunId() { return runId; }
    public void setRunId(UUID runId) { this.runId = runId; }

    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<KillStats> getKills() { return kills; }
    public void setKills(List<KillStats> kills) { this.kills = kills; }
}

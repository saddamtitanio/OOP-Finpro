package com.game.backend.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "kill_stats",
    uniqueConstraints = @UniqueConstraint(
            columnNames = {"run_id", "zombie_type"}
    )
)

public class KillStats {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "run_id", nullable = false)
    private UUID runId;

    @Column(name = "zombie_type", nullable = false)
    private String zombieType;

    @Column(nullable = false)
    private Integer count;

    public KillStats() {}

    public KillStats(UUID runId, String zombieType, Integer count) {
        this.runId = runId;
        this.zombieType = zombieType;
        this.count = count;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRunId() {
        return runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public String getZombieType() {
        return zombieType;
    }

    public void setZombieType(String zombieType) {
        this.zombieType = zombieType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

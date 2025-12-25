package com.game.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "player_id")
    private UUID playerId;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "high_score")
    private Integer highScore = 0;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    // Default Constructor
    public Player() {}
    // Contructor with username
    public Player(String username) {
        this.username = username;
    }
    // Getter and Setter
    public UUID getPlayerId() {
        return playerId;
    }
    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getHighScore() {
        return highScore;
    }
    public void setHighScore(Integer highScore) {
        this.highScore = highScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // Business Methods
    public void updateHighScore(Integer newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
        }
    }
}
package com.game.backend.service;

import com.game.backend.model.Score;
import com.game.backend.repository.PlayerRepository;
import com.game.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;


    @Transactional
    public Score createScore(Score score) {
        if (!playerRepository.existsById(score.getPlayerId())) {
            throw new RuntimeException("Player does not exist");
        }
        Score scoreObj = scoreRepository.save(score);
        playerService.updatePlayerStats(scoreObj.getPlayerId(), scoreObj.getValue());
        return scoreObj;
    }

    public Optional<Score> getScoreById(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score>getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getScoresByPlayerId(UUID playerId) {
        return scoreRepository.findByPlayerId(playerId);
    }

    public List<Score> getLeaderboard(int limit) {
        return scoreRepository.findTopScores(limit);
    }

    public Optional<Score> getHighestScoreByPlayerId(UUID playerId) {
        List<Score> highestScores = scoreRepository.findHighestScoreByPlayerId(playerId);
        if (highestScores.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(highestScores.get(0));
        }
    }

    public List<Score> getScoresAboveValue(Integer minValue) {
        return scoreRepository.findByValueGreaterThan(minValue);
    }

    public List<Score> getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public Score updateScore(UUID scoreId, Score updatedScore) {
        Score existingScore = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Score does not exist"));

        if (updatedScore.getValue() != null) {
            existingScore.setValue(updatedScore.getValue());
        }

        return scoreRepository.save(existingScore);

    }

    public void deleteScore(UUID scoreId) {
        if (!scoreRepository.existsById(scoreId)) {
            throw new RuntimeException("Score does not exist");
        }
        else {
            scoreRepository.deleteById(scoreId);
        }
    }

    public void deleteScoresByPlayerId(UUID playerId) {
        List<Score> scores = scoreRepository.findByPlayerId(playerId);
        scoreRepository.deleteAll(scores);
    }
}

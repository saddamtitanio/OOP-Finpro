package com.game.backend.service;

import com.game.backend.model.HighscoreEntry;
import com.game.backend.util.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HighScoreService {

    private final String FILE = "data/highscores.json";

    public List<HighscoreEntry> getLeaderboard() {
        return JsonFileUtil.readList(FILE, HighscoreEntry[].class);
    }

    public void submitScore(int score) {
        List<HighscoreEntry> scores = new ArrayList<>(getLeaderboard());
        scores.add(new HighscoreEntry(score));
        scores.sort((a,b) -> b.getScore() - a.getScore());
        JsonFileUtil.write(FILE, scores);
    }
}

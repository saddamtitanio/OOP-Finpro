package com.game.backend.service;

import com.game.backend.model.HighscoreEntry;
import com.game.backend.util.JsonFileUtil;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HighScoreService {
    private final String FILE_PATH = "highscores.json";

    public void addScore(HighscoreEntry entry) {
        List<HighscoreEntry> scores = JsonFileUtil.readList(FILE_PATH, HighscoreEntry[].class);

        scores.add(entry);

        JsonFileUtil.write(FILE_PATH, scores);
    }

    public List<HighscoreEntry> getAllScores() {
        return JsonFileUtil.readList(FILE_PATH, HighscoreEntry[].class);
    }
}
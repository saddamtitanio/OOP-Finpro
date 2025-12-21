package com.game.backend.service;

import com.game.backend.model.RunSummary;
import com.game.backend.util.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RunSummaryService {

    private final String FILE = "data/runs.json";

    public void saveRun(RunSummary run) {
        List<RunSummary> runs = new ArrayList<>(
                JsonFileUtil.readList(FILE, RunSummary[].class)
        );
        runs.add(run);
        JsonFileUtil.write(FILE, runs);
    }

    public List<RunSummary> getRuns() {
        return JsonFileUtil.readList(FILE, RunSummary[].class);
    }
}

package com.game.backend.controller;

import com.game.backend.model.RunSummary;
import com.game.backend.service.RunSummaryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/run")
public class RunSummaryController {

    private final RunSummaryService service;

    public RunSummaryController(RunSummaryService service) {
        this.service = service;
    }

    @PostMapping
    public void save(@RequestBody RunSummary run) {
        service.saveRun(run);
    }

    @GetMapping
    public List<RunSummary> all() {
        return service.getRuns();
    }
}

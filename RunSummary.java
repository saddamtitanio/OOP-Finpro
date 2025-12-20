package com.game.backend.model;

import java.util.List;

public class RunSummary {
    public int waveReached;
    public int durationSeconds;
    public int kills;
    public boolean poisoned;
    public boolean died;
    public String deathMessage;
    public List<String> powerUpsUsed;
}
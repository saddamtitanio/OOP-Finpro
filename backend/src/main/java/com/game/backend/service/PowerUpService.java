package com.game.backend.service;

import com.game.backend.model.PowerUp;
import com.game.backend.util.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerUpService {

    private final String FILE = "data/powerups.json";

    public List<PowerUp> getAll() {
        return JsonFileUtil.readList(FILE, PowerUp[].class);
    }
}

package com.finpro.frontend.strategy;

import com.finpro.frontend.Player;

public interface PowerUpStrategy {
    void apply(Player player);
    void remove(Player player);
}

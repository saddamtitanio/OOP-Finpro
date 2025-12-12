package com.finpro.frontend.command;

import com.finpro.frontend.Player;


public interface Command {
    void execute(Player player);
}

package com.finpro.frontend.command;

import com.finpro.frontend.Player;

public class MoveUpCommand implements Command {
    @Override
    public void execute(Player player) {
        player.moveUp();
    }
}

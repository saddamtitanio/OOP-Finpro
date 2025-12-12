package com.finpro.frontend.command;
import com.finpro.frontend.Player;

public class InputHandler {
    private final Command shootCommand;
    private final Command moveCommand;


    public InputHandler() {
        shootCommand = new ShootCommand();
        moveCommand = new MoveCommand();
    }

    public void handleInput(Player player) {
        moveCommand.execute(player);
        shootCommand.execute(player);
    }
}

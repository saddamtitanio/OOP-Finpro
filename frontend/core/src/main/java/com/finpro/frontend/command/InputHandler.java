package com.finpro.frontend.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.finpro.frontend.Player;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    private final Map<Integer, Command> holdCommands = new HashMap<>();

    public InputHandler() {
        this.holdCommands.put(Input.Keys.A, new MoveLeftCommand());
        this.holdCommands.put(Input.Keys.D, new MoveRightCommand());
        this.holdCommands.put(Input.Keys.W, new MoveUpCommand());
        this.holdCommands.put(Input.Keys.S, new MoveDownCommand());

    }
    
    public void handleInput(Player player) {
        for (Map.Entry<Integer, Command> entry : holdCommands.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                entry.getValue().execute(player);
            }
        }
    }
}

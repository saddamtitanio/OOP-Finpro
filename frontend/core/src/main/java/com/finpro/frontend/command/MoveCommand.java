package com.finpro.frontend.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;

public class MoveCommand implements Command {
    @Override
    public void execute(Player player) {
        Vector2 moveDir = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveDir.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveDir.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveDir.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveDir.x += 1;

        if (!moveDir.isZero()) {
            player.move(moveDir.nor());
        }
    }
}

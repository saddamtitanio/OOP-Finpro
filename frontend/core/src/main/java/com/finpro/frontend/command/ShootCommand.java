package com.finpro.frontend.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Player;

public class ShootCommand implements Command {
    @Override
    public void execute(Player player) {
        Vector2 dir = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dir.y++;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dir.y--;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dir.x--;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dir.x++;

        if (!dir.isZero()) {
            player.fire(dir.nor());
        }
    }
}

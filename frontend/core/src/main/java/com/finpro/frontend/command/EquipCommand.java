package com.finpro.frontend.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.finpro.frontend.Player;

public class EquipCommand implements Command{
    @Override
    public void execute(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.activateStoredPowerUp();
        }
    }
}

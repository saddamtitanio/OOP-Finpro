package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.finpro.frontend.manager.LevelManager;
import com.finpro.frontend.manager.PowerUpManager;
import com.finpro.frontend.manager.ScoreManager;
import com.finpro.frontend.strategy.powerup.PowerUp;

public class HUD {
    private Stage stage;
    private Viewport viewport;

    private Label scoreLabel;
    private Label healthLabel;
    private Label powerUpLabel;
    private Label timerLabel;

    private ScoreManager scoreManager;

    public HUD(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;

        viewport = new FitViewport(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        stage = new Stage(viewport);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, font.getColor());

        scoreLabel = new Label("Score: 0", style);
        scoreLabel.setFontScale(2f);

        healthLabel = new Label("HP: 100", style);
        healthLabel.setFontScale(2f);

        powerUpLabel = new Label("Power-Up: ", style);
        powerUpLabel.setFontScale(2f);

        timerLabel = new Label("Timer: ", style);
        timerLabel.setFontScale(2f);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(scoreLabel)
            .expandX()
            .padTop(10)
            .padLeft(10)
            .left()
            .top();

        table.add(powerUpLabel)
            .expandX()
            .padTop(10)
            .center()
            .top();

        table.add(healthLabel)
            .expandX()
            .padTop(10)
            .padRight(10)
            .right()
            .top();

        table.row();

        table.add(timerLabel)
            .colspan(3)
            .padTop(40)
            .center()
            .top();

        stage.addActor(table);
    }

    public void update(Player player, LevelManager levelManager) {
        scoreLabel.setText("Score: " + scoreManager.getScore());
        healthLabel.setText(String.format("HP: %.1f", player.getHP()));
        PowerUp active = player.getStoredPowerUp();
        if (active != null) {
            powerUpLabel.setText("Power-Up: " + active.getClass().getSimpleName());
        } else {
            powerUpLabel.setText("Power-Up: None");
        }

        if (!levelManager.isBossLevel()) {
            float countdown = Math.max(0, levelManager.getLevelDuration() - levelManager.getLevelTimer());
            timerLabel.setText(String.format("Timer: %.1f", countdown));
        } else {
            timerLabel.setText("BOSS FIGHT");

        }
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}

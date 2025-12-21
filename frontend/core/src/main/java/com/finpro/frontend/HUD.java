package com.finpro.frontend;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.finpro.frontend.manager.ScoreManager;

public class HUD {
    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Label healthLabel;
    private ScoreManager scoreManager;

    public HUD(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;

        // Using a fixed virtual resolution so UI doesn't shrink/grow weirdly
        viewport = new FitViewport(640, 360);
        stage = new Stage(viewport);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, font.getColor());

        scoreLabel = new Label("Score: 0", style);
        scoreLabel.setFontScale(1.5f);

        healthLabel = new Label("HP: 100", style);
        healthLabel.setFontScale(1.5f);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // Padding adds space so text sits in the "sidebar" area
        table.add(scoreLabel).expandX().padTop(10).padLeft(20).left();
        table.add(healthLabel).expandX().padTop(10).padRight(20).right();

        stage.addActor(table);
    }

    public void update(float health) {
        scoreLabel.setText("Score: " + scoreManager.getScore());
        healthLabel.setText("HP: " + (int)health);
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

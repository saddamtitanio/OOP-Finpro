package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
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


        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(scoreLabel).expandX().padTop(10).left();
        table.add(healthLabel).expandX().padTop(10).right();

        stage.addActor(table);
    }

    public void update(float health) {
        scoreLabel.setText("Score: " + scoreManager.getScore());
        healthLabel.setText(String.format("HP: %.1f", health));
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

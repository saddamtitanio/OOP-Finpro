package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.finpro.frontend.manager.ScoreManager;

public class GameOverState implements GameState {
    private GameStateManager gsm;
    private Stage stage;
    private Skin skin;
    private ScoreManager scoreManager;

    public GameOverState(GameStateManager gsm, ScoreManager scoreManager) {
        this.gsm = gsm;
        this.scoreManager = scoreManager;

        stage = new Stage(new ExtendViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        createSkin();
        buildUI();
    }

    private void createSkin() {
        skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Pixmap white = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        white.setColor(Color.WHITE);
        white.fill();
        skin.add("white", new Texture(white));
        white.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.newDrawable("white", Color.GRAY);
        buttonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        buttonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", buttonStyle);
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("GAME OVER", skin);
        title.setColor(Color.RED);
        title.setFontScale(2f);

        Label score = new Label("SCORE: " + scoreManager.getScore(), skin);
        score.setFontScale(1f);

        TextButton playAgain = new TextButton("PLAY AGAIN", skin);
        TextButton exit = new TextButton("EXIT TO MENU", skin);

        playAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.startGame();
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.exitToMenu();
            }
        });

        table.add(title).padBottom(40f).row();
        table.add(score).padBottom(20f).row();
        table.add(playAgain).width(200).height(50).padBottom(20f).row();
        table.add(exit).width(200).height(50);

        stage.addActor(table);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(SpriteBatch batch) {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}


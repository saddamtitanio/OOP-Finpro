package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PauseState implements GameState{
    private GameStateManager gsm;
    private Stage stage;
    private Skin skin;
    private PlayState playState;
    private Texture whitePixel;

    public PauseState(GameStateManager gsm, PlayState playState){
        this.gsm = gsm;
        this.playState = playState;

        stage = new Stage(new ExtendViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        createSkin();
        buildPauseScreen();
        createWhitePixel();
    }

    private void createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    private void createSkin(){
        skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        skin.add("white", new Texture(whitePixmap));
        whitePixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.newDrawable("white", Color.GRAY);
        buttonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default", buttonStyle);
    }

    private void buildPauseScreen() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("GAME PAUSED", skin);
        title.setFontScale(2f);

        TextButton resumeButton = new TextButton("RESUME", skin);
        resumeButton.getLabel().setFontScale(1.2f);

        TextButton exitButton = new TextButton("EXIT TO MENU", skin);
        exitButton.getLabel().setFontScale(1.2f);

        table.add(title).padBottom(50f).row();
        table.add(resumeButton).width(200f).height(50f).padBottom(20f).row();
        table.add(exitButton).width(200f).height(50f);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.resumeGame();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.exitToMenu();
            }
        });

        stage.addActor(table);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(SpriteBatch batch) {
        playState.render(batch);

        batch.begin();
        batch.setColor(0, 0, 0, 0.3f);
        batch.draw(whitePixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
        batch.end();

        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        whitePixel.dispose();
    }
}

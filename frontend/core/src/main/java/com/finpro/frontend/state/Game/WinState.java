package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class WinState implements GameState{

    private Stage stage;
    private Skin skin;
    private GameStateManager gsm;

    private Label instructionLabel;
    private float blinkTimer = 0f;
    private boolean showInstruction = true;

    private float delay = 0f;
    private float delayDuration = 3f;

    public WinState(GameStateManager gsm){
        this.gsm = gsm;

        stage = new Stage(new ExtendViewport(800, 600));

        Gdx.input.setInputProcessor(stage);
        createSkin();
        buildWinScreen();

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
    }

    private void buildWinScreen(){
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("YOU WIN!", skin);
        title.setFontScale(4f);
        title.setColor(Color.GOLD);

        instructionLabel = new Label("Press any key to continue", skin);
        instructionLabel.setFontScale(1.5f);
        instructionLabel.setColor(Color.WHITE);

        Label victoryMessage = new Label("Boss Defeated!", skin);
        victoryMessage.setFontScale(2f);
        victoryMessage.setColor(Color.CYAN);

        table.add(title).padBottom(40f).row();
        table.add(victoryMessage).padBottom(30f).row();
        table.add(instructionLabel);
    }

    @Override
    public void update(float delta) {
        delay += delta;
        if (delay <= delayDuration) return;

        stage.addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                gsm.startMenu();
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.startMenu();
                return true;
            }

        });

        blinkTimer += delta;
        if (blinkTimer >= 0.5f) {
            showInstruction = !showInstruction;
            blinkTimer = 0f;

            if (instructionLabel != null) {
                instructionLabel.setVisible(showInstruction);
            }
        }

        stage.act(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

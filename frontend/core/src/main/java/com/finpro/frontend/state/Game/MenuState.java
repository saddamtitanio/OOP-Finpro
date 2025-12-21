package com.finpro.frontend.state.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.finpro.frontend.GameManager;

public class MenuState implements GameState{
    private Stage stage;
    private Skin skin;
    private GameStateManager gsm;

    public MenuState(GameStateManager gsm){
        this.gsm = gsm;

        stage = new Stage(new ExtendViewport(800, 600));

        Gdx.input.setInputProcessor(stage);
        createSkin();
        buildTitleScreen();
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
        buttonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", buttonStyle);

        TextField.TextFieldStyle fieldStyle = new TextField.TextFieldStyle();
        fieldStyle.font = font;
        fieldStyle.fontColor = Color.WHITE;
        fieldStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        fieldStyle.cursor = skin.newDrawable("white", Color.WHITE);
        skin.add("default", fieldStyle);
    }

    private void startGame(String username){
        if (username.isEmpty()) {
            username = "Player";
        }

        System.out.println("Starting game for: " + username);
        gsm.startGame();
    }

    private void buildTitleScreen(){
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("WHEN MORALES DIE", skin);
        title.setFontScale(3f);

        TextButton startButton = new TextButton("START", skin);
        startButton.getLabel().setFontScale(1.5f);

        TextButton quitButton = new TextButton("QUIT", skin);
        quitButton.getLabel().setFontScale(1.5f);

        table.add(title).padBottom(40f).row();
        table.add(startButton).width(200f).height(50f).padTop(20f).row();
        table.add(quitButton).width(200f).height(50f).padTop(10f);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildUsernameScreen();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }

    private void buildUsernameScreen(){
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label username = new Label("Enter your name:", skin);
        username.setFontScale(1.5f);

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("Username");

        TextButton playButton = new TextButton("PLAY", skin);

        table.add(username).padBottom(20f).row();
        table.add(nameField).width(250f).height(40f).padBottom(20f).row();
        table.add(playButton).width(150f).height(40f);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onPlayButtonClicked(nameField.getText().trim());
            }
        });

        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    onPlayButtonClicked(nameField.getText().trim());
                }
            }
        });

        stage.setKeyboardFocus(nameField);
    }

    private void onPlayButtonClicked(String username) {
        if (username.isEmpty()) {
            username = "Player";
        }
        startGame(username);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

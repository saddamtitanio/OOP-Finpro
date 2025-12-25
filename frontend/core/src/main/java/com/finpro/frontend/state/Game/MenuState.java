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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.finpro.frontend.GameManager;
import com.finpro.frontend.service.BackendService;

import java.util.UUID;

public class MenuState implements GameState{
    private Stage stage;
    private Skin skin;
    private GameStateManager gsm;
    private BackendService backendService;

    public MenuState(GameStateManager gsm){
        this.gsm = gsm;
        this.backendService = new BackendService();
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

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScroll = skin.newDrawable("white", Color.GRAY);
        scrollStyle.hScroll = skin.newDrawable("white", Color.GRAY);
        scrollStyle.background = skin.newDrawable("white", new Color(0,0,0,0.3f));
        skin.add("default", scrollStyle);

    }

    private void startGame(String username) {
        backendService.getPlayerByUsername(username, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                handleLogin(response, username);
            }

            @Override
            public void onError(int statusCode, String response) {
                if (statusCode == 404) {
                    backendService.createPlayer(username, new BackendService.RequestCallback() {
                        @Override
                        public void onSuccess(String response) {
                            handleLogin(response, username);
                        }

                        @Override
                        public void onError(int code, String err) {
                            Gdx.app.error("MenuState", "Failed to create player: " + err);
                        }
                    });
                } else {
                    Gdx.app.error("MenuState", "Failed to fetch player: " + response);
                }
            }
        });
    }

    private void handleLogin(String response, String username) {
        UUID playerId = extractPlayerId(response);

        Gdx.app.postRunnable(() -> {
            GameManager.getInstance().setPlayer(playerId, username);
            gsm.startGame();
        });
    }

    private UUID extractPlayerId(String json) {
        String id = json.split("\"playerId\":\"")[1].split("\"")[0];
        return UUID.fromString(id);
    }

    private void buildTitleScreen(){
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("WHEN MORALS DIE", skin);
        title.setFontScale(3f);

        TextButton startButton = new TextButton("START", skin);
        startButton.getLabel().setFontScale(1.5f);

        TextButton viewLeaderboardButton = new TextButton("LEADERBOARD", skin);
        viewLeaderboardButton.getLabel().setFontScale(1.5f);

        TextButton quitButton = new TextButton("QUIT", skin);
        quitButton.getLabel().setFontScale(1.5f);

        table.add(title).padBottom(40f).row();
        table.add(startButton).width(200f).height(50f).padTop(20f).row();
        table.add(viewLeaderboardButton).width(200f).height(50f).padTop(20f).row();
        table.add(quitButton).width(200f).height(50f).padTop(10f);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildUsernameScreen();
            }
        });

        viewLeaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildLeaderboardScreen();
            }
        });
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }

    private void buildLeaderboardScreen() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("Leaderboard", skin);
        title.setFontScale(2f);
        table.add(title).padBottom(70f).row();

        float rankWidth = 50f;
        float usernameWidth = 150f;
        float scoreWidth = 100f;

        Table headerTable = new Table(skin);
        headerTable.add(new Label("Rank", skin)).width(rankWidth).padRight(10f);
        headerTable.add(new Label("Username", skin)).width(usernameWidth).padRight(10f);
        headerTable.add(new Label("Score", skin)).width(scoreWidth).padRight(10f).row();

        table.add(headerTable).padBottom(10f).row();

        Table leaderboardRowsTable = new Table(skin);

        backendService.getGlobalLeaderboard(new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                JsonReader jsonReader = new JsonReader();
                JsonValue leaderboardJson = jsonReader.parse(response);

                leaderboardRowsTable.clear();

                for (int i = 0; i < leaderboardJson.size; i++) {
                    JsonValue runEntry = leaderboardJson.get(i);

                    String username = runEntry.getString("username");
                    int score = runEntry.getInt("score");

                    leaderboardRowsTable.add(new Label(String.valueOf(i + 1), skin)).width(rankWidth).padRight(10f);
                    leaderboardRowsTable.add(new Label(username, skin)).width(usernameWidth).padRight(10f);
                    leaderboardRowsTable.add(new Label(String.valueOf(score), skin)).width(scoreWidth).padRight(10f).row();
                }
            }

            @Override
            public void onError(int statusCode, String error) {
                Gdx.app.error("Leaderboard", "Failed to fetch leaderboard: " + error);

                Label errorLabel = new Label("Failed to fetch leaderboard: " + error, skin);
                errorLabel.setColor(Color.RED);
                errorLabel.setFontScale(1f);

                leaderboardRowsTable.clear();
                leaderboardRowsTable.add(errorLabel).colspan(4).center().padTop(20f).row();
            }

        });

        ScrollPane scrollPane = new ScrollPane(leaderboardRowsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).width(rankWidth + usernameWidth + scoreWidth + 200f)
            .height(250f).padBottom(20f).row();

        TextButton backButton = new TextButton("BACK", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new MenuState(gsm));
            }
        });
        table.add(backButton).width(150f).height(40f);
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
        TextButton backButton = new TextButton("BACK", skin);

        table.add(username).padBottom(20f).row();
        table.add(nameField).width(250f).height(40f).padBottom(20f).row();

        Table buttonTable = new Table();
        buttonTable.add(playButton).width(100f).height(40f).padRight(20f);
        buttonTable.add(backButton).width(100f).height(40f);

        table.add(buttonTable).row();

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

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(new MenuState(gsm));
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

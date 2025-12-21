package com.finpro.frontend.state.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.Boss;
import com.finpro.frontend.Player;

public class PlayState implements GameState {
    private GameStateManager gsm;
    private Player player;
    private Boss boss;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private float screenWidth;
    private float screenHeight;

    public PlayState(GameStateManager gsm){
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        this.gsm = gsm;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        Vector2 bossStart = new Vector2(screenWidth / 2, screenHeight / 2);
        Vector2 playerStart = new Vector2(screenWidth / 2, screenWidth * 0.1f);;

        player = new Player(new Vector2(playerStart));
        boss = new Boss(bossStart, screenWidth, screenHeight, shapeRenderer);
    }

    private void checkPauseButtonClicked(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gsm.pauseGame(this);
        }
    }

    @Override
    public void update(float delta) {
        player.update(delta);
        boss.update(delta, player.getPosition());
        checkPauseButtonClicked();
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.renderShape(shapeRenderer);
        boss.render(shapeRenderer);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}

package com.finpro.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.finpro.frontend.command.InputHandler;
import com.finpro.frontend.factory.BulletFactory;
import com.finpro.frontend.observer.EventManager;
import com.finpro.frontend.observer.event.FireEvent;
import com.finpro.frontend.observer.listener.ShootingListener;
import com.finpro.frontend.pool.BulletPool;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private InputHandler inputHandler;
    private BulletFactory bulletFactory;
    private BulletPool bulletPool;
    private EventManager eventManager;
    private ShootingListener shootingListener;
    private float strategyTimer = 0f;
    private boolean toggle = false;
    private final Array<Bullet> activeBullets = new Array<>();
    private OrthographicCamera camera;


    @Override
    public void create() {
        bulletPool = new BulletPool();
        bulletFactory = new BulletFactory(bulletPool);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        eventManager = new EventManager();
        shootingListener = new ShootingListener(bulletFactory);
        eventManager.subscribe(FireEvent.class, shootingListener);

        player = new Player(new Vector2(0, 0), eventManager);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float delta = Gdx.graphics.getDeltaTime();
        inputHandler.handleInput(player);
        player.update(delta);

        bulletPool.getActiveBullets(activeBullets);

        for (Bullet bullet : activeBullets) {
            bullet.update(delta);
            if (bullet.isOffScreen()) {
                bulletPool.release(bullet);
            }
        }

        // testing powerups
//        strategyTimer += delta;
//        if (strategyTimer >= 5f && !toggle) {
//            strategyTimer = 0f;
//            toggle = true;
//            player.applyTemporaryStrategy(new SpreadShot());
//        }
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);
        bulletPool.getActiveBullets(activeBullets);

        for (Bullet bullet : activeBullets) {
            bullet.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }
}

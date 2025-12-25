package com.finpro.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.command.BossCommands.BossAreaAttackCommand;
import com.finpro.frontend.command.BossCommands.BossCommand;
import com.finpro.frontend.command.BossCommands.BossDashAttackCommand;
import com.finpro.frontend.command.BossCommands.BossLaserAttackCommand;
import com.finpro.frontend.manager.BossManager.BossAttackManager;
import com.finpro.frontend.state.BossState.BossBehaviorState;
import com.finpro.frontend.state.BossState.BossNormalState;
import com.finpro.frontend.state.Game.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public class Boss {

    private Rectangle collider;
    private Vector2 position;
    private float width;
    private float height;

    private float attackTimer;
    private float attackInterval;

    private BossBehaviorState currentBehavior;
    private BossAttackManager attackManager;

    private final BossLaserAttackCommand laserAttackCommand;
    private final BossDashAttackCommand dashAttackCommand;
    private final BossAreaAttackCommand areaAttackCommand;

    private float worldWidth;
    private float worldHeight;

    private final List<BossCommand> attackCommands = new ArrayList<>();

    private float health = 10000;

    private GameStateManager gsm;

    private boolean isDead = false;

    public Boss(Vector2 startPosition, float worldWidth, float worldHeight, ShapeRenderer shapeRenderer){
        this.position = new Vector2(startPosition);
        this.width = 40f;
        this.height = 40f;

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.collider = new Rectangle(position.x - width/2, position.y - height/2, width, height);

        this.currentBehavior = new BossNormalState();
        this.currentBehavior.enter(this);
        this.attackManager = new BossAttackManager(shapeRenderer);

        this.attackTimer = 0f;
        this.attackInterval = 3f;

        this.laserAttackCommand = new BossLaserAttackCommand();
        this.dashAttackCommand = new BossDashAttackCommand();
        this.areaAttackCommand = new BossAreaAttackCommand();

        attackCommands.add(laserAttackCommand);
        attackCommands.add(dashAttackCommand);
        attackCommands.add(areaAttackCommand);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;

        System.out.println("Boss hit! Health: " + health);

        if (health <= 0) {
            die();
        }
    }

    private void die() {
        isDead = true;
        System.out.println("BOSS DEFEATED!");
    }

    public boolean isDead() {
        return isDead;
    }

    public void updateCollider(){
        collider.setPosition(position.x - width/2, position.y - height/2);
    }

    public void update(float delta, Vector2 playerPosition){

        currentBehavior.update(this, playerPosition, delta);
        updateCollider();
        attackManager.update(delta);

        for(BossCommand command : attackCommands){
            command.update(delta);
        }

        areaAttackCommand.setTargetPosition(playerPosition);

        attackTimer += delta;

        if(attackTimer >= attackInterval){
            attacking();
            attackTimer = 0f;
        }
    }

    public void attacking(){
        if(!(currentBehavior instanceof BossNormalState)){
            return;
        }

        List<BossCommand> executeCommand = new ArrayList<>();
        for(BossCommand command : attackCommands){
            if(command.isExecutable(this)){
                executeCommand.add(command);
            }
        }

        if(!executeCommand.isEmpty()){
            int randomIndex = MathUtils.random(executeCommand.size() - 1);
            executeCommand.get(randomIndex).execute(this);
        }
    }

    public void render(ShapeRenderer shapeRenderer){
        attackManager.render();
        shapeRenderer.setColor(currentBehavior.getBossColor());
        shapeRenderer.rect(position.x - width/2, position.y - height/2, width, height);
    }

    public void setBehavior(BossBehaviorState state){
        currentBehavior = state;
        currentBehavior.enter(this);
    }

    public void createAreaAttack(Vector2 position){
        attackManager.createAreaAttack(position);
    }

    public Vector2 getPosition(){
        return new Vector2(position);
    }

    public void setPosition(Vector2 position){
        this.position.set(position);
    }

    public BossBehaviorState getCurrentBehavior() {
        return currentBehavior;
    }

    public BossAttackManager getAttackManager() {
        return attackManager;
    }

    public float getWorldWidth() { return worldWidth; }

    public float getWorldHeight() { return worldHeight; }

    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }

    public float getHealth() {
        return health;
    }

    public Rectangle getCollider() {
        return collider;
    }
}

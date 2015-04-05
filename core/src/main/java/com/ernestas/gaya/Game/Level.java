package com.ernestas.gaya.Game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Backgrounds.LoopedBackground;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.Gameplay.Scenario;
import com.ernestas.gaya.Gameplay.Wave;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.HUD.HUD;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.Arsenal.Bullets.Bullet;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;
import com.ernestas.gaya.Validator.JSONToScenarioConverter;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private GayaEntry gaya;
    private InputProcessor input;

    private HUD hud;

//    Scenario
    private Scenario scenario;

//    Waves
    private Wave currentWave;

//    Background
    private LoopedBackground bg;
    private float backgroundSpeed = 30f;

//    Player
    private PlayerShip player;

//    Bullets
    private List<Bullet> bullets = new ArrayList<Bullet>();

    // Debug
    private boolean debug;
    private boolean paused;


    public Level(GayaEntry gaya, InputProcessor input) {
        this.gaya = gaya;
        this.input = input;
        hud = new HUD(this);
    }

    // Should be only called once
    public void setup() {
        //TODO: do stuff

        Sprite playerSprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipPlayer);
        player = new PlayerShip(this, playerSprite, new Vector2f(Settings.getInstance().getWidth() / 2, 100));

        Sprite bgSprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.background);
        bgSprite.setScale((Settings.getInstance().getWidth() / bgSprite.getWidth()), bgSprite.getScaleY());
        bg = new LoopedBackground(new Sprite(bgSprite), -backgroundSpeed, false);

        restartLevel();
    }

    public void restartLevel() {
        paused = false;
        currentWave = Wave.EMPTY_WAVE;
        bullets.clear();
        player.restart();

//        scenario = Scenario.getTestScenario();
        try {
            scenario = JSONToScenarioConverter.convertFromFile("Scenarios/scenario1.json");
        } catch (GayaException e) {
            e.printStackTrace();
            scenario = Scenario.getTestScenario();
        }
    }


    public void render(SpriteBatch batch) {
        // Background
        bg.render(batch);

        // Bullets
        for (Bullet bullet : bullets) {
            bullet.getSprite().draw(batch);
        }

        // Player
        player.getSprite().draw(batch);

        // Enemies
        List<Wave.EnemyWithOffset> enemyList = currentWave.getEnemyList();
        for (int index = 0; index < enemyList.size(); ++index) {
            EnemyShip enemy = enemyList.get(index).ship;
            enemy.getSprite().draw(batch);
        }

        hud.render(batch);

        if (debug) {
            batch.end();
            Rectangle rec = player.getBounds();
            ShapeRenderer renderer = new ShapeRenderer();

            renderer.begin(ShapeRenderer.ShapeType.Line);

            // player
            renderer.setColor(Color.RED);
            renderer.rect(rec.x, rec.y, rec.width, rec.height);

            // enemies
            renderer.setColor(Color.BLUE);
            for (int index = 0; index < enemyList.size(); ++index) {
                EnemyShip enemy = enemyList.get(index).ship;
                rec = enemy.getSprite().getBoundingRectangle();
                renderer.rect(rec.x, rec.y, rec.width, rec.height);
            }

            // bullets
            renderer.setColor(Color.PURPLE);
            for (Bullet bullet : bullets) {
                rec = bullet.getBounds();
                renderer.rect(rec.x, rec.y, rec.width, rec.height);
            }

            // pickups
            renderer.setColor(Color.GREEN);


            renderer.end();

            batch.begin();
        }
    }

    public void update(float delta) {
        if (input.isPressedAdvanced(Input.Keys.STAR)) {
            debug = !debug;
        }
        if (input.isPressedAdvanced(Input.Keys.P)) {
            paused = !paused;
        }
        if (input.isPressedAdvanced(Input.Keys.R)) {
            restartLevel();
        }
        if (input.isPressedAdvanced(Input.Keys.O)) {
            for (Wave.EnemyWithOffset ship : currentWave.getEnemyList()) {
                ship.ship.explode();
            }
        }

        if (paused) {
            return;
        }

        if (player.dead()) {
            System.out.println("PLAYER IS DEAD");
            return;
        }

        // Background
        bg.update(delta);

        // Player
        player.update(input, delta);

        // Waves
        if (currentWave.waveCompleted()) {
            currentWave = scenario.getNextWave();
            for (Wave.EnemyWithOffset enemy : currentWave.getEnemyList()) {
                enemy.ship.setLevel(this);
                enemy.ship.getAI().init(this, enemy.ship);
            }
        }

        // Enemies
        List<Wave.EnemyWithOffset> enemyList = currentWave.getEnemyList();
        for (int index = 0; index < enemyList.size(); ++index) {
            // update ship
            EnemyShip enemy = enemyList.get(index).ship;
            enemy.update(delta);

            // check collision
            if (player.collidesWith(enemy)) {
                System.out.println("COLLISION");
                enemy.hitFor(100);
                player.hitFor(enemy.getImpactDamage());
            }

            if (enemy.canRemove()) {
                enemyList.remove(index);
                --index;
            }
        }

        // Bullets
        for (int i = 0; i < bullets.size() ; ++i) {
            Bullet bullet = bullets.get(i);
            boolean hit = false;
            bullet.update(delta);
            if (bullet.getBounds().overlaps(player.getBounds()) && !bullet.getAuthor().equals(player)) {
                // hit player
                player.hitFor(bullet.getDamage());
                hit = true;
            } else {
                for (int index = 0; index < enemyList.size(); ++index) {
                    EnemyShip enemy = enemyList.get(index).ship;

                    if (bullet.getBounds().overlaps(enemy.getBounds()) && bullet.getAuthor().equals(player) &&
                        !enemy.isExploding()) {
                        // hit enemy
                        enemy.hitFor(bullet.getDamage());
                        hit = true;
                        if (enemy.getHealth() <= 0) {
                            player.addScore(enemy.getPoints());
                        }
                    }
                }
            }
            if (hit || !bullet.onScreen()) {
                bullets.remove(i);
                --i;
            }
        }

        // Is game finished?
        if (scenario.scenarioCompleted() && currentWave == Wave.EMPTY_WAVE) {
            System.out.println("GAME FINISHED");
        }
    }

    public void addBullets(List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            this.bullets.add(bullet);
        }
    }

    public PlayerShip getPlayer() { return player; }
}

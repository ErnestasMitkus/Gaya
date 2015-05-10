package com.ernestas.gaya.Gameplay;

import com.badlogic.gdx.Gdx;
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
import com.ernestas.gaya.Overlay.HUD;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.Overlay.ScreenDimmer;
import com.ernestas.gaya.Powerups.Powerup;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Screens.MenuScreen;
import com.ernestas.gaya.Overlays.Callbacks.LevelCallback;
import com.ernestas.gaya.Overlays.FadingOverlay;
import com.ernestas.gaya.Overlays.Items.RectangleItem;
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
    private ScreenDimmer screenDimmer;
    private boolean endingDimmer;

//    Scenario
    private Scenario scenario;

//    Waves
    private Wave currentWave;

//    Background
    private LoopedBackground bg;

//    Player
    private PlayerShip player;

//    Bullets
    private List<Bullet> bullets = new ArrayList<Bullet>();

    // Overlays
    private FadingOverlay pauseOverlay;
    private FadingOverlay endGameOverlay;
    private FadingOverlay victoryOverlay;

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

        pauseOverlay = new FadingOverlay(input);
        pauseOverlay.addItem(new RectangleItem(new Vector2f(0, 90), new LevelCallback(this, LevelCallback.RESUME_LEVEL),
                                RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
                                .withText("Resume"));
        pauseOverlay.addItem(new RectangleItem(new Vector2f(0, 30), new LevelCallback(this, LevelCallback.RESTART_LEVEL),
                                RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
                                .withText("Restart"));
        pauseOverlay.addItem(new RectangleItem(new Vector2f(0, -30), new LevelCallback(this, LevelCallback.VIEW_OPTIONS),
                                RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
                                .withText("Options"));
        pauseOverlay.addItem(new RectangleItem(new Vector2f(0, -90), new LevelCallback(this, LevelCallback.EXIT_TO_MENU),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
                                .withText("Exit to menu"));
        pauseOverlay.setFadeIn(0.5f);

        endGameOverlay = new FadingOverlay(input);
        endGameOverlay.addItem(new RectangleItem(new Vector2f(0, 30), new LevelCallback(this, LevelCallback.RESTART_LEVEL),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Restart"));
        endGameOverlay.addItem(new RectangleItem(new Vector2f(0, -30), new LevelCallback(this, LevelCallback.EXIT_TO_MENU),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Exit to menu"));
        endGameOverlay.setFadeIn(1f);

        victoryOverlay = new FadingOverlay(input);
        victoryOverlay.addItem(new RectangleItem(new Vector2f(0, 30), null,
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL, false)
            .withText("YOU WON!"));
        victoryOverlay.addItem(new RectangleItem(new Vector2f(0, -30), new LevelCallback(this, LevelCallback.EXIT_TO_MENU),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Exit to menu"));
        victoryOverlay.setFadeIn(0.5f);

        Sprite playerSprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipPlayer);
        player = new PlayerShip(this, playerSprite, new Vector2f(Settings.getInstance().getWidth() / 2, 100));

        Sprite bgSprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.background);
        bgSprite.setScale((Settings.getInstance().getWidth() / bgSprite.getWidth()), bgSprite.getScaleY());
        bg = new LoopedBackground(new Sprite(bgSprite), -GameSettings.getInstance().getGameSpeed(), false);

        restartLevel();
    }

    public void restartLevel() {
        paused = false;
        currentWave = Wave.EMPTY_WAVE;
        bullets.clear();
        screenDimmer = new ScreenDimmer(1f, 0f, 1f);
        player.restart();
        endingDimmer = false;
        pauseOverlay.reset();
        endGameOverlay.reset();
        victoryOverlay.reset();

        // Save backup scenario somewhere
        try {
            if (gaya.scenarioPath != null) {
                System.out.println("Launching scenario from file \"" + gaya.scenarioPath + "\"");
                scenario = JSONToScenarioConverter.convertFromFile(gaya.scenarioPath);
            } else {
                System.out.println("Launching scenario resource stream");
                scenario = JSONToScenarioConverter.convert(Gdx.files.internal("Scenarios/scenario1.json").readString());
            }
        } catch (GayaException e) {
            e.printStackTrace();
            System.err.println("[WARN] Using default scenario.");
            scenario = Scenario.getTestScenario();
        }
    }


    public void render(SpriteBatch batch) {
        // Background
        bg.render(batch);

        // Powerups
        for (Powerup powerup : currentWave.getPowerupList()) {
            powerup.getSprite().draw(batch);
        }

        // Bullets
        for (Bullet bullet : bullets) {
            bullet.getSprite().draw(batch);
        }

        // Player
        player.getSprite().draw(batch);

        // Enemies
        for (Wave.EnemyWithOffset anEnemyList : currentWave.getEnemyList()) {
            EnemyShip enemy = anEnemyList.ship;
            enemy.getSprite().draw(batch);
        }

        hud.render(batch);
        if (!screenDimmer.done() || endingDimmer) {
            screenDimmer.getSprite().draw(batch);
        }

        if (showEndMenu()) {
            endGameOverlay.render(batch);
        }

        // Is game finished?
        if (gameFinished()) {
            victoryOverlay.render(batch);
        }

        if (paused) {
            pauseOverlay.render(batch);
        }

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
            for (Wave.EnemyWithOffset anEnemyList : currentWave.getEnemyList()) {
                rec = anEnemyList.ship.getSprite().getBoundingRectangle();
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
            for (Powerup powerup : currentWave.getPowerupList()) {
                rec = powerup.getBounds();
                renderer.rect(rec.x, rec.y, rec.width, rec.height);
            }

            // endGameOverlay
            if (showEndMenu()) {
                renderer.setColor(Color.CYAN);
                endGameOverlay.renderBounds(renderer);
            }

            if (paused) {
                renderer.setColor(Color.CYAN);
                pauseOverlay.renderBounds(renderer);
            }

            renderer.end();

            batch.begin();
        }
    }

    public void update(float delta) {
        if (input.isPressedAdvanced(Input.Keys.STAR)) {
            debug = !debug;
        }
        if (input.isPressedAdvanced(Input.Keys.ESCAPE)) {
            if (!paused) {
                paused = true;
                pauseOverlay.reset();
            }
        }
        if (input.isPressedAdvanced(Input.Keys.R)) {
            restartLevel();
        }
        if (input.isPressedAdvanced(Input.Keys.O)) {
            for (Wave.EnemyWithOffset ship : currentWave.getEnemyList()) {
                ship.ship.explode();
            }
        }
        if (input.isPressedAdvanced(Input.Keys.K)) {
            player.hitFor(player.getHealth());
        }

        if (paused) {
            pauseOverlay.update(delta);
            return;
        }

        if (showEndMenu()) {
            endGameOverlay.update(delta);
        }

        // Is game finished?
        if (gameFinished()) {
            victoryOverlay.update(delta);
        }

        if (!screenDimmer.done()) {
            screenDimmer.update(delta);
        }

        if (player.dead()) {
            if (!endingDimmer) {
                screenDimmer = new ScreenDimmer(0f, 0.6f, 1.5f);
                endingDimmer = true;
            }
            return;
        }

        // Background
        bg.update(delta);

        // Player
        player.update(input, delta);

        // Waves
        if (currentWave.waveCompleted() && screenDimmer.done()) {
            currentWave = scenario.getNextWave(currentWave);
            for (Wave.EnemyWithOffset enemy : currentWave.getEnemyList()) {
                enemy.ship.setLevel(this);
                enemy.ship.getAI().init(this, enemy.ship);
            }
        }

        // Powerup
        List<Powerup> powerupList = currentWave.getPowerupList();
        for (int i = 0; i < powerupList.size(); ++i) {
            Powerup powerup = powerupList.get(i);
            powerup.update(delta);

            if (powerup.getBounds().overlaps(player.getBounds())) {
                powerup.effect(this, player);
            }

            if (powerup.canRemove()) {
                powerupList.remove(i);
                --i;
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
                for (Wave.EnemyWithOffset anEnemyList : enemyList) {
                    EnemyShip enemy = anEnemyList.ship;

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
    }

    public void addBullets(List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            this.bullets.add(bullet);
        }
    }

    public void pause() { paused = true; }
    public void unpause() {
        paused = false;
    }

    public void exitToMenu() {
        gaya.setScreen(new MenuScreen(gaya, 1f));
    }

    public boolean gameFinished() { return scenario.scenarioCompleted() && currentWave == Wave.EMPTY_WAVE; }
    public PlayerShip getPlayer() { return player; }
    public InputProcessor getInput() { return input; }
    private boolean showEndMenu() { return endingDimmer && screenDimmer.done(); }
}

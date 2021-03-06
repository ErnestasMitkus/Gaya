package com.ernestas.gaya.Ships;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.ResourceLoaders.SpriteScaler;
import com.ernestas.gaya.Ships.Arsenal.Arsenal;
import com.ernestas.gaya.Ships.Arsenal.ArsenalWrapper;
import com.ernestas.gaya.Spritesheet.Animation;
import com.ernestas.gaya.Util.SerializationRepair;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

import java.io.Serializable;

public class PlayerShip extends Ship  implements Serializable, SerializationRepair {
    private static final long serialVersionUID = 6425371864150261489L;

    private static final int MAX_HEALTH = 30;
    private static final float DEFAULT_SPEED = 150f;
    private static final float INVULNERABILITY_TIME = 1f;

    private transient Sprite sprite = null;
    private int health = MAX_HEALTH;
    private float speed = DEFAULT_SPEED;

    private ArsenalWrapper arsenalWrapper;

    private int score;

    private float invulnerabilityTime = 0;
    private float blinkPeriod = 0.25f;
    private float timeSinceLastBlinked = 0;
    private boolean visible = true;

    /**
     *
     * @param level current level
     * @param position initial position
     */
    public PlayerShip(Level level, Vector2f position) {
        super(level, position);
        arsenalWrapper = new ArsenalWrapper(new Arsenal(this), 0.5f);
    }

    /**
     *
     * @param level current level
     * @param resId image's resource id
     * @param position initial position
     */
    public PlayerShip(Level level, ResourceLoader.ResourceId resId, Vector2f position) {
        this(level, position);
        this.resId = resId;
        this.sprite = GameSettings.getInstance().getResourceLoader().getResource(resId);
        setBounds(sprite.getBoundingRectangle());
        setPosition(getPosition());
    }

    /**
     * resets ship's info
     */
    public void restart() {
        arsenalWrapper.getArsenal().resetBulletType();
        score = 0;
        invulnerabilityTime = 0;
        health = MAX_HEALTH;
        exploding = false;
        explodingDone = false;
        explosionAnimation.restart();
    }

    /**
     *
     * @return ship's image or explosion animation if ship is exploding
     */
    public Sprite getSprite() {
        Sprite result;
        if (exploding) {
            result  = explosionAnimation.getSprite();

            float percentage = (1 - explosionAnimation.percentageDone()) * 100;

            result = SpriteScaler.lowerSpriteZAxis(result, percentage);
        } else {
            if (!vulnerable()) {
                if (timeSinceLastBlinked >= blinkPeriod) {
                    timeSinceLastBlinked -= blinkPeriod;
                    visible = !visible;
                }
            }
            if (visible) {
                result = sprite;
            } else {
                result = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.emptySprite);
            }
        }

        if (result != null) {
            result.setPosition(position.x - bounds.getWidth() / 2, position.y - bounds.getHeight() / 2);
        }

        return result;
    }

    /**
     * Update method, which should be called every frame
     * @param input input processor for player's input handling
     * @param delta time since last frame
     */
    public void update(InputProcessor input, float delta) {
        super.update(delta);

        if (isExploding() || exploded()) {
            return;
        }

        if (health <= 0) {
            exploding = true;
        }

        arsenalWrapper.update(delta);
        if (!vulnerable()) {
            invulnerabilityTime -= delta;
            timeSinceLastBlinked += delta;
            if (invulnerabilityTime < 0) {
                invulnerabilityTime = 0;
            }
        } else {
            visible = true;
        }

        // Movement
        float vecX = 0;
        float vecY = 0;

        if (input.isPressed(Input.Keys.A) || input.isHold(Input.Keys.A)) { //hold not needed?
            vecX += -1;
        }
        if (input.isPressed(Input.Keys.D) || input.isHold(Input.Keys.D)) {
            vecX += 1;
        }

        if (input.isPressed(Input.Keys.W) || input.isHold(Input.Keys.W)) {
            vecY += 1;
        }
        if (input.isPressed(Input.Keys.S) || input.isHold(Input.Keys.S)) {
            vecY += -1;
        }

        if (vecX != 0)
            move(vecX, 0, delta);
        if (vecY != 0)
            move(0, vecY, delta);


        // Shooting
        if (Settings.getInstance().getAutoShoot() || input.isPressed(Input.Keys.SPACE)) {
            arsenalWrapper.shoot();
        }

        if (input.isPressedAdvanced(Input.Keys.NUMPAD_1)) {
            arsenalWrapper.getArsenal().setBulletType(Arsenal.BulletType.singleBullet);
        }
        if (input.isPressedAdvanced(Input.Keys.NUMPAD_2)) {
            arsenalWrapper.getArsenal().setBulletType(Arsenal.BulletType.doubleBullet);
        }
        if (input.isPressedAdvanced(Input.Keys.NUMPAD_3)) {
            arsenalWrapper.getArsenal().setBulletType(Arsenal.BulletType.tripleBullet);
        }
        if (input.isPressedAdvanced(Input.Keys.NUMPAD_4)) {
            arsenalWrapper.getArsenal().setBulletType(Arsenal.BulletType.pentaBullet);
        }
    }

    private void move(float vecX, float vecY, float delta) {
        float x = getPosition().x + vecX * speed * delta * Settings.getInstance().getScale();
        float y = getPosition().y + vecY * speed * delta * Settings.getInstance().getScale();

        float boX = bounds.width / 2;
        float boY = bounds.height / 2;

        if (x - boX >= 0 && x + boX <= Settings.getInstance().getWidth() &&
            y - boY >= 0 && y + boY <= Settings.getInstance().getHeight()) {

            setPosition(x, y);
        }
    }

    /**
     * Method to damage the ship
     * (Won't get damaged if ship is invulnerable)
     * @param damage how much damage the ship should take
     */
    public void hitFor(int damage) {
        if (vulnerable()) {
            health -= damage;
            if (health < 0) {
                health = 0;
            }
            invulnerabilityTime = INVULNERABILITY_TIME;
            timeSinceLastBlinked = blinkPeriod;
        }
    }

    /**
     *
     * @return True if ship is exploded and the explosion animation is done
     */
    public boolean dead() { return health <= 0 && exploded(); }

    /**
     *
     * @return True, if ship is not invulnerable
     */
    public boolean vulnerable() { return invulnerabilityTime <= 0; }

    /**
     * Heals the ship
     * @param amount how much to heal the ship
     */
    public void heal(int amount) {
        health = Math.min(MAX_HEALTH, health + amount);
    }

    /**
     *
     * @return ship's current health
     */
    public int getHealth() { return health; }

    /**
     *
     * @return ship's max health
     */
    public int getMaxHealth() { return MAX_HEALTH; }

    /**
     *
     * @param points amount to add to the ship's score
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     *
     * @return ship's score
     */
    public int getScore() { return score; }

    /**
     *
     * @return ship's Arsenal
     */
    public Arsenal getArsenal() {
        return arsenalWrapper.getArsenal();
    }

    /**
     * Method to call after loading the object from serialization
     */
    @Override
    public void repair() {
        arsenalWrapper.repair();
        init();
        this.sprite = GameSettings.getInstance().getResourceLoader().getResource(resId);
    }
}

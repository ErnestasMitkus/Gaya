package com.ernestas.gaya.Ships;

import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Spritesheet.Animation;
import com.ernestas.gaya.Spritesheet.Spritesheet;
import com.ernestas.gaya.Util.Printable;
import com.ernestas.gaya.Util.SerializationRepair;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

import java.io.Serializable;

public abstract class Ship implements Printable, Serializable, SerializationRepair {
    private static final long serialVersionUID = 6435071864150261489L;

    protected transient Animation explosionAnimation;
    /**
     * ship's center position
     */
    protected Vector2f position;
    /**
     * ship's image's bounding rectangle
     */
    protected Rectangle bounds;
    protected ResourceLoader.ResourceId resId = null;

    protected boolean exploding = false;
    protected boolean explodingDone = false;

    protected Level level;


    public Ship() { this(null); }

    /**
     *
     * @param level current level
     */
    public Ship(Level level) {
        this(level, new Vector2f());
    }

    /**
     *
     * @param level current level
     * @param position initial position
     */
    public Ship(Level level, Vector2f position) {
        this.level = level;
        this.position = position;
        this.bounds = new Rectangle(0, 0, 0, 0);
        init();
    }

    /**
     * Initialization method, which should be called from any child class
     */
    protected void init() {
        Spritesheet spritesheet = new Spritesheet(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.explosionSS).getTexture(),
            32, 32, 14);
        explosionAnimation = new Animation(spritesheet, (int) position.x, (int) position.y, 8, Animation.frameRateToDeltaRate(Settings.getInstance().getFrameRate()));
    }

    /**
     * Update method, which should be called every frame
     * @param delta time since last frame
     */
    public void update(float delta) {
        if (exploding && !explodingDone) {
            explosionAnimation.update(delta);
            if (explosionAnimation.iterationDone()) {
                explodingDone = explosionAnimation.iterationDone();
            }
        }
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2f(x, y));
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        setBounds(position.x - bounds.width / 2, position.y - bounds.height / 2, bounds.width, bounds.height);
    }

    public void setBounds(float x, float y, float width, float height) {
        setBounds(new Rectangle(x, y, width, height));
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Explodes the ship, causing to replace it's sprite with an explosion animation
     */
    public void explode() {
        exploding = true;
    }

    /**
     *
     * @return ship's center position
     */
    public Vector2f getPosition() { return position; }

    /**
     *
     * @return ship's image's bounding rectangle
     */
    public Rectangle getBounds() { return bounds; }

    public void setLevel(Level level) { this.level = level; }

    /**
     *
     * @return level the ship is in
     */
    public Level getLevel() { return level; }

    /**
     *
     * @param ship
     * @return true if ship collides with any other not exploding ship
     */
    public boolean collidesWith(Ship ship) {
        if (this.getBounds().overlaps(ship.getBounds())) {
            if (!this.exploding && !ship.exploding) {
                return true;
            }
        }
        return false;
    }

    // Final, because we don't want out children to override this method

    /**
     * Prints out ship's info to the console
     */
    public final void println() {
        System.out.println(this.toString());
    }

    /**
     *
     * @return ship's representation as a string
     */
    @Override
    public String toString() {
        return this.getClass() + " position:["  + position.toString() + "] bounds:[" + bounds.toString() + "]";
    }

    /**
     *
     * @return true if ship is exploding
     */
    public boolean isExploding() {
        return exploding;
    }

    /**
     *
     * @return true if ship's explosion animation has been completed
     */
    public boolean exploded() { return explodingDone; }
}

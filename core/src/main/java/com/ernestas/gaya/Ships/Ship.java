package com.ernestas.gaya.Ships;

import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Spritesheet.Animation;
import com.ernestas.gaya.Spritesheet.Spritesheet;
import com.ernestas.gaya.Util.Printable;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public abstract class Ship implements Printable {

    protected Animation explosionAnimation;
    protected Vector2f position;
    protected Rectangle bounds;

    protected boolean exploding = false;
    protected boolean explodingDone = false;

    protected Level level;

    /*
    *   Position    - position of the ship's center
    *   Bounds      - ship's bounding rectangle
    */

    public Ship() { this(null); }

    public Ship(Level level) {
        this(level, new Vector2f());
    }

    public Ship(Level level, Vector2f position) {
        this.level = level;
        this.position = position;
        this.bounds = new Rectangle(0, 0, 0, 0);
        init();
    }

    private void init() {
        Spritesheet spritesheet = new Spritesheet(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.explosionSS).getTexture(),
            32, 32, 14);
        explosionAnimation = new Animation(spritesheet, (int) position.x, (int) position.y, 8, Animation.frameRateToDeltaRate(Settings.getInstance().getFrameRate()));
    }

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

    public void explode() {
        exploding = true;
    }

    public Vector2f getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }

    public void setLevel(Level level) { this.level = level; }
    public Level getLevel() { return level; }

    public boolean collidesWith(Ship ship) {
        if (this.getBounds().overlaps(ship.getBounds())) {
            if (!this.exploding && !ship.exploding) {
                return true;
            }
        }
        return false;
    }

    // Final, because we don't want out children to override this method
    public final void println() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return this.getClass() + " position:["  + position.toString() + "] bounds:[" + bounds.toString() + "]";
    }

    public boolean isExploding() {
        return exploding;
    }
    public boolean exploded() { return explodingDone; }
}

package com.ernestas.gaya.Powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.SerializationRepair;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

import java.io.Serializable;

public abstract class Powerup implements Serializable, SerializationRepair {
    private static final long serialVersionUID = 6435371844150261489L;

    protected transient Sprite sprite;
    protected float floatSpeed;
    protected boolean canRemove = false;

    public Powerup(Sprite sprite, Vector2f position) {
        this.sprite = sprite;
        sprite.setPosition(position.x, position.y);
        this.floatSpeed = GameSettings.getInstance().getGameSpeed() * 1.5f;
    }

    public void update(float delta) {
        float y = getPosition().y - floatSpeed * delta;
        sprite.setPosition(getPosition().x, y);
    }

    public void effect(Level level, PlayerShip ship) {
        canRemove = true;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setPosition(float x, float y) { sprite.setPosition(x, y); }
    public Vector2f getPosition() {
        return new Vector2f(sprite.getX(), sprite.getY());
    }

    public Rectangle getBounds() { return sprite.getBoundingRectangle(); }

    public boolean canRemove() {
        return canRemove || getPosition().y < -getBounds().height;
    }

    public static Powerup getPowerupFromString(String powerupName) {
        if (powerupName.equalsIgnoreCase("health")) {
            return new HealthPowerup();
        } else if (powerupName.equalsIgnoreCase("bullet")) {
            return new BulletPowerup();
        }

        return null;
    }

}

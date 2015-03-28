package com.ernestas.gaya.Ships.Arsenal.Bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Ships.Ship;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public abstract class Bullet {

    private float bulletXOffset = 7;

    protected Ship author;

    protected Sprite sprite;
    protected float speed;
    protected Vector2f vector;
    protected int damage;

    public Bullet(Ship author, Sprite sprite, float speed, Vector2f flyVector, Vector2f position, int damage) {
        this.author = author;
        this.sprite = sprite;
        this.speed = speed;
        this.vector = flyVector.onetized();
        this.damage = damage;

        float rotationDegrees = (float) (Math.toDegrees(Math.atan2(this.vector.x, this.vector.y)));

        sprite.setPosition(position.x + bulletXOffset, position.y);

        sprite.setOriginCenter();
        sprite.setRotation(360 - rotationDegrees);
    }

    public void update(float delta) {
        Vector2f currentPos = getPosition();
        Vector2f newPos = new Vector2f(currentPos.x + vector.x * speed * delta * Settings.getInstance().getScale(),
                                    currentPos.y + vector.y * speed * delta * Settings.getInstance().getScale());

        sprite.setPosition(newPos.x, newPos.y);
    }

    public Rectangle getBounds() { return sprite.getBoundingRectangle(); }

    public Vector2f getPosition() {
        return new Vector2f(sprite.getX(), sprite.getY());
    }

    public void setAuthor(Ship author) {
        this.author = author;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


    public Ship getAuthor() {
        return author;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean onScreen() {
        Rectangle rec = new Rectangle(0, 0, 0, 0);
        rec.width = Settings.getInstance().getWidth();
        rec.height = Settings.getInstance().getHeight();

        return rec.overlaps(this.getBounds());
    }
}

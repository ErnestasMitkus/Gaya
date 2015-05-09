package com.ernestas.gaya.Ships;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.AI.AI;
import com.ernestas.gaya.ResourceLoaders.SpriteScaler;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class EnemyShip extends Ship {

    public static class Builder {
        private EnemyShip ship;

        public Builder() {
            ship = new EnemyShip();
        }

        public Builder withSprite(Sprite sprite) {
            ship.setSprite(sprite);
            return this;
        }

        public Builder withHealth(int health) {
            ship.setHealth(health);
            return this;
        }

        public Builder withSpeed(float speed) {
            ship.setSpeed(speed);
            return this;
        }

        public Builder withAI(AI ai) {
            ship.setAI(ai);
            return this;
        }

        public Builder withPosition(Vector2f pos) {
            ship.setPosition(pos.x, pos.y);
            return this;
        }

        public EnemyShip build() {
            return ship;
        }

    }

    private Sprite sprite;
    private int health;
    private float speed;

    private boolean canRemove = false;

    private int points = 100;
    private int impactDamage = 10;
    private AI ai;

    private EnemyShip() {
        this(null, 0, 0f, null);
    }

    public EnemyShip(Sprite sprite, int health, float speed, AI ai) {
        this.sprite = sprite;
        this.health = health;
        this.speed = speed;
        this.ai = ai;
    }

    public void update(float delta) {
        super.update(delta);

        if (ai != null) {
            ai.update(delta);
        }

        if (health <= 0) {
            exploding = true;
        }

        if (getPosition().y + sprite.getHeight() < 0) {
            canRemove = true;
        }
    }

    public boolean canRemove() {
        return canRemove || exploded();
    }

    public Sprite getSprite() {
        Sprite result;

        if (exploding) {
            result = explosionAnimation.getSprite();

            float percentage = (1 - explosionAnimation.percentageDone()) * 100;

            result = SpriteScaler.lowerSpriteZAxis(result, percentage);
        } else {
            result = sprite;
        }
        if (result != null) {
            result.setPosition(position.x - bounds.getWidth() / 2, position.y - bounds.getHeight() / 2);
        }
        return result;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        setBounds(sprite.getBoundingRectangle());
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void hitFor(int damage) { this.health -= damage; }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getPoints() { return points; }

    public int getImpactDamage() { return impactDamage; }

    public void setAI(AI ai) { this.ai = ai; }

    public AI getAI() { return ai; }

}

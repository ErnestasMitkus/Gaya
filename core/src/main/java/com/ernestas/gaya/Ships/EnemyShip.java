package com.ernestas.gaya.Ships;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Game.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.ResourceLoaders.SpriteScaler;
import com.ernestas.gaya.Spritesheet.Animation;
import com.ernestas.gaya.Spritesheet.Spritesheet;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
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

        public Builder withPosition(Vector2f pos) {
            ship.setPosition(pos.x, pos.y);
            return this;
        }

        public EnemyShip build() {
            return ship;
        }

    }

    private Animation explosionAnimation;
    private Sprite sprite;
    private int health;
    private float speed;

    private boolean canRemove = false;

    private int points = 100;
    private int impactDamage = 10;

    private EnemyShip() {
        this.sprite = null;
        this.health = 0;
        this.speed = 0f;
        init();
    }

    public EnemyShip(Sprite sprite, int health, float speed) {
        this.sprite = sprite;
        this.health = health;
        this.speed = speed;
        init();
    }

    private void init() {
        Spritesheet spritesheet = new Spritesheet(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.explosionSS).getTexture(),
            32, 32, 14);
        explosionAnimation = new Animation(spritesheet, (int) position.x, (int) position.y, 8, Animation.frameRateToDeltaRate(Settings.getInstance().getFrameRate()));
    }

    public void update(float delta) {
        //TODO: AI
        if (health <= 0) {
            exploding = true;
        }

        if (exploding) {
            explosionAnimation.update(delta);
            if (explosionAnimation.iterationDone()) {
                canRemove = explosionAnimation.iterationDone();
            }
        }

        float flySpeed = exploding ? -10f : speed;
        setPosition(position.x, position.y - flySpeed * delta * Settings.getInstance().getScale());
        if (getPosition().y + sprite.getHeight() < 0) {
            canRemove = true;
        }
    }

    public boolean canRemove() {
        return canRemove;
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

}

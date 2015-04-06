package com.ernestas.gaya.Powerups;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Game.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class BulletPowerup extends Powerup {

    public BulletPowerup() {
        this(new Vector2f(0, 0));
    }

    public BulletPowerup(Vector2f position) {
        super(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.bulletPowerup), position);
    }

    @Override
    public void effect(Level level, PlayerShip ship) {
        if (!canRemove) {
            super.effect(level, ship);
            ship.getArsenal().upgradeBulletType();
        }
    }
}

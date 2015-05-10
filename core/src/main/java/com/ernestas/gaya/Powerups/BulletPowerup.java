package com.ernestas.gaya.Powerups;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

import java.io.Serializable;

public class BulletPowerup extends Powerup implements Serializable {
    private static final long serialVersionUID = 6431581844150261489L;

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

    @Override
    public void repair() {
        sprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.bulletPowerup);
    }
}

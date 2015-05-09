package com.ernestas.gaya.Powerups;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class HealthPowerup extends Powerup {

    private static int HEAL_AMOUNT = 10;

    public HealthPowerup() {
        this(new Vector2f(0, 0));
    }

    public HealthPowerup(Vector2f position) {
        super(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.healthPowerup), position);
    }

    @Override
    public void effect(Level level, PlayerShip ship) {
        if (!canRemove) {
            super.effect(level, ship);
            ship.heal(HEAL_AMOUNT);
        }
    }
}

package com.ernestas.gaya.AI;

import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.Game.Level;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Util.Settings.Settings;

public class FloatAI implements AI {

    private Level level;
    private EnemyShip ship;
    private boolean initialized = false;

    public void init(Level level, EnemyShip ship) {
        this.level = level;
        this.ship = ship;
        initialized = true;
    }

    @Override
    public void update(float delta) {
        if (!initialized) {
            System.err.println("UNINITIALIZED AI. EXPLODING SHIP");
            ship.explode();
        }

        float flySpeed = ship.isExploding() ? -10f : ship.getSpeed();
        ship.setPosition(ship.getPosition().x, ship.getPosition().y - flySpeed * delta * Settings.getInstance().getScale());
    }


    @Override
    public AI clone() {
        return new FloatAI();
    }
}

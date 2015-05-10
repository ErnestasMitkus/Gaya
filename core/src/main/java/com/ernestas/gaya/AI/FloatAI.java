package com.ernestas.gaya.AI;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Util.SerializationRepair;
import com.ernestas.gaya.Util.Settings.Settings;

import java.io.Serializable;

public class FloatAI implements AI, Serializable, SerializationRepair {
    private static final long serialVersionUID = 6435333864150261489L;

    protected Level level;
    protected EnemyShip ship;
    protected boolean initialized = false;

    @Override
    public void init(Level level, EnemyShip ship) {
        this.level = level;
        this.ship = ship;
        initialized = true;
    }

    @Override
    public void update(float delta) {
        if (initialized) {
            float flySpeed = ship.isExploding() ? -10f : ship.getSpeed();
            ship.setPosition(ship.getPosition().x, ship.getPosition().y - flySpeed * delta * Settings.getInstance().getScale());
        }
    }


    @Override
    public AI clone() {
        return new FloatAI();
    }

    @Override
    public void repair() {

    }
}

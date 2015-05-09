package com.ernestas.gaya.AI;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Ships.Arsenal.Arsenal;
import com.ernestas.gaya.Ships.Arsenal.ArsenalWrapper;
import com.ernestas.gaya.Ships.EnemyShip;

public class ShooterAI extends FloatAI {

    protected ArsenalWrapper arsenalWrapper;
    private int damage;

    public ShooterAI() {
        this(1);
    }

    public ShooterAI(int damage) {
        this.damage = Math.max(damage, 1);
    }

    @Override
    public void init(Level level, EnemyShip ship) {
        super.init(level, ship);
        arsenalWrapper = new ArsenalWrapper(new Arsenal(ship, damage), 1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (initialized) {
            if (!ship.isExploding()) {
                arsenalWrapper.update(delta);
                arsenalWrapper.shoot();
            }
        }
    }

    @Override
    public AI clone() {
        return new ShooterAI(damage);
    }
}

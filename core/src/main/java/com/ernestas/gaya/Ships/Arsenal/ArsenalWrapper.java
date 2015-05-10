package com.ernestas.gaya.Ships.Arsenal;

import com.ernestas.gaya.Util.SerializationRepair;

import java.io.Serializable;

public class ArsenalWrapper implements Serializable, SerializationRepair {
    private static final long serialVersionUID = 6435371864780261489L;

    private Arsenal arsenal;

    private float fireRate;
    private float timePassedSinceLastShoot;

    public ArsenalWrapper(Arsenal arsenal, float fireRate) {
        this.arsenal = arsenal;
        this.fireRate = fireRate;
        this.timePassedSinceLastShoot = fireRate;
    }

    public void shoot() {
        if (canShoot()) {
            timePassedSinceLastShoot = 0f;
            arsenal.shoot();
        }
    }

    public void update(float delta) {
        timePassedSinceLastShoot += delta;
    }

    public boolean canShoot() {
        return timePassedSinceLastShoot >= fireRate;
    }

    public Arsenal getArsenal() { return arsenal; }

    @Override
    public void repair() {
        arsenal.repair();
    }
}

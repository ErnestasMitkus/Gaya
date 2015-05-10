package com.ernestas.gaya.AI;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Util.SerializationRepair;

public interface AI extends SerializationRepair {

    public void init(Level level, EnemyShip ship);

    public void update(float delta);

    public AI clone();

}

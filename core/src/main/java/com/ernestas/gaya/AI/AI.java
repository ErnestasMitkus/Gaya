package com.ernestas.gaya.AI;

import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Ships.EnemyShip;

public interface AI {

    public void init(Level level, EnemyShip ship);

    public void update(float delta);

    public AI clone();

}

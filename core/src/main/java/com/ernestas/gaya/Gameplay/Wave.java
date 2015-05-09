package com.ernestas.gaya.Gameplay;

import com.ernestas.gaya.Powerups.Powerup;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Ships.Ship;
import com.ernestas.gaya.Util.Settings.Settings;

import java.util.LinkedList;
import java.util.List;

public class Wave {

    public static class EnemyWithOffset {
        public float offsetX;
        public float offsetY;
        public EnemyShip ship;

        public EnemyWithOffset(float offsetX, float offsetY, EnemyShip ship) {
            this.offsetX = offsetX * Settings.getInstance().getScale();
            this.offsetY = offsetY * Settings.getInstance().getScale();
            this.ship = ship;
        }

    }

    public static class Builder {
        private static final float OFFSET_Y = 32f;

        private Wave wave;

        public Builder() {
            wave = new Wave();
        }

        public Builder withEnemy(EnemyWithOffset enemy) {
            if (enemy != null) {
                wave.addEnemy(enemy);
            }
            return this;
        }

        public Builder withPowerup(Powerup powerup) {
            if (powerup != null) {
                wave.addPowerup(powerup);
            }
            return this;
        }

        public Builder withId(int id) {
            wave.id = id;
            return this;
        }

        public Wave build() {
            prepareForBuild();
            return wave;
        }

        private void prepareForBuild() {
            float scale = Settings.getInstance().getScale();
            // enemies
            for (int i = 0; i < wave.enemyList.size(); ++i) {
                EnemyWithOffset enemy = wave.enemyList.get(i);
                enemy.offsetY += Settings.getInstance().getHeight();
                //enemy.offsetX += Settings.getInstance().getWidth() / 2;

                enemy.ship.setPosition(enemy.offsetX, enemy.offsetY + OFFSET_Y * scale);
            }

            // powerups
            for (Powerup powerup : wave.powerupList) {
                powerup.setPosition(powerup.getPosition().x, powerup.getPosition().y + Settings.getInstance().getHeight() + OFFSET_Y * scale);
            }
        }

    }

    private List<EnemyWithOffset> enemyList = new LinkedList<EnemyWithOffset>();
    private List<Powerup> powerupList = new LinkedList<Powerup>();
    private int id;

    public static final Wave EMPTY_WAVE = new Wave.Builder()
                                                .withId(-1)
                                                .build();

    private Wave() {
    }

    private void addEnemy(EnemyWithOffset enemy) {
        if (!enemyList.contains(enemy)) {
            enemyList.add(enemy);
        }
    }

    private void addPowerup(Powerup powerup) {
        if (!powerupList.contains(powerup)) {
            powerupList.add(powerup);
        }
    }

    public void offsetBy(float y) {
        for (EnemyWithOffset enemy : enemyList) {
            enemy.ship.setPosition(enemy.ship.getPosition().x, enemy.ship.getPosition().y + y);
        }
        for (Powerup powerup : powerupList) {
            powerup.setPosition(powerup.getPosition().x, powerup.getPosition().y + y);
        }
    }

    public List<EnemyWithOffset> getEnemyList() { return enemyList; }
    public List<Powerup> getPowerupList() { return powerupList; }
    public int getId() { return id; }

    public boolean waveCompleted() {
        return enemyList.isEmpty();
    }

}

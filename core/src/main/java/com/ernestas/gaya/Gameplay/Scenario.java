package com.ernestas.gaya.Gameplay;

import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.EnemyShip;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Scenario {
    private List<Wave> waves = new LinkedList<Wave>();

    private String name = "";
    private String author = "";

    public Scenario() {

    }

    public void addWave(Wave wave) {
        if (!waves.contains(wave)) {
            waves.add(wave);
            sort();
        }
    }

    public Wave getNextWave() {
        Wave wave = Wave.EMPTY_WAVE;
        if (!waves.isEmpty()) {
            wave = waves.get(0);
            waves.remove(0);
        }
        return wave;
    }

    private void sort() {
        Collections.sort(waves, new Comparator<Wave>() {
            @Override
            public int compare(Wave wave1, Wave wave2) {
                return wave1.getId() - wave2.getId();
            }
        });
    }

    public boolean scenarioCompleted() {
        return waves.isEmpty();
    }

    public List<Wave> getWaves() { return waves; }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Scenario getTestScenario() {
        Scenario scenario = new Scenario();
        int middle = Settings.getInstance().getWidth() / 2;

        Wave wave = new Wave.Builder()
            .withId(0)
            .withEnemy(new Wave.EnemyWithOffset(
                middle, 0, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle - 64, 64, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle + 64, 64, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .build()
            ;


        Wave wave1 = new Wave.Builder()
            .withId(1)
            .withEnemy(new Wave.EnemyWithOffset(
                middle, 0, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle - 64, 64, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle + 64, 64, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle - 128, 96, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle, 96, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .withEnemy(new Wave.EnemyWithOffset(
                middle + 128, 96, new EnemyShip.Builder()
                .withSprite(GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.shipGreen))
                .withHealth(1)
                .withSpeed(60f)
                .build()))
            .build()
            ;


        scenario.addWave(wave);
        scenario.addWave(wave1);
        return scenario;
    }

}

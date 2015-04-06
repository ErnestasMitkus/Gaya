package com.ernestas.gaya.Util.Settings;

import com.ernestas.gaya.ResourceLoaders.ResourceLoader;

public class GameSettings {
    public static float DEFAULT_GAME_SPEED = 30f;

    /********************************************************/
    private static GameSettings instance = null;

    private GameSettings() {}

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public static void reset() { instance = null; }
    /********************************************************/

    private ResourceLoader resourceLoader;
    private float gameSpeed = DEFAULT_GAME_SPEED;

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public float getGameSpeed() { return gameSpeed; }

    public void setGameSpeed(float gameSpeed) { this.gameSpeed = gameSpeed; }


}

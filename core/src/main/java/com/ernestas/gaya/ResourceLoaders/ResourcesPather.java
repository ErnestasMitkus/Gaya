package com.ernestas.gaya.ResourceLoaders;

public class ResourcesPather {

    // Images
    public String splash;
    public String background;
    public String cloud;
    public String menuButton;

    // Player Ship
    public String shipPlayer;

    // Enemy Ship
    public String shipGreen;
    public String shipWhite;
    public String shipBlack;
    public String shipBlue;

    // Bullets
    public String simpleBullet;

    // Powerups
    public String powerupSS;

    // FX
    public String explosionSS;

    // Sounds

    private ResourcesPather() {
    }

    public static ResourcesPather defaultResourcesPather() {
        ResourcesPather resourcesPather = new ResourcesPather();
        String defaultPath = "Sprites/";

        return defaultResourcesPather(defaultPath);
    }

    public static ResourcesPather defaultResourcesPather(String defaultPath) {
        ResourcesPather resourcesPather = new ResourcesPather();

        resourcesPather.splash = defaultPath + "splash.png";
        resourcesPather.background = defaultPath + "desert-background-looped.png";
        resourcesPather.cloud = defaultPath + "clouds-transparent.png";
        resourcesPather.shipPlayer = defaultPath + "spaceship32.png";
        resourcesPather.shipGreen = defaultPath + "greenShip.png";
        resourcesPather.shipWhite = defaultPath + "whiteShip.png";
        resourcesPather.shipBlack = defaultPath + "blackShip.png";
        resourcesPather.shipBlue = defaultPath + "blueShip.png";
        resourcesPather.explosionSS = defaultPath + "explosionSS.png";
        resourcesPather.simpleBullet = defaultPath + "bullets/simpleBullet.png";
        resourcesPather.powerupSS = defaultPath + "powerupSS.png";
        resourcesPather.menuButton = defaultPath + "menuButton.png";

        return resourcesPather;
    }

}

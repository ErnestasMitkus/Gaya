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

    public String lightning;

    // Sounds

    private ResourcesPather() {
    }

    public static ResourcesPather defaultResourcesPather() {
        ResourcesPather resourcesPather = new ResourcesPather();
        String defaultPath = "";

        return defaultResourcesPather(defaultPath);
    }

    public static ResourcesPather defaultResourcesPather(String defaultPath) {
        ResourcesPather resourcesPather = new ResourcesPather();

        String spritePath = "Sprites/";
        String audioPath = "Audio/";
        String animationsPath = "Animations/";

        resourcesPather.splash = defaultPath + spritePath + "splash.png";
        resourcesPather.background = defaultPath + spritePath + "desert-background-looped.png";
        resourcesPather.cloud = defaultPath + spritePath + "clouds-transparent.png";
        resourcesPather.shipPlayer = defaultPath + spritePath + "spaceship32.png";
        resourcesPather.shipGreen = defaultPath + spritePath + "greenShip.png";
        resourcesPather.shipWhite = defaultPath + spritePath + "whiteShip.png";
        resourcesPather.shipBlack = defaultPath + spritePath + "blackShip.png";
        resourcesPather.shipBlue = defaultPath + spritePath + "blueShip.png";
        resourcesPather.simpleBullet = defaultPath + spritePath + "bullets/simpleBullet.png";
        resourcesPather.powerupSS = defaultPath + spritePath + "powerupSS.png";
        resourcesPather.menuButton = defaultPath + spritePath + "menuButton.png";
        resourcesPather.explosionSS = defaultPath + spritePath + "explosionSS.png";

        resourcesPather.lightning = defaultPath + animationsPath + "lightning.gif";

        return resourcesPather;
    }

}

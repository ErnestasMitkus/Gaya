package com.ernestas.gaya.ResourceLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Util.Settings.Settings;

import java.io.File;
import java.util.HashMap;

public class ResourceLoader {

    public enum ResourceId {
        // Splash
        splash,

        // Background
        background, cloud,

        // Ships
        shipPlayer, shipGreen,

        // Bullets
        simpleBullet,

        // Effects
        explosionSS,

        // Empty sprite
        emptySprite
    }

    ResourcesPather pather;
    HashMap<ResourceId, Sprite> spriteMap = new HashMap<ResourceId, Sprite>();
    boolean loaded = false;

    public ResourceLoader(ResourcesPather pather) {
        if (pather != null) {
            this.pather = pather;
        } else {
            this.pather = ResourcesPather.defaultResourcesPather();
        }
    }

    public void load() {
        spriteMap.clear();
        try {
            loadResource(ResourceId.splash, pather.splash);
            loadResource(ResourceId.background, pather.background);
            loadResource(ResourceId.cloud, pather.cloud);
            loadResource(ResourceId.shipPlayer, pather.shipPlayer);
            loadResource(ResourceId.shipGreen, pather.shipGreen);
            loadResource(ResourceId.explosionSS, pather.explosionSS, false);
            loadResource(ResourceId.simpleBullet, pather.simpleBullet);

            loaded = true;
        } catch(Exception e) {
            e.printStackTrace(); // Handle it somehow different?
//            throw e;
        }
    }

    private void loadResource(ResourceId id, String path) {
        loadResource(id, path, true);
    }

    private void loadResource(ResourceId id, String path, boolean scale) {
        Texture texture = new Texture(path);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Sprite sprite = new Sprite(texture);
        if (scale) {
            sprite = SpriteScaler.scaleSprite(sprite, Settings.getInstance().getScale());
        }
        spriteMap.put(id, sprite);
    }

    public Sprite getResource(ResourceId id) {
        if (id == ResourceId.emptySprite) {
            return new Sprite(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
        }

        if (loaded == false) {
            return null;
        }

        Sprite sprite = spriteMap.get(id);
        return new Sprite(sprite);
    }

    public static ResourceId resourceIdFromName(String resourceName) {

        if (resourceName.equalsIgnoreCase("greenShip")) {
            return ResourceId.shipGreen;
        }

        return null;
    }

}

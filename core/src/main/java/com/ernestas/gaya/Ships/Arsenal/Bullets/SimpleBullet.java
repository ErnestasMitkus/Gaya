package com.ernestas.gaya.Ships.Arsenal.Bullets;

import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Ships.Ship;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class SimpleBullet extends Bullet {

    public SimpleBullet(Ship author, Vector2f position, Vector2f flyVector) {
        super(author,
            GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.simpleBullet),
            250f,
            flyVector,
            position,
            1);
    }

    public SimpleBullet(Ship author, Vector2f position) {
        this(author, position, new Vector2f(0, 1));
    }

    @Override
    public Rectangle getBounds() {
        int emptyWidth = 24;
        int emptyHeight = 16;

        Rectangle bounds = sprite.getBoundingRectangle();

        bounds.x += emptyWidth / 2;
        bounds.width -= emptyWidth;

        bounds.y += emptyHeight / 2;
        bounds.height -= emptyHeight;

        return bounds;
    }



}

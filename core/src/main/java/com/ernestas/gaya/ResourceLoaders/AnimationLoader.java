package com.ernestas.gaya.ResourceLoaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Util.GIFDecoder;
import com.ernestas.gaya.Util.Settings.Settings;

import java.io.InputStream;
import java.util.HashMap;

public class AnimationLoader {

    public enum ResourceId {
        lightning
    }

    ResourcesPather pather;
    HashMap<ResourceId, Animation> animationMap = new HashMap<ResourceId, Animation>();

    public AnimationLoader(ResourcesPather pather) {
        if (pather != null) {
            this.pather = pather;
        } else {
            this.pather = ResourcesPather.defaultResourcesPather();
        }
    }

    public void load() {
        animationMap.clear();
        try {
            loadAnimation(ResourceId.lightning, pather.lightning);
        } catch(Exception e) {
            e.printStackTrace(); // Handle it somehow different?
//            throw e;
        }
    }

    private void loadAnimation(ResourceId id, String path) {
        loadAnimation(id, path, true);
    }

    private void loadAnimation(ResourceId id, String path, boolean scale) {
        InputStream animationStream = Gdx.files.internal(path).read();

        Animation animation = GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, animationStream);
        animationMap.put(id, animation);
    }

    public Animation getResource(ResourceId id) {
        Animation animation = animationMap.get(id);
        if (animation == null) {
            return null;
        }
        return new Animation(animation.getFrameDuration(), animation.getKeyFrames());
    }

}

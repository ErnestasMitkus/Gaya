package com.ernestas.gaya.Overlay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;

public class ScreenDimmer {

    private Sprite dimmerSprite;
    private float startAlpha;
    private float targetAlpha;
    private float duration;
    private float elapsed = 0;

    public ScreenDimmer(float startAlpha, float targetAlpha, float duration) {
        this.startAlpha = startAlpha;
        this.targetAlpha = targetAlpha;
        this.duration = duration;

        dimmerSprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.blackSprite);
        dimmerSprite.setScale(Settings.getInstance().getWidth(), Settings.getInstance().getHeight());
        dimmerSprite.setAlpha(startAlpha);
    }

    public void update(float delta) {
        if (done()) {
            return;
        }
        float alphaDiff = targetAlpha - startAlpha;
        elapsed += delta;

        float alpha = (elapsed / duration * alphaDiff) + startAlpha;

        alpha = Math.min(alpha, 1f);
        alpha = Math.max(alpha, 0f);

        dimmerSprite.setAlpha(alpha);
    }

    public Sprite getSprite() { return dimmerSprite; }

    public boolean done() {
        return duration - elapsed < 0;
    }

}

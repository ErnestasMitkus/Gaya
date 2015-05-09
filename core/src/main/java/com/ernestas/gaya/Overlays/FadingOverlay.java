package com.ernestas.gaya.Overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.Overlays.Items.OverlayItem;

import java.util.List;
import java.util.ArrayList;

public class FadingOverlay implements Overlay {

    private InputProcessor input;
    private List<OverlayItem> items = new ArrayList<OverlayItem>();

    private float fadeIn = 0f;
    private float leftToFadeIn = 0f;
    private boolean fadeInDone = true;
    private float delay = 0f;

    private float fadeOut = 0f;
    private float leftToFadeOut = 0f;
    private boolean doFadeOut = false;

    public FadingOverlay(InputProcessor input) {
        this(input, new ArrayList<OverlayItem>());
    }

    public FadingOverlay(InputProcessor input, List<OverlayItem> overlayItems) {
        this.input = input;
        this.items.addAll(overlayItems);
    }

    public void addItem(OverlayItem item) {
        items.add(item);
    }

    @Override
    public void update(float delta) {
        if (delay <= 0) {
            leftToFadeIn -= delta;
            if (leftToFadeIn <= 0) {
                fadeInDone = true;
            }
            if (doFadeOut && leftToFadeOut > 0) {
                leftToFadeOut -= delta;
            }

            if (!doFadeOut) {
                for(OverlayItem item : items) {
                    item.update(input, delta);
                }
            }

        } else {
            delay -= delta;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (delay > 0) {
            return;
        }
        float alpha = 1f;
        if (!fadeInDone) {
            alpha = (1 - leftToFadeIn / fadeIn);
        }
        if (doFadeOut) {
            alpha = (leftToFadeOut / fadeOut);
            alpha = Math.max(alpha, 0f);
        }
        for(OverlayItem item : items) {
            item.render(batch, alpha);
        }
    }

    @Override
    public void renderBounds(ShapeRenderer renderer) {
        for(OverlayItem item : items) {
            Rectangle rec = item.getBounds();
            renderer.rect(rec.x, rec.y, rec.width, rec.height);
        }
    }

    public void setFadeIn(float duration) {
        fadeIn = duration;
        reset();
    }

    public void reset() {
        leftToFadeIn = fadeIn;
        fadeInDone = false;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setFadeOut(float duration) {
        fadeOut = duration;
        leftToFadeOut = fadeOut;
    }

    public void fadeOut() {
        doFadeOut = true;
    }

    public boolean doneFadingOut() {
        return leftToFadeOut <= 0;
    }
}

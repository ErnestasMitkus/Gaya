package com.ernestas.gaya.Screens.Overlays;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Game.Level;
import com.ernestas.gaya.Screens.Overlays.Items.OverlayItem;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.ArrayList;

public class EndGameOverlay implements Overlay {

    private List<OverlayItem> items = new ArrayList<OverlayItem>();
    private Level level;

    private float fadeIn = 0f;
    private float leftToFadeIn = 0f;
    private boolean fadeInDone = true;

    public EndGameOverlay(Level level) {
        this(level, new ArrayList<OverlayItem>());
    }

    public EndGameOverlay(Level level, List<OverlayItem> overlayItems) {
        this.level = level;
        this.items.addAll(overlayItems);
    }

    public void addItem(OverlayItem item) {
        items.add(item);
    }

    @Override
    public void update(float delta) {
        leftToFadeIn -= delta;
        if (leftToFadeIn <= 0) {
            fadeInDone = true;
        }
        for(OverlayItem item : items) {
            item.update(level.getInput(), delta);
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        for(OverlayItem item : items) {
            Sprite sprite = new Sprite(item.getSprite());
            if (!fadeInDone) {
                sprite.setAlpha(1 - leftToFadeIn / fadeIn);
            }
            sprite.draw(batch);
        }
    }

    @Override
    public void renderBounds(ShapeRenderer renderer) {
        for(OverlayItem item : items) {
            Rectangle rec = item.getSprite().getBoundingRectangle();
            renderer.rect(rec.x, rec.y, rec.width, rec.height);
        }
    }

    public void setFadeIn(float duration) {
        fadeIn = duration;
        leftToFadeIn = duration;
        fadeInDone = false;
    }

    public void reset() {
        fadeInDone = false;
    }

}

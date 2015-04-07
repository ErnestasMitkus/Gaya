package com.ernestas.gaya.Screens.Overlays.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Screens.Overlays.Callbacks.OverlayCallback;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class RectangleItem implements OverlayItem {

    private Sprite sprite;
    private OverlayCallback callback;
    private int id;

    private boolean lockOn = false;
    private int mouseDownDuration = 0;

    public RectangleItem(int id, Vector2f position, OverlayCallback callback) {
        this.id = id;
        this.callback = callback;
        sprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.cloud);
        sprite.setPosition(position.x, position.y);
    }

    @Override
    public void update(InputProcessor input, float delta) {
        if (input.mouseDown()) {
            mouseDownDuration++;
        } else {
            mouseDownDuration = 0;
        }

        if ((mouseDownDuration == 1) && !lockOn) {
            if (getBounds().contains(input.getMousePos().x, input.getMousePos().y)) {
                lockOn = true;
            }
        }

        if (!input.mouseDown()) {
            if (lockOn) {
                lockOn = false;
                // Do click
                callback.callback(id);
            }
        }

        if (lockOn) {
            // check if mouse is still in rectangle
            if (!getBounds().contains(input.getMousePos().x, input.getMousePos().y)) {
                lockOn = false;
            }
            System.out.println(getBounds() + "       " + input.getMousePos());
        }

    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}

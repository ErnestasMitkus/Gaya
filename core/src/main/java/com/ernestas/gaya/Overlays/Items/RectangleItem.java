package com.ernestas.gaya.Overlays.Items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Overlays.Callbacks.OverlayCallback;
import com.ernestas.gaya.Util.Fonts.FontFactory;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class RectangleItem implements OverlayItem {

    public static final int ALIGN_MIDDLE_HORIZONTAL = 0x0001;
    public static final int ALIGN_MIDDLE_VERTICAL = 0x0002;

    private Sprite buttonBg;
    private OverlayCallback callback;

    private boolean lockOn = false;
    private int mouseDownDuration = 0;

    private BitmapFont font;
    private String text;

    private boolean showBg;
    private Color textColor = Color.WHITE;

    public RectangleItem(Vector2f offset, OverlayCallback callback) {
        this(offset, callback, 0);
    }

    public RectangleItem(Vector2f offset, OverlayCallback callback, int options) {
        this(offset, callback, options, true);
    }

    public RectangleItem(Vector2f offset, OverlayCallback callback, int options, boolean showBg) {
        this.callback = callback;
        this.showBg = showBg;
        buttonBg = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.menuButton);
        parseOptions(options);
        Vector2f scaledOffset = offset.scale(Settings.getInstance().getScale());
        buttonBg.translate(scaledOffset.x, scaledOffset.y);
        withFont(FontFactory.getInstance().getFont(FontFactory.FontId.Calibri));
    }

    public RectangleItem withText(String text) {
        this.text = text;
        return this;
    }

    public RectangleItem withFont(BitmapFont font) {
        this.font = font;
        return this;
    }

    private void parseOptions(int options) {
        if (equalBit(options, ALIGN_MIDDLE_HORIZONTAL)) {
            float spriteWidth = buttonBg.getBoundingRectangle().width;
            int screenWidth = Settings.getInstance().getWidth();
            buttonBg.setX((screenWidth - spriteWidth) / 2);
        }
        if (equalBit(options, ALIGN_MIDDLE_VERTICAL)) {
            float spriteHeight = buttonBg.getBoundingRectangle().height;
            int screenHeight = Settings.getInstance().getHeight();
            buttonBg.setY((screenHeight - spriteHeight) / 2);
        }
    }

    private boolean equalBit(int a, int b) {
        return ((a & b) == b);
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
                callback.callback();
            }
        }

        if (lockOn) {
            // check if mouse is still in rectangle
            if (!getBounds().contains(input.getMousePos().x, input.getMousePos().y)) {
                lockOn = false;
            }
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        render(batch, 1f);
    }

    @Override
    public void render(SpriteBatch batch, float alpha) {
        // Draw rectangle
        if (showBg) {
            buttonBg.setAlpha(alpha);
            buttonBg.draw(batch);
        }

        // Draw text
        if (text != null && !text.isEmpty()) {
            BitmapFont.TextBounds bounds = font.getBounds(text);
            Rectangle rec = getBounds();
            float x = rec.x + ((rec.width - bounds.width) / 2);
            float y = rec.y + ((rec.height + bounds.height) / 2);

            textColor.a = alpha;
            font.setColor(textColor);
            font.draw(batch, text, x, y);
        }
    }

    public void setColor(Color color) { this.textColor = color; }

    public Rectangle getBounds() {
        return buttonBg.getBoundingRectangle();
    }
}

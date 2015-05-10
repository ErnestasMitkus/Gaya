package com.ernestas.gaya.Overlay;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.Util.Fonts.FontFactory;
import com.ernestas.gaya.Util.SerializationRepair;
import com.ernestas.gaya.Util.Settings.Settings;

import java.io.Serializable;

public class HUD implements Serializable, SerializationRepair {
    private static final long serialVersionUID = 6435371812350261489L;

    private Level level;
    private transient BitmapFont font;

    public HUD(Level level) {
        this.level = level;

        // Move to font factory
        font = FontFactory.getInstance().getFont(FontFactory.FontId.Calibri);
    }

    public void render(SpriteBatch batch) {
        // score
        String scoreString = "score: " + level.getPlayer().getScore();
        BitmapFont.TextBounds scoreBounds = font.getBounds(scoreString);
        font.draw(batch, scoreString, Settings.getInstance().getWidth() - scoreBounds.width - 15,
                                         Settings.getInstance().getHeight() - 10);

        // player health
        int healthWidth = 100;
        int healthHeight = 20;
        Pixmap pixmap = new Pixmap(healthWidth, healthHeight, Pixmap.Format.RGBA8888);
        float percentage = (1f * level.getPlayer().getHealth()) / level.getPlayer().getMaxHealth();
        int healthX = (int) (healthWidth * percentage);

        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, healthX, healthHeight);

        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(healthX, 0, healthWidth - healthX, healthHeight);

        Sprite healthSprite = new Sprite(new Texture(pixmap));
        healthSprite.setPosition(10, 10);
        healthSprite.draw(batch);

        // player amunition ?
    }

    @Override
    public void repair() {
        font = FontFactory.getInstance().getFont(FontFactory.FontId.Calibri);
    }
}

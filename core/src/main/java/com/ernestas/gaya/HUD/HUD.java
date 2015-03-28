package com.ernestas.gaya.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Game.Level;
import com.ernestas.gaya.Util.Settings.Settings;

import java.awt.*;

public class HUD {

    Level level;
    BitmapFont font;

    public HUD(Level level) {
        this.level = level;

        // Move to font factory
        font = new BitmapFont(Gdx.files.internal("Fonts/calibri.fnt"), Gdx.files.internal("Fonts/calibri.png"), false);
    }

    public void render(SpriteBatch batch) {
        // score
        String scoreString = "score: " + level.getPlayer().getScore();
        // draw font
        BitmapFont.TextBounds scoreBounds = font.getBounds(scoreString);
        font.draw(batch, scoreString, Settings.getInstance().getWidth() - scoreBounds.width - 15,
                                         Settings.getInstance().getHeight() - 10);

        // player health

        // player amunition ?

        // wave progress
    }

}

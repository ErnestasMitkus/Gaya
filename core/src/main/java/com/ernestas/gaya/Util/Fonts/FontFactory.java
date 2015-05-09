package com.ernestas.gaya.Util.Fonts;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;
import java.util.Map;

public class FontFactory {

    public enum FontId {
        Calibri
    }

    /********************************************************/
    private static FontFactory instance = null;

    private FontFactory() {}

    public static FontFactory getInstance() {
        if (instance == null) {
            instance = new FontFactory();
            instance.loadFontPaths();
        }
        return instance;
    }

    public static void reset() { instance = null; }
    /********************************************************/

    private Map<FontId, String> fontMap = new HashMap<FontId, String>();


    public BitmapFont getFont(FontId id) {
        return fontFromPath(fontMap.get(id));
    }

    private void loadFontPaths() {
        fontMap.clear();

        fontMap.put(FontId.Calibri, "Fonts/calibri");
    }

    private BitmapFont fontFromPath(String path) {
        return new BitmapFont(Gdx.files.internal(path + ".fnt"), Gdx.files.internal(path + ".png"), false);
    }

}

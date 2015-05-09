package com.ernestas.gaya.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Util.Settings.Settings;

public class DesktopLauncher {
    private static final float SCALE = 1.4f;
    private static final String TITLE = "Gaya";
    private static final String VERSION = "v0.8";

    private static final int FPS = 60;

	public static void main (String[] arg) {
        try {
            launchGame(arg);
        } catch(Exception e) {
            e.printStackTrace();
        } catch(Error e) {
            e.printStackTrace();
        }
	}

    public static void launchGame(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        configureSettings();
        configureConfig(config);

        new LwjglApplication(new GayaEntry(GayaEntry.DESKTOP, arg), config);
    }

    private static void configureSettings() {
        Settings.getInstance().setScale(SCALE);
        Settings.getInstance().setWidth((int) (Settings.DEFAULT_WIDTH * SCALE));
        Settings.getInstance().setHeight((int) (Settings.DEFAULT_HEIGHT * SCALE));
        Settings.getInstance().setFrameRate(FPS);
    }

    private static void configureConfig(LwjglApplicationConfiguration config) {
        config.title = TITLE + " " + VERSION;
        config.width = Settings.getInstance().getWidth();
        config.height = Settings.getInstance().getHeight();

        config.foregroundFPS = FPS;
        config.resizable = false;

//        config.useGL30 = true; // Use GL30 for performance improvement
    }
}

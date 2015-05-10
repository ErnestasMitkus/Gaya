package com.ernestas.gaya.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Screens.PlayScreen;
import com.ernestas.gaya.Ships.PlayerShip;
import com.ernestas.gaya.Util.Settings.Settings;

import java.io.IOException;

public class DesktopLauncher {
    private static final float SCALE = 1.4f;
    private static final String TITLE = "Gaya";
    private static final String VERSION = "v0.8";

    private static final int FPS = 60;

	public static void main (String[] arg) {
        try {
            final GayaEntry gaya = new GayaEntry(GayaEntry.DESKTOP, arg);
            launchGame(gaya, arg);

            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        try {
                            System.in.read(buffer);

                            String msg = new String(buffer);
                            msg = msg.substring(0, msg.indexOf('\n'));

                            if (msg.equalsIgnoreCase("hello")) {
                                System.out.println("Hello to you too.");
                            } else if (msg.equalsIgnoreCase("player")) {
                                if (gaya.getScreen() instanceof PlayScreen) {
                                    PlayScreen screen = (PlayScreen) gaya.getScreen();
                                    PlayerShip player = screen.getLevel().getPlayer();

                                    System.out.println("Player info:");
                                    System.out.println("position: " + player.getPosition());
                                    System.out.println("health: " + player.getHealth());
                                    System.out.println("score: " + player.getScore());
                                    System.out.println("----------");
                                } else {
                                    System.out.println("Player is not playing... :(");
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch(Exception e) {
            e.printStackTrace();
        } catch(Error e) {
            e.printStackTrace();
        }
	}

    public static void launchGame(GayaEntry gaya, String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        configureSettings();
        configureConfig(config);

        new LwjglApplication(gaya != null ? gaya : new GayaEntry(GayaEntry.DESKTOP, arg), config);
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

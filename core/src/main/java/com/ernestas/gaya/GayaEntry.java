package com.ernestas.gaya;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.ResourceLoaders.GlobalLoader;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.ResourceLoaders.ResourcesPather;
import com.ernestas.gaya.Screens.ErrorScreen;
import com.ernestas.gaya.Screens.MenuScreen;
import com.ernestas.gaya.Screens.PlayScreen;

import com.ernestas.gaya.Screens.SplashScreen;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;


import static com.ernestas.gaya.Util.Settings.Settings.*;

public class GayaEntry extends Game {

    public static final int DESKTOP = 0;
    public static final int ANDROID = 1;

    public final int DEVICE;

    private InputProcessor input;
    private String startScreen;

    public GayaEntry(int device) {
        this(device, null);
    }

    public GayaEntry(int device, String[] args)  {
        this.DEVICE = device;
        if (args != null) {
            parseArguments(args);
        }
    }

    public String scenarioPath;
    private void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equalsIgnoreCase("-s")) {
                scenarioPath = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-start")) {
                startScreen = args[i + 1];
            }

        }
    }

	@Override
	public void create () {
        try {
            input = new InputProcessor(DEVICE == ANDROID);
            Gdx.input.setInputProcessor(input);
            if (DEVICE == ANDROID) {
                float scale = Gdx.graphics.getHeight() / DEFAULT_HEIGHT;
                if (Gdx.graphics.getWidth() / DEFAULT_WIDTH < scale) {
                    scale = Gdx.graphics.getWidth() / DEFAULT_WIDTH;
                }

                Settings.getInstance().setScale(scale);
                Settings.getInstance().setWidth((int) (Settings.DEFAULT_WIDTH * scale));
                Settings.getInstance().setHeight((int) (Settings.DEFAULT_HEIGHT * scale));
                Settings.getInstance().setAutoShoot(true);
            } else if (DEVICE == DESKTOP) {

            }


            // Global loaders
            // Move to after splash?
            GlobalLoader.load();

            // Move to level?
            GameSettings.getInstance().setResourceLoader(new ResourceLoader(ResourcesPather.defaultResourcesPather()));
            GameSettings.getInstance().getResourceLoader().load();

            if (startScreen != null) {
                if (startScreen.equalsIgnoreCase("PLAY")) {
                    setScreen(new PlayScreen(this));
                } else if (startScreen.equalsIgnoreCase("MENU")) {
                    setScreen(new MenuScreen(this));
                } else {
                    setScreen(new SplashScreen(this));
                }
            } else {

//            setScreen(new ErrorScreen());
            setScreen(new SplashScreen(this));
//            setScreen(new MenuScreen(this));
//            setScreen(new PlayScreen(this));
            }
        } catch(Exception e) {
            onException(e);
        }
	}

    @Override
    public void render () {
        try {
            input.update();
            if (screen != null) {
                screen.render(Gdx.graphics.getDeltaTime());
            }
        } catch(Exception e) {
            if (screen instanceof ErrorScreen) {
                e.printStackTrace();
                Gdx.app.exit();
            } else {
                onException(e);
            }
        }
    }

    public void onException(Exception e) {
        setScreen(new ErrorScreen(e));
    }
    public int getDevice() { return DEVICE; }
    public InputProcessor getInputProcessor() {
        return input;
    }
}

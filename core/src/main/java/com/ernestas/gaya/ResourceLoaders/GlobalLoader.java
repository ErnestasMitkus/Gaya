package com.ernestas.gaya.ResourceLoaders;

import com.ernestas.gaya.Util.Fonts.FontFactory;
import com.ernestas.gaya.Util.Settings.GameSettings;

public class GlobalLoader {

    private static String loadingMessage = "";
    private static boolean loaded = false;

    private GlobalLoader() {}

    public static void loadInitial() {
        FontFactory.getInstance();

        GameSettings.getInstance().setResourceLoader(new ResourceLoader(ResourcesPather.defaultResourcesPather()));
        GameSettings.getInstance().getResourceLoader().loadInitial();
    }

    public static void load() {
        // Load global resources

        loadingMessage = "Loading images.";
        GameSettings.getInstance().setResourceLoader(new ResourceLoader(ResourcesPather.defaultResourcesPather()));
        GameSettings.getInstance().getResourceLoader().load();

        loadingMessage = "Loading animations.";
        GameSettings.getInstance().setAnimationLoader(new AnimationLoader(ResourcesPather.defaultResourcesPather()));
        GameSettings.getInstance().getAnimationLoader().load();

        loadingMessage = "Loading sounds.";

        loadingMessage = "Loading done.";
        loaded = true;
    }

    public static synchronized String getLoadingMessage() { return loadingMessage; }
    public static synchronized boolean isLoaded() { return loaded; }

}

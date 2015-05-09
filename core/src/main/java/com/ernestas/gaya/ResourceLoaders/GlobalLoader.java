package com.ernestas.gaya.ResourceLoaders;

import com.ernestas.gaya.Util.Fonts.FontFactory;

public class GlobalLoader {

    private GlobalLoader() {}

    public static void load() {
        FontFactory.getInstance();
        // Load global resources
    }

}

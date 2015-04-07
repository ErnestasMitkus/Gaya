package com.ernestas.gaya.Screens.Overlays.Callbacks;

import com.ernestas.gaya.Game.Level;

public class LevelCallback implements OverlayCallback {

    public static int RESTART_LEVEL = 0;
    public static int EXIT_TO_MENU = 1;

    private Level level;

    public LevelCallback(Level level) {
        this.level = level;
    }

    @Override
    public void callback() {
    }

    @Override
    public void callback(int id) {
        if (id == RESTART_LEVEL) {
            level.restartLevel();
        } else if (id == EXIT_TO_MENU) {
            // TODO: Add exit to menu
        }
    }

    @Override
    public void callback(Object[] callbackItems) {
    }
}

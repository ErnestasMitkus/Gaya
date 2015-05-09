package com.ernestas.gaya.Overlays.Callbacks;

import com.ernestas.gaya.Gameplay.Level;

public class LevelCallback implements OverlayCallback {

    public static final int RESUME_LEVEL = 0;
    public static final int RESTART_LEVEL = 1;
    public static final int VIEW_OPTIONS = 2;
    public static final int EXIT_TO_MENU = 3;

    private Level level;
    private int id;

    public LevelCallback(Level level, int id) {
        this.level = level;
        this.id = id;
    }

    @Override
    public void callback() {
        switch(id) {
            case RESTART_LEVEL:
                level.restartLevel();
                break;
            case VIEW_OPTIONS:
                // TODO: Add Options
                break;
            case EXIT_TO_MENU:
                level.exitToMenu();
                break;
            case RESUME_LEVEL:
            default:
                level.unpause();
                break;
        }
    }

}

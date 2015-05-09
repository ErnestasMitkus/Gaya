package com.ernestas.gaya.Overlays.Callbacks;

import com.ernestas.gaya.Screens.MenuScreen;

public class MenuCallback implements OverlayCallback {

    public static final int PLAY = 0;
    public static final int OPTIONS = 1;
    public static final int ABOUT = 2;
    public static final int EXIT = 3;

    private MenuScreen menu;
    private int id;

    public MenuCallback(MenuScreen menu, int id) {
        this.menu = menu;
        this.id = id;
    }

    @Override
    public void callback() {
        switch(id) {
            case PLAY:
                menu.play();
                break;
            case OPTIONS:
                break;
            case ABOUT:
                break;
            case EXIT:
                menu.exitGame();
                break;
            default:
                break;
        }
    }
}

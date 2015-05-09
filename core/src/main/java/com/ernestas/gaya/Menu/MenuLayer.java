package com.ernestas.gaya.Menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.Screens.MenuScreen;

public abstract class MenuLayer {

    protected MenuScreen screen;

    protected InputProcessor input;
    protected boolean doneHiding;

    public MenuLayer(InputProcessor input, MenuScreen screen) {
        this.input = input;
        this.screen = screen;
    }

    public void show() {
        doneHiding = false;
    }

    public void hide() {
        doneHiding = true;
    }

    public boolean doneHiding() {
        return doneHiding;
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

}

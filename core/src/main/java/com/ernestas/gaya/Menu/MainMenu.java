package com.ernestas.gaya.Menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Input.InputProcessor;
import com.ernestas.gaya.Screens.MenuScreen;
import com.ernestas.gaya.Overlays.Callbacks.MenuCallback;
import com.ernestas.gaya.Overlays.FadingOverlay;
import com.ernestas.gaya.Overlays.Items.RectangleItem;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class MainMenu extends MenuLayer {

    private FadingOverlay menuOverlay;

    public MainMenu(InputProcessor input, MenuScreen screen) {
        super(input, screen);
        init();
    }

    private void init() {
        menuOverlay = new FadingOverlay(input);
        menuOverlay.addItem(new RectangleItem(new Vector2f(0, 90), new MenuCallback(screen, MenuCallback.PLAY),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Play"));
        menuOverlay.addItem(new RectangleItem(new Vector2f(0, 30), new MenuCallback(screen, MenuCallback.OPTIONS),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Options"));
        menuOverlay.addItem(new RectangleItem(new Vector2f(0, -30), new MenuCallback(screen, MenuCallback.ABOUT),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("About"));
        menuOverlay.addItem(new RectangleItem(new Vector2f(0, -90), new MenuCallback(screen, MenuCallback.EXIT),
            RectangleItem.ALIGN_MIDDLE_HORIZONTAL | RectangleItem.ALIGN_MIDDLE_VERTICAL)
            .withText("Exit"));
        menuOverlay.setFadeIn(1f);
    }

    @Override
    public void show() {
        show(0f);
    }

    public void show(float delay) {
        menuOverlay.reset();
        menuOverlay.setDelay(delay);
    }

    @Override
    public void hide() {
        menuOverlay.setFadeOut(0.5f);
        menuOverlay.fadeOut();
        doneHiding = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        menuOverlay.render(batch);
    }

    @Override
    public void update(float delta) {
        menuOverlay.update(delta);
        if (menuOverlay.doneFadingOut()) {
            doneHiding = true;
        }
    }
}

package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.ResourceLoaders.ResourceLoader;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;

public class SplashScreen implements Screen {

    private GayaEntry gaya;

    private Sprite sprite;
    private SpriteBatch batch;

    private static final float DURATION = 1f;
    private static final float HOLD = 1.5f;

    private float holdingDuration = 0;

    private float alpha = 0;
    private boolean up = true;
    private boolean bounce = true;

    public SplashScreen(GayaEntry gaya) {
        this.gaya = gaya;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        sprite = GameSettings.getInstance().getResourceLoader().getResource(ResourceLoader.ResourceId.splash);
        sprite.setPosition(Settings.getInstance().getWidth() / 2 - sprite.getBoundingRectangle().width / 2,
                            Settings.getInstance().getHeight() / 2 - sprite.getBoundingRectangle().height / 2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void update(float delta) {
        if ((alpha == 0 && !up) || (alpha == 1 && !bounce)) {
            gaya.setScreen(new LoadingScreen(gaya));
            return;
        }

        if (alpha == 1) {
            holdingDuration += delta;
            if (holdingDuration >= HOLD) {
                up = !up;
            }
        }

        alpha += delta * DURATION * (up ? 1 : -1);


        if (alpha > 1) {
            alpha = 1;
        } else if (alpha < 0) {
            alpha = 0;
        }

        sprite.setColor(1, 1, 1, alpha);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

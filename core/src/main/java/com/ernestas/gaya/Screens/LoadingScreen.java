package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.ResourceLoaders.GlobalLoader;
import com.ernestas.gaya.Util.Fonts.FontFactory;
import com.ernestas.gaya.Util.Settings.Settings;

public class LoadingScreen implements Screen {

    private GayaEntry game;

    String loadingTitle = "Loading...";
    String progress = "Loading resources";
    BitmapFont font;

    private SpriteBatch batch;

    public LoadingScreen(GayaEntry game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = FontFactory.getInstance().getFont(FontFactory.FontId.Calibri);
        font.setColor(Color.WHITE);
    }

    private int renderCount = 0;

    @Override
    public void render(float delta) {
        if (GlobalLoader.isLoaded()) {
            game.setScreen(new MenuScreen(game));
        } else {
            if (renderCount > 60) {
                GlobalLoader.load(); // blocking function.
            }
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();

            BitmapFont.TextBounds titleBounds = font.getBounds(loadingTitle);
            font.draw(batch, loadingTitle, (Settings.getInstance().getWidth() - titleBounds.width) / 2,
                (Settings.getInstance().getHeight() + titleBounds.height) / 2);

            batch.end();
        }
        renderCount++;
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

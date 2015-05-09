package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Input.InputProcessor;

public class PlayScreen implements Screen {

    private GayaEntry gaya;
    private InputProcessor input;

    private SpriteBatch batch;

    private Level level;

    public PlayScreen(GayaEntry gaya) {
        this.gaya = gaya;

        batch = new SpriteBatch();
        input = gaya.getInputProcessor();
        level = new Level(gaya, input);
    }

    @Override
    public void show() {
        level.setup();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.update(delta);

        batch.begin();
        level.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        level.pause();
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

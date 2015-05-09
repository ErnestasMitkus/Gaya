package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ernestas.gaya.Util.Fonts.FontFactory;
import com.ernestas.gaya.Util.Settings.Settings;

import static com.ernestas.gaya.Util.Settings.Settings.DEFAULT_HEIGHT;
import static com.ernestas.gaya.Util.Settings.Settings.DEFAULT_WIDTH;

public class ErrorScreen implements Screen {

    Sprite sprite;
    SpriteBatch batch;

    ShapeRenderer renderer;

    int width, height;

    BitmapFont font;
    Exception e;

    public ErrorScreen() {
        this(null);
    }

    public ErrorScreen(Exception e) {
        this.e = e;
    }

    @Override
    public void show() {
        Texture texture = new Texture(Gdx.files.internal("Sprites/splash.png"));
        sprite = new Sprite(texture);
        sprite.setOrigin(0, 0);
        sprite.setPosition(0, 0);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        font = FontFactory.getInstance().getFont(FontFactory.FontId.Calibri);

        float scale = Gdx.graphics.getHeight() / DEFAULT_HEIGHT;
        if (Gdx.graphics.getWidth() / DEFAULT_WIDTH < scale) {
            scale = Gdx.graphics.getWidth() / DEFAULT_WIDTH;
        }

        Settings.getInstance().setScale(scale);
        Settings.getInstance().setWidth((int) (Settings.DEFAULT_WIDTH * scale));
        Settings.getInstance().setHeight((int) (Settings.DEFAULT_HEIGHT * scale));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0, 0, width - 1, height - 1);
        renderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        if (e != null) {
            font.drawWrapped(batch, e.getMessage(), 10, 10, 300);
        } else {
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();
            float accelZ = Gdx.input.getAccelerometerZ();

            font.draw(batch, "accelX --> " + accelX, 10, 200);
            font.draw(batch, "accelY --> " + accelY, 10, 170);
            font.draw(batch, "accelZ --> " + accelZ, 10, 140);
        }
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
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

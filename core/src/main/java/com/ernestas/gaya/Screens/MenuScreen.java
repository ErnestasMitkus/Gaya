package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Input.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ernestas.gaya.Menu.MainMenu;
import com.ernestas.gaya.Menu.MenuLayer;
import com.ernestas.gaya.ResourceLoaders.AnimationLoader;
import com.ernestas.gaya.Util.GIFDecoder;
import com.ernestas.gaya.Util.Settings.GameSettings;
import com.ernestas.gaya.Util.Settings.Settings;

public class MenuScreen implements Screen {

    private GayaEntry gaya;
    private InputProcessor input;

    private SpriteBatch batch;

    private Animation animation;

    private float frameCounter;
    private float gifSpeed = 0.8f;

    private MenuLayer menuLayer;

    private boolean goToPlay = false;

    public MenuScreen(GayaEntry gaya) {
        this(gaya, 2f);
    }

    public MenuScreen(GayaEntry gaya, float initialDelay) {
        this.gaya = gaya;
        this.input = gaya.getInputProcessor();

        this.batch = new SpriteBatch();

        animation = GameSettings.getInstance().getAnimationLoader().getResource(AnimationLoader.ResourceId.lightning);
        init(initialDelay);
    }

    private void init(float delay) {
        menuLayer = new MainMenu(input, this);
        ((MainMenu) menuLayer).show(delay);
    }

    @Override
    public void show() {
        frameCounter = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update
        update(delta);

        batch.begin();
        // background
        batch.draw(animation.getKeyFrame(frameCounter * gifSpeed, true), 0, 0, Settings.getInstance().getWidth(), Settings.getInstance().getHeight());
        menuLayer.render(batch);
        batch.end();
    }

    public void update(float delta) {
        frameCounter += delta;
        menuLayer.update(delta);

        if (goToPlay) {
            if (menuLayer.doneHiding()) {
                gaya.setScreen(new PlayScreen(gaya));
            }
        }
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

    public void play() {
        goToPlay = true;
        menuLayer.hide();
    }

    public void exitGame() {
        // Bad solution. Not working on android. Need to reload textures on activity creation
        Gdx.app.exit();
    }
}

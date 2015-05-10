package com.ernestas.gaya.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernestas.gaya.Gameplay.Level;
import com.ernestas.gaya.GayaEntry;
import com.ernestas.gaya.Input.InputProcessor;

import java.io.*;

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

        update(delta);

        batch.begin();
        level.render(batch);
        batch.end();
    }

    public void update(float delta) {
        if (input.isPressedAdvanced(Input.Keys.F9)) {
            System.out.println("SAVING");

            boolean success = saveLevel();

            if (success) {
                System.out.println("SAVED");
            } else {
                System.out.println("SAVE FAILED");
            }
        }
        if (input.isPressedAdvanced(Input.Keys.F12)) {
            System.out.println("LOADING");

            Level level = loadLevel();

            if (level != null) {
                this.level.destroy();
                this.level = level;
                level.repair();
                level.requirementsAfterDeserialization(gaya, input);
                System.out.println("LOADING SUCCESSFULL");
            } else {
                System.out.println("LOADING FAILED");
            }
        }
        level.update(delta);
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

    private boolean saveLevel() {
//        String fileName = "Saves/" + level.getLevelName();
        String fileName = "Saves/level.ser";

        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;

        try {
            fileOut = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(level);
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch(IOException io) {
                System.err.println("Failed to close a stream after saving level.");
                io.printStackTrace();
            }
        }

        return true;
    }

    public Level loadLevel() {
        Level level = null;
        String fileName = "Saves/level.ser";

        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(fileName);
            in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            level = (Level) o;
        } catch(IOException i) {
            i.printStackTrace();
            return null;
        } catch(ClassNotFoundException c) {
            c.printStackTrace();
            return null;
        } finally {
            try {
                if (fileIn != null) {
                    fileIn.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch(IOException e) {
                System.err.println("Failed to close a stream after loading level.");
                e.printStackTrace();
            }
        }

        return level;
    }

    public Level getLevel() { return level; }
}

package com.ernestas.gaya.Overlays.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ernestas.gaya.Input.InputProcessor;

public interface OverlayItem {

    public void update(InputProcessor input, float delta);
    public void render(SpriteBatch batch);
    public void render(SpriteBatch batch, float alpha);
    public Rectangle getBounds();

}

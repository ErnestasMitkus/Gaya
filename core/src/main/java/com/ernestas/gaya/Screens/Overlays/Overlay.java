package com.ernestas.gaya.Screens.Overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Overlay {

    public void update(float delta);
    public void render(SpriteBatch batch);
    public void renderBounds(ShapeRenderer shapeRenderer);

}

package com.ernestas.gaya.Screens.Overlays.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ernestas.gaya.Input.InputProcessor;

public interface OverlayItem {

    public void update(InputProcessor input, float delta);
    public Sprite getSprite();

}

package com.ernestas.gaya.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ernestas.gaya.Util.Settings.Settings;
import com.ernestas.gaya.Util.Vectors.Vector2f;

public class InputProcessor implements com.badlogic.gdx.InputProcessor {

    public static final int KEY_UP = 0;
    public static final int KEY_PRESSED = 1;
    public static final int KEY_HOLD = 2;

    private int keys[] = new int[256];

    private boolean isAndroid = false;
    private boolean mouseDown;
    private Vector2f mousePos = new Vector2f();

    private boolean accelerometerAvailable = false;

    public InputProcessor(boolean isAndroid) {
        this.isAndroid = isAndroid;
        if (isAndroid) {
            accelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        }
        for (int i = 0; i < keys.length; ++i) {
            keys[i] = KEY_UP;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keys[keycode] = KEY_PRESSED;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys[keycode] = KEY_UP;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseDown = true;
//        if (isAndroid) {
//            if (screenX < Settings.getInstance().getWidth() / 2) {
//                if (keys[Input.Keys.A] == KEY_UP) {
//                    keys[Input.Keys.A] = KEY_PRESSED;
//                } else {
//                    keys[Input.Keys.A] = KEY_HOLD;
//                }
//                keys[Input.Keys.D] = KEY_UP;
//            } else {
//                if (keys[Input.Keys.D] == KEY_UP) {
//                    keys[Input.Keys.D] = KEY_PRESSED;
//                } else {
//                    keys[Input.Keys.D] = KEY_HOLD;
//                }
//                keys[Input.Keys.A] = KEY_UP;
//            }
//
//            return true;
//        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseDown = false;
//        if (isAndroid) {
//            keys[Input.Keys.A] = KEY_UP;
//            keys[Input.Keys.D] = KEY_UP;
//            return true;
//        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseDown = true;
        mousePos.x = screenX;
        mousePos.y = Settings.getInstance().getHeight() - screenY;
//        if (isAndroid) {
//            if (screenX < Settings.getInstance().getWidth() / 2) {
//                if (keys[Input.Keys.A] == KEY_UP) {
//                    keys[Input.Keys.A] = KEY_PRESSED;
//                } else {
//                    keys[Input.Keys.A] = KEY_HOLD;
//                }
//                keys[Input.Keys.D] = KEY_UP;
//            } else {
//                if (keys[Input.Keys.D] == KEY_UP) {
//                    keys[Input.Keys.D] = KEY_PRESSED;
//                } else {
//                    keys[Input.Keys.D] = KEY_HOLD;
//                }
//                keys[Input.Keys.A] = KEY_UP;
//            }
//
//            return true;
//        }

        return false;
    }

    public void update() {
        if (isAndroid && accelerometerAvailable) {
            float accelX = Gdx.input.getAccelerometerX(); // < 0 go right, > 0 go left
            float accelY = Gdx.input.getAccelerometerY(); // < 0 go up, > 0 go down
            float accelZ = Gdx.input.getAccelerometerZ(); // ~10 upside up, ~-10 upside down

            // make callibration in settings.
            float xCalib = 1;
            float yCalib = 1;

            keys[Input.Keys.A] = (accelX > xCalib) ? (keys[Input.Keys.A] == KEY_HOLD ? KEY_HOLD : KEY_PRESSED) : KEY_UP;
            keys[Input.Keys.D] = (accelX < -xCalib) ? (keys[Input.Keys.D] == KEY_HOLD ? KEY_HOLD : KEY_PRESSED) : KEY_UP;

            keys[Input.Keys.W] = (accelY < -yCalib) ? (keys[Input.Keys.W] == KEY_HOLD ? KEY_HOLD : KEY_PRESSED) : KEY_UP;
            keys[Input.Keys.S] = (accelY > yCalib) ? (keys[Input.Keys.S] == KEY_HOLD ? KEY_HOLD : KEY_PRESSED) : KEY_UP;
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.x = screenX;
        mousePos.y = Settings.getInstance().getHeight() - screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public int getKeyStatus(int keycode) {
        return keys[keycode];
    }

    public boolean isPressed(int keycode) {
        return keys[keycode] == KEY_PRESSED;
    }

    public boolean isHold(int keycode) {
        return keys[keycode] == KEY_HOLD;
    }

    public boolean isPressedAdvanced(int keycode) {
        boolean result = isPressed(keycode);
        keys[keycode] = KEY_HOLD;
        return result;
    }

    public boolean mouseDown() { return mouseDown; }
    public Vector2f getMousePos() { return mousePos; }

}

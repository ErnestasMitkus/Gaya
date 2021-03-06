package com.ernestas.gaya.Util.Vectors;

import com.ernestas.gaya.Util.SerializationRepair;

import java.io.Serializable;

public class Vector2f implements Serializable, SerializationRepair {
    private static final long serialVersionUID = 6785371864150261489L;

    public float x;
    public float y;

    public Vector2f() {
        x = 0;
        y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2f onetized() {
        if (this.x <= 1 && this.y <= 1) {
            return new Vector2f(this);
        } else {
            float max = this.x > this.y ? this.x : this.y;
            return new Vector2f(this.x / max, this.y / max);
        }
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public Vector2f scale(float scale) {
        return new Vector2f(this.x * scale, this.y * scale);
    }

    @Override
    public void repair() {}
}

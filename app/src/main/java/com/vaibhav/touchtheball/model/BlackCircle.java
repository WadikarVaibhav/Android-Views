package com.vaibhav.touchtheball.model;

import android.graphics.Color;
import android.graphics.Paint;

import com.vaibhav.touchtheball.Constants;

public class BlackCircle {


    private float currentY;
    private float currentX;
    private final float radius;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public BlackCircle(float currentX, float currentY) {
        this.currentY = currentY;
        this.currentX = currentX;
        this.radius = Constants.BLACK_CIRCLE_RADIUS;
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public Paint getPaint() {
        return paint;
    }

    public float getCurrentY() {
        return currentY;
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getRadius() {
        return radius;
    }
}

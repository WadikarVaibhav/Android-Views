package com.vaibhav.touchtheball.model;

import android.graphics.Color;
import android.graphics.Paint;

public class WhiteCircle {

    private static final float CIRCLE_STROKE = 2.0f;
    private float currentY;
    private final float currentX;
    private float radius;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WhiteCircle( float currentX, float currentY) {
        this.currentY = currentY;
        this.currentX = currentX;
        this.radius = 0;
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(CIRCLE_STROKE);
    }

    public static float getCircleStroke() {
        return CIRCLE_STROKE;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getPaint() {
        return paint;
    }
}

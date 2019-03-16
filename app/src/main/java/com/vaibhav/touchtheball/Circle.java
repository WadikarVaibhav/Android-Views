package com.vaibhav.touchtheball;

import android.graphics.Color;
import android.graphics.Paint;

public class Circle {

    private float currentX;
    private float currentY;
    private float radius;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Circle(float currentX, float currentY, float radius) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.radius = radius;
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(2.0f);
        this.paint.setColor(Color.BLACK);
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}

package com.vaibhav.touchtheball.model;

import android.graphics.Color;
import android.graphics.Paint;

public class WhiteCircle {

    private static final float CIRCLE_STROKE = 2.0f;
    public static final int WHITE_BALL_INITIAL_SPEED = 5;
    public static final int INITIAL_SCORE = 1;
    private float currentY;
    private final float currentX;
    private float radius;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float speed;
    private int score;

    public WhiteCircle(float currentX, float currentY) {
        this.currentY = currentY;
        this.currentX = currentX;
        this.radius = 0;
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(CIRCLE_STROKE);
        this.speed = WHITE_BALL_INITIAL_SPEED;
        this.score = INITIAL_SCORE;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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

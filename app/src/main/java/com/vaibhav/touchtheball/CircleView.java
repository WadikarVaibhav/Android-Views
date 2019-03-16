package com.vaibhav.touchtheball;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CircleView extends View {

    List<Circle> whiteCircles = new ArrayList<>();
    Circle blackCircle;
    boolean isPressed = false;
    boolean startFalling = false;
    String state = Constants.END_GAME;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackCircle = new Circle(0, 0, 30);
        setWillNotDraw(false);
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (state == Constants.END_GAME) {
            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
        } else {
            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
            for (Circle circle : whiteCircles) {
                canvas.drawCircle(circle.getCurrentX(), circle.getCurrentY(), circle.getRadius(), circle.getPaint());
            }
        }
    }

    public void stopFallingCircles() {
        startFalling = false;
    }

    public void pauseFallingCircles() {
        startFalling = false;
    }

    public void startFallingCircles() {
        startFalling = true;
        CircleFallAsynTasks circleFallAsynTasks = new CircleFallAsynTasks(whiteCircles);
        circleFallAsynTasks.execute();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                if (state == Constants.START_GAME) {
                    return handleCircleCreation(event);
                } else {
                    return handleBallMovement(event);
                }
            case MotionEvent.ACTION_UP:
                isPressed = false;
                handlePressUp(event);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        blackCircle.setCurrentX(getWidth() / 2);
        blackCircle.setCurrentY(getHeight() - blackCircle.getRadius());
        invalidate();
    }

    private boolean handleBallMovement(MotionEvent event) {
        float touchX = event.getX();
        if (touchX > blackCircle.getCurrentX()) {
            blackCircle.setCurrentX(blackCircle.getCurrentX() + 30);
        } else {
            blackCircle.setCurrentX(blackCircle.getCurrentX() - 30);
        }
        invalidate();
        return true;
    }

    private void handlePressUp(MotionEvent event) {
        isPressed = false;
    }

    private boolean handleCircleCreation(MotionEvent event) {
        Circle circle = new Circle(event.getX(), event.getY(), 0);
        whiteCircles.add(circle);
        CircleCreationAsynTask circleAsynTask = new CircleCreationAsynTask(circle);
        circleAsynTask.execute();
        return true;
    }

    class CircleCreationAsynTask extends AsyncTask<Float, Float, Float> {

        Circle circle;

        public CircleCreationAsynTask(Circle circle) {
            this.circle = circle;
        }

        @Override
        protected Float doInBackground(Float... floats) {
            while (isPressed) {
                try {
                    Thread.sleep(100);

                    circle.setRadius(circle.getRadius() + 3f);
                    if (circle.getCurrentX() < circle.getRadius()) {
                        isPressed = false;
                    }
                    publishProgress(circle.getRadius());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return circle.getRadius();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            circle.setRadius(circle.getRadius());
            invalidate();
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);
            circle.setRadius(circle.getRadius());
            invalidate();
        }
    }

    class CircleFallAsynTasks extends AsyncTask<Float, Float, Float> {

        List<Circle> whiteCircles;

        public CircleFallAsynTasks(List<Circle> whiteCircles) {
            this.whiteCircles = whiteCircles;
        }

        @Override
        protected Float doInBackground(Float... floats) {
            while (startFalling) {
                try {
                    Thread.sleep(10);
                    for (Circle whiteCircle : whiteCircles) {
                        if (whiteCircle.getCurrentY() >= getHeight() + whiteCircle.getRadius()) {
                            whiteCircle.setCurrentY(whiteCircle.getRadius());
                        } else {
                            whiteCircle.setCurrentY(whiteCircle.getCurrentY() + 3);
                        }
                        double dist = Math.sqrt(Math.pow(whiteCircle.getCurrentX() - blackCircle.getCurrentX(), 2)
                                + Math.pow(whiteCircle.getCurrentY() - blackCircle.getCurrentY(), 2));

                        if (whiteCircle.getRadius() + blackCircle.getRadius() >= dist) {
                            startFalling = false;
                        }

                        publishProgress(whiteCircle.getCurrentY());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            for (Circle circle : whiteCircles) {
                circle.setCurrentY(circle.getCurrentY());
            }
            invalidate();
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);
            for (Circle circle : whiteCircles) {
                circle.setCurrentY(circle.getCurrentY());
            }
            invalidate();
        }

    }
}

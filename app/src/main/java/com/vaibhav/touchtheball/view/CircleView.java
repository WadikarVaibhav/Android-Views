package com.vaibhav.touchtheball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vaibhav.touchtheball.Communicator;
import com.vaibhav.touchtheball.Constants;
import com.vaibhav.touchtheball.model.BlackCircle;
import com.vaibhav.touchtheball.model.WhiteCircle;

import java.util.ArrayList;
import java.util.List;

public class CircleView extends View {

    private String state;
    private BlackCircle blackCircle;
    private List<WhiteCircle> whiteCircles;
    private static final float BLACK_CIRCLE_RADIUS = 40;
    private Communicator communicator;
    private boolean isReleasedTouch = true;
    private boolean continueCircleFall = false;
    private int lives;
    private int score;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        whiteCircles = new ArrayList<WhiteCircle>();
        blackCircle = new BlackCircle(getWidth()/2, (getHeight() - BLACK_CIRCLE_RADIUS), BLACK_CIRCLE_RADIUS);
        setWillNotDraw(false);
        lives = 3;
        score = 0;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state.equals(Constants.NEW) || state.equals(Constants.END)) {
            whiteCircles = new ArrayList<>();
            lives = 3;
            score = 0;
            communicator.updateLives(lives);
            communicator.updateScore(score);
            blackCircle = new BlackCircle(getWidth()/2, (getHeight() - BLACK_CIRCLE_RADIUS), BLACK_CIRCLE_RADIUS);
            invalidate();
        }
        this.state = state;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        blackCircle.setCurrentX(getWidth()/2);
        blackCircle.setCurrentY(getHeight() - BLACK_CIRCLE_RADIUS);
        invalidate();
    }

    public void stopCircleFall() {
        continueCircleFall = false;
    }
    public void pauseCircleFall() {
        continueCircleFall = false;
    }

    public void resumeCircleFall() {
        continueCircleFall = true;
        CircleFall circleFall = new CircleFall(whiteCircles);
        circleFall.execute();
    }

    public void createCircleFall() {
        continueCircleFall = true;
        CircleFall circleFall = new CircleFall(whiteCircles);
        circleFall.execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getState().equals(Constants.END)) {
            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
        } else if (getState().equals(Constants.PLAY)) {
            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
            for (WhiteCircle whiteCircle: whiteCircles) {
                checkCollision(whiteCircle);
                updateScore(whiteCircle);
                canvas.drawCircle(whiteCircle.getCurrentX(), whiteCircle.getCurrentY(), whiteCircle.getRadius(), whiteCircle.getPaint());
            }
        } else if (getState().equals(Constants.NEW)) {
            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
            for (WhiteCircle whiteCircle: whiteCircles) {
                canvas.drawCircle(whiteCircle.getCurrentX(), whiteCircle.getCurrentY(), whiteCircle.getRadius(), whiteCircle.getPaint());
            }
        } else if (getState().equals(Constants.PAUSE)) {

        } else {

        }
    }

    private void updateScore(WhiteCircle whiteCircle) {
        if (whiteCircle.getCurrentY() - whiteCircle.getRadius() >= getHeight()) {
            score++;
            communicator.updateScore(score);
            whiteCircle.setCurrentY(whiteCircle.getRadius());
            if (whiteCircle.getSpeed() < 10f) {
                whiteCircle.setSpeed(whiteCircle.getSpeed()*1.25f);
            }
        }
    }

    private void checkCollision(WhiteCircle whiteCircle) {
        double dist = Math.sqrt(Math.pow(whiteCircle.getCurrentX() - blackCircle.getCurrentX(), 2)
                + Math.pow(whiteCircle.getCurrentY() - blackCircle.getCurrentY(), 2));
        if (whiteCircle.getRadius() + blackCircle.getRadius() >= dist) {
            lives--;
            communicator.updateLives(lives);
            if (lives <= 0) {
                startNewGame();
            } else {
                whiteCircle.setCurrentY(whiteCircle.getRadius());
            }
        }
        return;
    }

    private void startNewGame() {
        stopCircleFall();
        blackCircle = new BlackCircle(getWidth()/2, (getHeight() - BLACK_CIRCLE_RADIUS), BLACK_CIRCLE_RADIUS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return handleTouchDown(event);
            case MotionEvent.ACTION_UP:
                return handleTouchRelease();
            default:
                return true;
        }
    }

    private boolean handleTouchDown(MotionEvent event) {
        if (getState().equals(Constants.NEW)) {
            return createNewCircles(event);
        } else if (getState().equals(Constants.PLAY)) {
            return moveBlackBall(event);
        }
        return true;
    }

    private boolean moveBlackBall(MotionEvent event) {
        float touchX = event.getX();
        if (touchX > getWidth()/2) {
            blackCircle.setCurrentX(blackCircle.getCurrentX() + 30);
        } else {
            blackCircle.setCurrentX(blackCircle.getCurrentX() - 30);
        }
        invalidate();
        return true;
    }

    private boolean createNewCircles(MotionEvent event) {
        WhiteCircle whiteCircle = new WhiteCircle(event.getX(), event.getY());
        whiteCircles.add(whiteCircle);
        isReleasedTouch = false;
        WhiteCircleCreation whiteCircleCreation = new WhiteCircleCreation(whiteCircle);
        whiteCircleCreation.execute();
        return true;
    }

    private boolean handleTouchRelease() {
        isReleasedTouch = true;
        return true;
    }

    class CircleFall extends AsyncTask<Float, Float, Float> {

        List<WhiteCircle> whiteCircles;

        public CircleFall(List<WhiteCircle> whiteCircles) {
            this.whiteCircles = whiteCircles;
        }

        @Override
        protected Float doInBackground(Float... floats) {
            while (continueCircleFall) {
                try {
                    Thread.sleep(10);
                    for (WhiteCircle whiteCircle: whiteCircles) {
                        whiteCircle.setCurrentY(whiteCircle.getCurrentY()+whiteCircle.getSpeed());
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
            for (WhiteCircle whiteCircle: whiteCircles) {
                whiteCircle.setCurrentY(whiteCircle.getCurrentY());
            }
            invalidate();
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);
            for (WhiteCircle whiteCircle: whiteCircles) {
                whiteCircle.setCurrentY(whiteCircle.getCurrentY());
            }
            invalidate();
        }
    }

    class WhiteCircleCreation extends AsyncTask<Float, Float, Float> {

        WhiteCircle whiteCircle;

        public WhiteCircleCreation(WhiteCircle whiteCircle) {
            this.whiteCircle = whiteCircle;
        }

        @Override
        protected Float doInBackground(Float... floats) {
            while (!isReleasedTouch) {
                try {
                    Thread.sleep(50);
                    if (!isWhiteCircleWithinBoundary(whiteCircle)) {
                        isReleasedTouch = true;
                    }
                    whiteCircle.setRadius(whiteCircle.getRadius() + 5f);
                    publishProgress(whiteCircle.getRadius());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return whiteCircle.getRadius();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            whiteCircle.setRadius(whiteCircle.getRadius());
            invalidate();
        }

        private boolean isWhiteCircleWithinBoundary(WhiteCircle whiteCircle) {
            if ((whiteCircle.getRadius() + 5 >= whiteCircle.getCurrentX())
                    || (whiteCircle.getCurrentY() <= whiteCircle.getRadius() + 5)
                    || (whiteCircle.getCurrentX() + whiteCircle.getRadius() + 5 >= getWidth())
                    || (whiteCircle.getCurrentY() + whiteCircle.getRadius() + 5 >= getHeight())) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);
            whiteCircle.setRadius(whiteCircle.getRadius());
            invalidate();
        }
    }
}

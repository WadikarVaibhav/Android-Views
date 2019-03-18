//package com.vaibhav.touchtheball;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.os.AsyncTask;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CircleView extends View {
//
//    List<Circle> whiteCircles = new ArrayList<>();
//    Circle blackCircle;
//    boolean isPressed = false;
//    boolean startFalling = false;
//    String state = Constants.END;
//    int lives = 3;
//    int score = 0;
//    Communicator comminicator;
//    boolean isCollision = false;
//
//    public Communicator getComminicator() {
//        return comminicator;
//    }
//
//    public void setComminicator(Communicator comminicator) {
//        this.comminicator = comminicator;
//    }
//
//    public CircleView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        blackCircle = new Circle(0, 0, 30);
//        setWillNotDraw(false);
//    }
//
//    public void setState(String state) {
//        this.state = state;
//        if (this.state == Constants.START) {
//            lives = 3;
//            score = 0;
//            comminicator.updateLives(lives);
//            comminicator.updateScore(score);
//        }
//        if (this.state == Constants.END) {
//            startFalling = false;
//            whiteCircles.clear();
//        }
//    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (state == Constants.END) {
//            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
//        } else {
//            canvas.drawCircle(blackCircle.getCurrentX(), blackCircle.getCurrentY(), blackCircle.getRadius(), blackCircle.getPaint());
//            for (Circle circle : whiteCircles) {
//                if (isCollision) {
//                    lives--;
//                    if (comminicator != null) {
//                        comminicator.updateLives(lives);
//                        isCollision = false;
//                    }
//                    if (lives <= 0) {
//                        this.setState(Constants.END);
//                    }
//                }
////                if (circle.getCurrentY() + circle.getRadius() >= getHeight()) {
////                    score++;
////                    comminicator.updateScore(score);
////                }
//                canvas.drawCircle(circle.getCurrentX(), circle.getCurrentY(), circle.getRadius(), circle.getPaint());
//            }
//        }
//    }
//
//    public void stopFallingCircles() {
//        startFalling = false;
//    }
//
//    public void pauseFallingCircles() {
//        startFalling = false;
//    }
//
//    public void startFallingCircles() {
//        startFalling = true;
//        CircleFallAsynTasks circleFallAsynTasks = new CircleFallAsynTasks(whiteCircles);
//        circleFallAsynTasks.execute();
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                isPressed = true;
//                if (state == Constants.START) {
//                    return createCircle(event);
//                } else {
//                    return moveBallOnTouch(event);
//                }
//            case MotionEvent.ACTION_UP:
//                isPressed = false;
//                handlePressUp(event);
//                return true;
//            default:
//                return true;
//        }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        blackCircle.setCurrentX(getWidth() / 2);
//        blackCircle.setCurrentY(getHeight() - blackCircle.getRadius());
//        invalidate();
//    }
//
//    private boolean moveBallOnTouch(MotionEvent event) {
//        float touchX = event.getX();
//        if (touchX > blackCircle.getCurrentX()) {
//            blackCircle.setCurrentX(blackCircle.getCurrentX() + 30);
//        } else {
//            blackCircle.setCurrentX(blackCircle.getCurrentX() - 30);
//        }
//        invalidate();
//        return true;
//    }
//
//    private void handlePressUp(MotionEvent event) {
//        isPressed = false;
//    }
//
//    private boolean createCircle(MotionEvent event) {
//        Circle circle = new Circle(event.getX(), event.getY(), 0);
//        whiteCircles.add(circle);
//        CircleCreationAsynTask circleAsynTask = new CircleCreationAsynTask(circle);
//        circleAsynTask.execute();
//        return true;
//    }
//
//    class CircleCreationAsynTask extends AsyncTask<Float, Float, Float> {
//
//        Circle circle;
//
//        public CircleCreationAsynTask(Circle circle) {
//            this.circle = circle;
//        }
//
//        @Override
//        protected Float doInBackground(Float... floats) {
//            while (isPressed) {
//                try {
//                    Thread.sleep(50);
//
//                    circle.setRadius(circle.getRadius() + 5f);
//                    if (circle.getCurrentX() < circle.getRadius() || circle.getRadius() + circle.getCurrentX() >= getWidth()) {
//                        isPressed = false;
//                    }
//                    publishProgress(circle.getRadius());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return circle.getRadius();
//        }
//
//        @Override
//        protected void onProgressUpdate(Float... values) {
//            super.onProgressUpdate(values);
//            circle.setRadius(circle.getRadius());
//            invalidate();
//        }
//
//        @Override
//        protected void onPostExecute(Float aFloat) {
//            super.onPostExecute(aFloat);
//            circle.setRadius(circle.getRadius());
//            invalidate();
//        }
//    }
//
//    class CircleFallAsynTasks extends AsyncTask<Float, Float, Float> {
//
//        List<Circle> whiteCircles;
//
//        public CircleFallAsynTasks(List<Circle> whiteCircles) {
//            this.whiteCircles = whiteCircles;
//        }
//
//        @Override
//        protected Float doInBackground(Float... floats) {
//
//            while (startFalling) {
//                try {
//                    Thread.sleep(10);
//                    for (Circle whiteCircle : whiteCircles) {
//                        if (whiteCircle.getCurrentY() >= getHeight() + whiteCircle.getRadius()) {
//                            whiteCircle.setCurrentY(whiteCircle.getRadius());
//                        } else {
//                            whiteCircle.setCurrentY(whiteCircle.getCurrentY() + 2);
//                        }
//
//                        double dist = Math.sqrt(Math.pow(whiteCircle.getCurrentX() - blackCircle.getCurrentX(), 2)
//                                + Math.pow(whiteCircle.getCurrentY() - blackCircle.getCurrentY(), 2));
//
//                        if (whiteCircle.getRadius() + blackCircle.getRadius() >= dist) {
//                            resetCirclesPosition(whiteCircle);
//                            isCollision = true;
//                            startFalling = true;
//                        }
//
//                        publishProgress(whiteCircle.getCurrentY());
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        private void resetCirclesPosition(Circle whiteCircle) {
//            whiteCircle.setCurrentY(whiteCircle.getRadius());
//        }
//
//        @Override
//        protected void onProgressUpdate(Float... values) {
//            super.onProgressUpdate(values);
//            for (Circle circle : whiteCircles) {
//
//                circle.setCurrentY(circle.getCurrentY());
//            }
//            invalidate();
//        }
//
//        @Override
//        protected void onPostExecute(Float aFloat) {
//            super.onPostExecute(aFloat);
//            for (Circle circle : whiteCircles) {
//                circle.setCurrentY(circle.getCurrentY());
//            }
//            invalidate();
//        }
//
//    }
//}

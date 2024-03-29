package com.vaibhav.touchtheball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Comminicator{

    private Button pauseResumeButton;
    private Button startEndButton;
    private TextView livesText;
    private TextView scoreText;
    private String state;
    private int score;
    private int lives;
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pauseResumeButton = (Button) findViewById(R.id.pause_resume);
        startEndButton = (Button) findViewById(R.id.start_end);
        circleView = (com.vaibhav.touchtheball.CircleView) findViewById(R.id.circleView);
        livesText = (TextView) findViewById(R.id.lives);
        scoreText = (TextView) findViewById(R.id.score);
        pauseResumeButton.setOnClickListener(this);
        startEndButton.setOnClickListener(this);
        setup();
    }

    private void setup() {
        lives = 3;
        score = 0;
        state = Constants.END_GAME;
        startEndButton.setText(Constants.CREAT_BALLS);
        pauseResumeButton.setText(Constants.PAUSE_GAME);
        pauseResumeButton.setEnabled(false);
        circleView.setComminicator((Comminicator) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_end:
                if (state == Constants.END_GAME) {
                    startEndButton.setText(Constants.PLAY_GAME);
                    state = Constants.CREAT_BALLS;
                    circleView.setState(Constants.CREAT_BALLS);
                    circleView.stopFallingCircles();
                    pauseResumeButton.setEnabled(false);
                } else if (state == Constants.CREAT_BALLS) {
                    startEndButton.setText(Constants.END_GAME);
                    state = Constants.PLAY_GAME;
                    circleView.setState(Constants.PLAY_GAME);
                    circleView.startFallingCircles();
                    pauseResumeButton.setEnabled(true);
                } else if (state == Constants.PLAY_GAME) {
                    startEndButton.setText(Constants.CREAT_BALLS);
                    circleView.stopFallingCircles();
                    state = Constants.END_GAME;
                    circleView.setState(Constants.END_GAME);
                    pauseResumeButton.setEnabled(false);
                } else if (state == Constants.RESUME_GAME) {

                }
                break;
            case R.id.pause_resume:
                if (state == Constants.PLAY_GAME) {
                    state = Constants.PAUSE_GAME;
                    circleView.pauseFallingCircles();
                    pauseResumeButton.setText("RESUME");
                } else if (state == Constants.PAUSE_GAME) {
                    state = Constants.PLAY_GAME;
                    circleView.startFallingCircles();
                    pauseResumeButton.setText("PAUSE");
                }

                break;
        }
    }

    @Override
    public void updateScore(int score) {
        scoreText.setText(score+"");
    }

    @Override
    public void updateLives(int lives) {
        livesText.setText(lives+"");
    }
}

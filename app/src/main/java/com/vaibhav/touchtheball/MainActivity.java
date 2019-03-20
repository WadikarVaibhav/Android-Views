package com.vaibhav.touchtheball;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import com.vaibhav.touchtheball.view.CircleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Communicator {

    public static final String GAME_OVER_TITLE = "Game Over";
    public static final String SCORE_MESSAGE = "You Scored ";
    public static final String START_NEW_GAME_MESSAGE = "Start New";
    private Button pauseResumeButton;
    private Button stateButton;
    private TextView livesText;
    private TextView scoreText;
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResources();
    }

    private void initResources() {
        pauseResumeButton = (Button) findViewById(R.id.pause_resume);
        stateButton = (Button) findViewById(R.id.start_end);
        circleView = (com.vaibhav.touchtheball.view.CircleView) findViewById(R.id.circleView);
        livesText = (TextView) findViewById(R.id.lives);
        scoreText = (TextView) findViewById(R.id.score);

        pauseResumeButton.setOnClickListener(this);
        stateButton.setOnClickListener(this);

        pauseResumeButton.setEnabled(false);
        circleView.setCommunicator((Communicator) this);
        circleView.setState(Constants.END);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_end:
                if (stateButton.getText().toString().equals(Constants.NEW)) {
                    handleNewState();
                } else if (stateButton.getText().toString().equals(Constants.PLAY)) {
                    handlePlayState();
                } else if (stateButton.getText().toString().equals(Constants.END)) {
                    handleEndState();
                }
                break;
            case R.id.pause_resume:
                if (pauseResumeButton.getText().toString().equals(Constants.PAUSE)) {
                    handlePauseState();
                } else if (pauseResumeButton.getText().toString().equals(Constants.RESUME)) {
                    handleResumeState();
                }
                break;
        }
    }

    private void handlePlayState() {
        changeStateButtonText(Constants.END);
        circleView.setState(Constants.PLAY);
        pauseResumeButton.setEnabled(true);
        circleView.createCircleFall();
    }

    private void handleEndState() {
        changeStateButtonText(Constants.NEW);
        circleView.setState(Constants.END);
        circleView.stopCircleFall();
        pauseResumeButton.setEnabled(false);
        changePauseButtonState(Constants.PAUSE);
    }

    private void handleNewState() {
        changeStateButtonText(Constants.PLAY);
        circleView.setState(Constants.NEW);
        circleView.stopCircleFall();
        pauseResumeButton.setEnabled(false);
        changePauseButtonState(Constants.PAUSE);
    }

    private void changeStateButtonText(String newState) {
        stateButton.setText(newState);
    }

    private void changePauseButtonState(String newState) {
        pauseResumeButton.setText(newState);
    }

    private void handleResumeState() {
        circleView.resumeCircleFall();
        changePauseButtonState(Constants.PAUSE);
    }

    private void handlePauseState() {
        circleView.stopCircleFall();
        changePauseButtonState(Constants.RESUME);
    }

    @Override
    public void updateScore(int score) {
        scoreText.setText(score+"");
    }

    @Override
    public void updateLives(int lives, int score) {
        if (lives <= 0) {
            showPopup(score);
            stateButton.setText(Constants.NEW);
            circleView.setState(Constants.NEW);
            changePauseButtonState(Constants.PAUSE);
            pauseResumeButton.setEnabled(false);
        }
        livesText.setText(lives+"");
    }

    private void showPopup(int score) {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle(GAME_OVER_TITLE).setMessage(SCORE_MESSAGE +score)
            .setNegativeButton(START_NEW_GAME_MESSAGE, null)
            .show();
    }
}

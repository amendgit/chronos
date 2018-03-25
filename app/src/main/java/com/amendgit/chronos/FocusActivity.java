package com.amendgit.chronos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    private TickTockView mCountDownView;
    private Button mStartButton;
    private Button mPauseButton;
    private Button mResumeButton;
    private Button mStopButton;

    final String TAG = "FocusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_focus);
        FocusNotification.startForeground(this);

        mCountDownView = super.findViewById(R.id.ticktock_countdown_view);
        mCountDownView.setOnTickDelegate(new TickTockView.TickTockDelegate() {
            @Override
            public String getTickText(long timeRemainingInMillis) {
                int s = (int) ( timeRemainingInMillis /  1000)            % 60;
                int m = (int) ((timeRemainingInMillis / (1000 * 60))      % 60);
                int h = (int) ((timeRemainingInMillis / (1000 * 60 * 60)) % 24);
                return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
            }

            @Override
            public void onTickFinish() {
                onStopFocus();
            }
        });

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onStartFocus(); }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onPauseFocus(); }
        });

        mResumeButton = findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onResumeFocus(); }
        });

        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onStopFocus(); }
        });
    }

    void onStartFocus() {
        long countDownIntervalInMillis = 10 * 1000;
        mCountDownView.start(countDownIntervalInMillis);
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void onPauseFocus() {
        mCountDownView.pause();
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    void onResumeFocus() {
        mCountDownView.resume();
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void onStopFocus() {
        mCountDownView.stop();
        mStartButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }
}

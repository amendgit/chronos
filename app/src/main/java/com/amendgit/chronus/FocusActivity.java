package com.amendgit.chronus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amendgit.chronus.TickTockView;

import java.util.Calendar;
import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    TickTockView mCountDownView;
    Button mStartButton;
    Button mPauseButton;
    Button mContinueButton;
    Button mStopButton;

    enum FocusState {
        START,
        PAUSE,
        STOP
    }
    FocusState mFocusState = FocusState.STOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        mCountDownView = findViewById(R.id.ticktock_countdown_view);
        mCountDownView.setOnTickListener(new TickTockView.OnTickListener() {
            @Override
            public String getText(long timeRemainingInMillis) {
                int s = (int) ( timeRemainingInMillis /  1000)            % 60;
                int m = (int) ((timeRemainingInMillis / (1000 * 60))      % 60);
                int h = (int) ((timeRemainingInMillis / (1000 * 60 * 60)) % 24);
                return String.format(Locale.CHINESE,"%1$02d:%2$02d:%3$02d", h, m, s);
            }
        });

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar end = Calendar.getInstance();
                end.add(Calendar.MINUTE, 30);
                end.add(Calendar.SECOND, 0);

                Calendar start = Calendar.getInstance();
                start.add(Calendar.MINUTE, -1);
                if (mCountDownView != null) {
                    mCountDownView.start(start, end);
                    mStartButton.setVisibility(View.GONE);
                    mPauseButton.setVisibility(View.VISIBLE);
                }
            }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mCountDownView.pause();
                mPauseButton.setVisibility(View.GONE);
                mContinueButton.setVisibility(View.VISIBLE);
                mStopButton.setVisibility(View.VISIBLE);
            }
        });

        mContinueButton = findViewById(R.id.continue_button);
        mStopButton = findViewById(R.id.stop_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDownView.stop();
    }
}

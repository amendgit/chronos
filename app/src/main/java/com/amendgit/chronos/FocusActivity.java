package com.amendgit.chronos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        FocusController.getInstance().addDelegate(new FocusController.FocusDelegate() {
            @Override
            public void onTick(long remainMillis) {
                mCountDownView.setTotalTimerInterval(FocusController.getInstance().getTotalTimeInterval());
                mCountDownView.setRemainTimerInterval(remainMillis);
            }

            @Override
            public void onFinish() {
                displayStopUI();
                MediaPlayer mediaPlayer = MediaPlayer.create(FocusActivity.this, R.raw.beat);
                mediaPlayer.start();
            }

            @Override
            public void onStateChange(FocusState state) {
                if (state == FocusState.RUNNING) {
                    displayRunUI();
                } else if (state == FocusState.SUSPENDING) {
                    displayPauseUI();
                } else if (state == FocusState.READY) {
                    displayStopUI();
                }
            }
        });

        mCountDownView = super.findViewById(R.id.ticktock_countdown_view);

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long countDownIntervalInMillis = 10 * 1000;
                FocusController.getInstance().start(countDownIntervalInMillis);
            }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { FocusController.getInstance().pause(); }
        });

        mResumeButton = findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { FocusController.getInstance().resume();  }
        });

        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { FocusController.getInstance().stop(); }
        });
    }

    void displayRunUI() {
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void displayPauseUI() {
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    void displayStopUI() {
        mStartButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }
}

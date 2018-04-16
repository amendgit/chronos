package com.amendgit.chronos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class FocusActivity extends AppCompatActivity {
    private TickTockView mCountDownView;
    private Button mStartButton;
    private Button mPauseButton;
    private Button mResumeButton;
    private Button mStopButton;
    private FocusController mController;

    final String TAG = "FocusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_focus);
        FocusNotification.startForeground(this);
        mController = FocusController.getInstance();
        mController.addDelegate(new FocusController.FocusDelegate() {
            @Override
            public void onTickTock(long remainMillis) {
                mCountDownView.setTotalTimeInterval(mController.getTotalTimeInterval());
                mCountDownView.setRemainTimeInterval(remainMillis);
            }

            @Override
            public void onFinish() {
                // We should be ready for next focus when previous focus finished.
                displayReadyUI();
                MediaPlayer mediaPlayer = MediaPlayer.create(FocusActivity.this, R.raw.beat);
                mediaPlayer.start();
            }

            @Override
            public void onStateChange(FocusState state) {
                if (state == FocusState.FOCUSING) {
                    displayFocusingUI();
                } else if (state == FocusState.SUSPENDING) {
                    displaySuspendingUI();
                } else if (state == FocusState.READY) {
                    displayReadyUI();
                }
            }
        });

        mCountDownView = super.findViewById(R.id.ticktock_countdown_view);

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.start(Constants.TIME_INTERVAL_FOCUS);
            }
        });

        mPauseButton = findViewById(R.id.pause_button);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mController.pause(); }
        });

        mResumeButton = findViewById(R.id.resume_button);
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mController.resume();  }
        });

        mStopButton = findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mController.stop(); }
        });
    }

    void displayFocusingUI() {
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    void displaySuspendingUI() {
        mStartButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    void displayReadyUI() {
        mStartButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }
}

package com.amendgit.chronos;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

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
            public void onTick(long remainMillis) {
                mCountDownView.setTotalTimerInterval(FocusController.getInstance().getTotalTimeInterval());
                mCountDownView.setRemainTimerInterval(remainMillis);
            }

            @Override
            public void onFinish() {
                displayReadyUI();
                MediaPlayer mediaPlayer = MediaPlayer.create(FocusActivity.this, R.raw.beat);
                mediaPlayer.start();
            }

            @Override
<<<<<<< HEAD
            public void onStateChange(FocusState state) {
                if (state == FocusState.FOCUSING) {
                    displayFocusingUI();
                } else if (state == FocusState.SUSPENDING) {
                    displaySuspendingUI();
                } else if (state == FocusState.READY) {
                    displayReadyUI();
                }
=======
            public void onTickFinish() {
                onStopFocus();
                showFocusFinishedNotification();
>>>>>>> 0f1e37b... add custom notification
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

<<<<<<< HEAD
    void displayFocusingUI() {
=======
    void onStartFocus() {
        long countDownIntervalInMillis = 10 * 1000;
        mCountDownView.start(countDownIntervalInMillis);
>>>>>>> 0f1e37b... add custom notification
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
<<<<<<< HEAD
=======

    void showFocusFinishedNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_focus);

        NotificationManager notificationManager = (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = builder
                .setContentTitle("专注")
                .setContentText("一段美好的专注时光已经完成")
                .setContent(remoteViews)
                .setAutoCancel(true)
                // .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beat))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(1, notification);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beat);
        mediaPlayer.start();
    }
>>>>>>> 0f1e37b... add custom notification
}

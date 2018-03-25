package com.amendgit.chronos;

/**
 * Created by jash on 25/03/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class FocusNotification extends Service {
    final static String TAG = "FocusNotification";

    private RemoteViews mRemoteViews;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private String mTimeLabelText;
    private FocusController mController;

    public FocusNotification() {
        mController = FocusController.getInstance();
        mController.addDelegate(new FocusController.FocusDelegate() {
            @Override
            public void onTick(long remainMillis) {
                if (TimeUtil.millisToLabel(remainMillis).equals(mTimeLabelText)) {
                    return;
                }
                mTimeLabelText = TimeUtil.millisToLabel(remainMillis);
                mRemoteViews.setTextViewText(R.id.time_label, mTimeLabelText);
                mNotificationManager.notify(Constants.NOTIFICATION_ID_FOCUS, mNotificationBuilder.build());
            }

            @Override
            public void onFinish() {
                displayReadyUI();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, intent.getAction());
        if (intent.getAction().equals(Constants.ACTION_START_FOREGROUND)) {
            this.startForeground();
        } else if (intent.getAction().equals(Constants.ACTION_START)) {
            mController.start(Constants.TIME_INTERVAL_FOCUS);
        } else if (intent.getAction().equals(Constants.ACTION_PAUSE)) {
            mController.pause();
        } else if (intent.getAction().equals(Constants.ACTION_RESUME)) {
            mController.resume();
        } else if (intent.getAction().equals(Constants.ACTION_STOP)) {
            mController.stop();
        } else if (intent.getAction().equals(Constants.ACTION_STOP_FOREGROUND)) {
            super.stopForeground(true);
            super.stopSelf();
        }
        return START_STICKY;
    }

    void displayFocusingUI() {
        mRemoteViews.setViewVisibility(R.id.start_group, View.GONE);
        mRemoteViews.setViewVisibility(R.id.pause_group, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.resume_stop_group, View.GONE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID_FOCUS, mNotificationBuilder.build());
    }

    void displaySuspendingUI() {
        mRemoteViews.setViewVisibility(R.id.start_group, View.GONE);
        mRemoteViews.setViewVisibility(R.id.pause_group, View.GONE);
        mRemoteViews.setViewVisibility(R.id.resume_stop_group, View.VISIBLE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID_FOCUS, mNotificationBuilder.build());
    }

    void displayReadyUI() {
        mRemoteViews.setViewVisibility(R.id.start_group, View.VISIBLE);
        mRemoteViews.setViewVisibility(R.id.pause_group, View.GONE);
        mRemoteViews.setViewVisibility(R.id.resume_stop_group, View.GONE);
        mTimeLabelText = "00:00:00";
        mRemoteViews.setTextViewText(R.id.time_label, mTimeLabelText);
        mNotificationManager.notify(Constants.NOTIFICATION_ID_FOCUS, mNotificationBuilder.build());
    }

    void startForeground() {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_focus);

        Intent startIntent = new Intent(this, FocusNotification.class);
        startIntent.setAction(Constants.ACTION_START);
        PendingIntent startPendingIntent = PendingIntent.getService(this, 0, startIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.start_button, startPendingIntent);

        Intent pauseIntent = new Intent(this, FocusNotification.class);
        pauseIntent.setAction(Constants.ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.pause_button, pausePendingIntent);

        Intent resumeIntent = new Intent(this, FocusNotification.class);
        resumeIntent.setAction(Constants.ACTION_RESUME);
        PendingIntent resumePendingIntent = PendingIntent.getService(this, 0, resumeIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.resume_button, resumePendingIntent);

        Intent stopIntent = new Intent(this, FocusNotification.class);
        stopIntent.setAction(Constants.ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.stop_button, stopPendingIntent);

        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_FOCUS);
        Notification notification = mNotificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(mRemoteViews)
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);
    }

    public static void startForeground(Context context) {
        Intent serviceIntent = new Intent(context, FocusNotification.class);
        serviceIntent.setAction(Constants.ACTION_START_FOREGROUND);
        context.startService(serviceIntent);
    }
}
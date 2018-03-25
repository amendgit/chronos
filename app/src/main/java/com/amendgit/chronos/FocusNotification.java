package com.amendgit.chronos;

/**
 * Created by jash on 25/03/2018.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FocusNotification extends Service {
    // final static String TAG = "FocusNotification";

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
        Toast.makeText(this, intent.getAction(), Toast.LENGTH_LONG).show();

        if (intent.getAction().equals(Constants.ACTION_START_FOREGROUND)) {
            this.showNotification();
        }

        /*
        if (intent.getAction().equals(Constants.START_FOREGROUND_ACTION)) {
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Constants.ACTION_STOP)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION_START)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION_NEXT)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Clicked Next");
        } else if (intent.getAction().equals(Constants.STOP_FOREGROUND_ACTION)) {
            Log.i(TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stop", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        */
        return START_STICKY;
    }

    void showNotification() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beat);
        mediaPlayer.start();

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_focus);
        Intent playIntent = new Intent(this, FocusNotification.class);
        playIntent.setAction(Constants.ACTION_START);
        PendingIntent startPendingIntent = PendingIntent.getService(this, 0, playIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.start, startPendingIntent);
        Notification notification = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_FOCUS)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
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
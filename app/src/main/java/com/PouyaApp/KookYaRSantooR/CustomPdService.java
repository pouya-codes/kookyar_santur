package com.PouyaApp.KookYaRSantooR;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import org.puredata.android.service.PdService;

/**
 * Custom PdService to fix PendingIntent FLAG_IMMUTABLE issue on Android S+ (API 31+)
 */
public class CustomPdService extends PdService {
    private static final String CHANNEL_ID = "pd_service_channel";
    private static final String CHANNEL_NAME = "Audio Service";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Audio processing service");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create notification with proper PendingIntent flags for Android S+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, createNotification());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification() {
        // Create an intent that will be fired when the user taps the notification
        Intent intent = new Intent(this, SanturTuner.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Use FLAG_IMMUTABLE for Android S+ (API 31+)
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                flags
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.kookyar)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Audio service is running")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false);

        return builder.build();
    }
}

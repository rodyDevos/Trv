package com.igames2go.t4f.Activities;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.igames2go.t4f.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final String TAG = "T4F";
    public static final int CHAT_NOTIFICATION_ID = 1;
    public static final int TURN_NOTIFICATION_ID = 2;
    public static final int START_END_NOTIFICATION_ID = 3;
    public static final int DEFAULT_NOTIFICATION_ID = 4;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
        	String message = extras.getString("message_to_show");
        	if(message == null) {
        		message = extras.toString();
        	}
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + message);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        message);
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + message);
                // 03/02/2014 lp 
                //sendNotification(message);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, SplashScreen.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LoginPage.class), 0);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icon)
        // .setContentTitle(getString(R.string.notification_app_name))
        // 03/02/2014 lp notification_app_name does not exists
        .setContentTitle(getString(R.string.app_name))
        .setStyle(new NotificationCompat.BigTextStyle()
        
        .bigText(msg))
        .setContentText(msg);
        int notification_id = DEFAULT_NOTIFICATION_ID;
        if(msg.toLowerCase().contains("said:")){
        	notification_id = CHAT_NOTIFICATION_ID;
        }
        else if(msg.toLowerCase().contains("your turn")){
        	notification_id = TURN_NOTIFICATION_ID;
        }
        else if(msg.toLowerCase().contains("end") || msg.toLowerCase().contains("started a new game")){
        	notification_id = START_END_NOTIFICATION_ID;
        }
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(notification_id, mBuilder.build());
        
    }
}

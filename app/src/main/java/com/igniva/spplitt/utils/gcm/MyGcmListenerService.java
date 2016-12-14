package com.igniva.spplitt.utils.gcm;

/**
 * Created by igniva-andriod-05 on 19/5/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private int userid = 0;
    private String userId = "";
    private String bigImage="";
    private String notificationType = "";
    private String messsgeTitle = "";
    private String messageDescription = "";
    private String messageId = "";
    private int notificationCount = 0;
    private String messageTicker = "";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e("Payload noty is ",""+from+" "+data);

        messageId = data.getString("message_id");
        notificationType = data.getString("notification_type");
        notificationCount = Integer.parseInt(data.getString("notification_count"));
        userId = data.getString("user_id");
        messsgeTitle = data.getString("title");
        messageDescription = data.getString("message");
        messageTicker = data.getString("message_ticker");
        bigImage = data.getString("big_image");

/*
     if (userId.equals(PreferenceHandler.readString(this, PreferenceHandler.USER_ID, ""))) {
*/
            sendNotificationRequest();
//   }
    }


    private void sendNotificationRequest() {
        PreferenceHandler.writeInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 5);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
             notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messsgeTitle)
                    .setContentText(messageDescription)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        } else {
            // Lollipop specific setColor method goes here.
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_logo_notification)
                    .setContentTitle(messsgeTitle)
                    .setContentText(messageDescription)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        NotificationCompat.BigTextStyle style =
                new NotificationCompat.BigTextStyle(notificationBuilder);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCount, notificationBuilder.build());
    }

}

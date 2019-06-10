package com.gymkhana.studio.updater.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

public class NotificationUtils extends ContextWrapper {
	private boolean _autoCancel = true;
    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "ANDROID";
    public static final String IOS_CHANNEL_ID = "IOS";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";
    public NotificationUtils(Context base) {
        super(base);
        createChannels();
        String groupId = "group_id_101";
        CharSequence groupName = "Channel Name";
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));
    }
    public void createChannels() {
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID, ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
		androidChannel.enableLights(true);
		androidChannel.enableVibration(true);
		androidChannel.setLightColor(Color.GREEN);
		androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(androidChannel);
        NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID, IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        iosChannel.enableLights(true);
        iosChannel.enableVibration(true);
        iosChannel.setLightColor(Color.GRAY);
        iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(iosChannel);
    }
    public Notification.Builder getAndroidChannelNotification(String title, String body, int icon, PendingIntent resultPendingIntent) {
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(icon)
                .setAutoCancel(_autoCancel);
    }
    public Notification.Builder getIosChannelNotification(String title, String body, int icon, PendingIntent resultPendingIntent) {
        return new Notification.Builder(getApplicationContext(), IOS_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(icon)
                .setAutoCancel(_autoCancel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public void deleteNotificationChannel(String channelId) {
        NotificationManager mNotificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.deleteNotificationChannel(channelId);
    }
    public void setMsg(int type, int id, String title, String msg, int icon, Intent intent) {
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	if (type == 1) {
    		Notification.Builder nb = getAndroidChannelNotification(title, msg, icon, resultPendingIntent);
			getManager().notify(id, nb.build());
		} else if (type == 2) {
    		Notification.Builder nb = getIosChannelNotification(title, msg, icon, resultPendingIntent);
			getManager().notify(id, nb.build());
		} else {
			Notification.Builder nb = getAndroidChannelNotification(title, msg, icon, resultPendingIntent);
			getManager().notify(id, nb.build());
		}
    }
}

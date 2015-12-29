package com.govibs.viva.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.global.Global;


/**
 *
 * Created by Vibhor on 12/14/15.
 */
public class VivaNotificationListenerService extends NotificationListenerService {

    private static final String TAG = Global.TAG;
    private NLServiceReceiver mNlServiceReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mNlServiceReceiver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter(Global.ACTION_NOTIFICATION_SERVICE);
        registerReceiver(mNlServiceReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNlServiceReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i(TAG, "****** Notification Posted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent(Global.ACTION_NOTIFICATION_SERVICE);
        i.putExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT, sbn.getPackageName());
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG,"********** Notification Removed");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent(Global.ACTION_NOTIFICATION_SERVICE);
        i.putExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT_REMOVED, sbn.getPackageName());
        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra("command");
            if (!TextUtils.isEmpty(command)) {
                if (command.equals("clearall")) {
                    VivaNotificationListenerService.this.cancelAllNotifications();
                }
            }
        }
    }
}

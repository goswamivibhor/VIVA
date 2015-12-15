package com.govibs.viva.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.govibs.viva.global.Global;


/**
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
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent(Global.ACTION_NOTIFICATION_SERVICE);
        i.putExtra("notification_event", "onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Intent i = new  Intent(Global.ACTION_NOTIFICATION_SERVICE);
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                VivaNotificationListenerService.this.cancelAllNotifications();
            }
        }
    }
}

package com.govibs.viva.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.utilities.Utils;
import com.govibs.viva.voice.VivaVoiceManager;


public class VivaBroadcastReceiver extends BroadcastReceiver {
    public VivaBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED) ||
                intent.getAction().equalsIgnoreCase(Intent.ACTION_POWER_CONNECTED) ||
                intent.getAction().equalsIgnoreCase(Intent.ACTION_POWER_DISCONNECTED)) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = (level / (float)scale) * 100;
            Log.i(Global.TAG, "Battery Percentage: " + batteryPct);
            VivaLibraryPreferenceHelper.setBatteryStatus(context, isCharging,
                    usbCharge, acCharge, batteryPct);
        }
        /**
         * This segment receiver the SMS.
         * @author Vibhor
         */
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            try {
                Object[] rawMsgs=(Object[])intent.getExtras().get("pdus");
                for (Object raw : rawMsgs) {
                    SmsMessage msg = SmsMessage.createFromPdu((byte[])raw);
                    Log.i(Global.TAG, "Address: " + msg.getOriginatingAddress() + " Body: " + msg.getMessageBody());
                    // TODO Check in contact list if this address is present. Pass this on to the CPU.
                    /*Intent intentJarvisCPU = new Intent(context, JarvisCPU.class);
                    intentJarvisCPU.setAction(JarvisCPU.JARVIS_SMS_RECEIVED);
                    intentJarvisCPU.putExtra(JarvisCPU.JARVIS_SMS_RECEIVED, msg.getOriginatingAddress() + "|" + msg.getMessageBody());
                    context.startService(intentJarvisCPU);*/
                    VivaVoiceManager.getInstance().speak(context.getApplicationContext(), "I received a message.");
                    abortBroadcast();
                }
            }
            catch (Exception ex) {
                Log.e(Global.TAG, "Exception in reading sms. " + ex.getMessage());
            }
        }

        if (intent.getAction().equals(Global.ACTION_NOTIFICATION_SERVICE)) {
            if (intent.hasExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT)) {
                String event = intent.getStringExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT);
                Log.i(Global.TAG, "Notification event received: " + Utils.getApplicationName(context, event));
                VivaVoiceManager.getInstance().speak(context.getApplicationContext(), "Sir, I received a notification.");
            } else if (intent.hasExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT_REMOVED)) {
                String event = intent.getStringExtra(Global.ACTION_ITEM_NOTIFICATION_EVENT_REMOVED);
                Log.i(Global.TAG, "Notification event removed: " + Utils.getApplicationName(context, event));
                VivaVoiceManager.getInstance().speak(context.getApplicationContext(), "Sir, notification has been removed.");
            }
        }
    }
}

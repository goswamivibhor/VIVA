package com.govibs.viva.receiver;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.utilities.Utils;
import com.govibs.viva.voice.VivaVoiceManager;

/**
 * The phone state has changed.
 * Created by goswamiv on 12/30/15.
 */
public class PhoneStateChangeListener extends PhoneStateListener {

    private static Context mContext;

    public PhoneStateChangeListener(Context context) {
        mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(Global.TAG, "RINGING");
                VivaLibraryPreferenceHelper.setVivaCallInProgress(mContext, true);
                String contactName = Utils.getContactName(mContext, incomingNumber);
                if (!TextUtils.isEmpty(contactName)) {
                    String info = "Incoming call from " + contactName;
                    Log.i(Global.TAG, info);
                    VivaVoiceManager.getInstance().speak(mContext.getApplicationContext(), info);
                }
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(Global.TAG, "OFF-HOOK");
                if (!VivaLibraryPreferenceHelper.isVivaCallInProgress(mContext)
                        && VivaLibraryPreferenceHelper.isVivaCallRecordEnabled(mContext)) {
                    // Start call recording
                } else {
                    // Stop call recording
                }
                // this should be the last piece of code before the break
                VivaLibraryPreferenceHelper.setVivaCallInProgress(mContext, true);
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(Global.TAG, "IDLE");
                // this should be the last piece of code before the break
                VivaLibraryPreferenceHelper.setVivaCallInProgress(mContext, false);
                break;

        }
    }
}

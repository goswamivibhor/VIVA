package com.govibs.viva.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * The preference helper for storage.
 * Created by Vibhor on 12/8/15.
 */
public class VivaPreferenceHelper {

    private static final String FirstTimeLaunch = "FirstTimeLaunch";
    private static final String Setup = "VivaSetup";
    private static final String CallSign = "VivaCallSign";
    private static final String MasterName = "MasterName";
    private static final String NotificationListening = "NotificationListener";

    private VivaPreferenceHelper() {
    }

    /**
     * Default preferences
     * @param context - the calling application context.
     * @return Shared preferences
     */
    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setFirstTimeLaunch(Context context, boolean firstTimeLaunch) {
        getPreferences(context).edit().putBoolean(FirstTimeLaunch, firstTimeLaunch).apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        return getPreferences(context).getBoolean(FirstTimeLaunch, true);
    }

    public static void setSetupComplete(Context context, boolean status) {
        getPreferences(context).edit().putBoolean(Setup, status).apply();
    }

    public static boolean isSetupComplete(Context context) {
        return getPreferences(context).getBoolean(Setup, false);
    }

    public static void setCallSign(Context context, String callSign) {
        getPreferences(context).edit().putString(CallSign, callSign).apply();
    }

    public static String getCallSign(Context context) {
        return getPreferences(context).getString(CallSign, "Boss");
    }

    public static void setMasterName(Context context, String masterName) {
        getPreferences(context).edit().putString(MasterName, masterName).apply();
    }

    public static String getMasterName(Context context) {
        return getPreferences(context).getString(MasterName, getCallSign(context));
    }

    public static void setNotificationListeningEnabled(Context context, boolean status) {
        getPreferences(context).edit().putBoolean(NotificationListening, status).apply();
    }

    public static boolean isNotificationListeningEnabled(Context context) {
        return getPreferences(context).getBoolean(NotificationListening, false);
    }

}

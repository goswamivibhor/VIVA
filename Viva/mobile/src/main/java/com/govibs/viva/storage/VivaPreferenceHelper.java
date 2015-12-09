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

}

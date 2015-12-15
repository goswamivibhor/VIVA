package com.govibs.viva.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Iris Preference Helper
 * Created by vgoswami on 9/14/15.
 */
public class VivaLibraryPreferenceHelper {

    /**
     * Iris startup flag.
     */
    private static final String Iris_Startup = "IrisStartup";

    private static final String Iris_Weather = "IrisWeather";

    private static final String Iris_FirstTimeLaunch = "IrisFirstTimeLaunch";

    private static final String Iris_Setup = "IrisSetup";

    private static final String Iris_AI_Initialized = "IrisAIInitialized";

    private static final String Viva_BatterStatus = "BatteryStatus";

    private static final String Viva_Current_Location = "VivaCurrentLocation";

    private static final String VivaCurrentTemp = "VivaCurrentTemp";
    private static final String VivaCurrentWeatherState = "VivaCurrentWeatherState";
    private static final String VivaCurrentWindSpeed = "VivaCurrentWindSpeed";


    /**
     * Default Preference Helper
     * @param context - the calling application context.
     * @return
     */
    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setIrisStartup(Context context, boolean isIrisStarted) {
        getPreferences(context).edit().putBoolean(Iris_Startup, isIrisStarted).apply();
    }

    /**
     * Is Iris started.
     * @param context - the calling application context.
     * @return True if initialized, False otherwise.
     */
    public static boolean isIrisStarted(Context context) {
        return getPreferences(context).getBoolean(Iris_Startup, false);
    }

    /**
     * Save the weather information
     * @param context - the calling application context.
     * @param jarvis_Weather - the Weather information.
     */
    public static void saveIrisWeatherInfo(Context context, String jarvis_Weather) {
        getPreferences(context).edit().putString(Iris_Weather, jarvis_Weather).apply();
    }

    /**
     * Get the Iris Weather information.
     * @param context - the calling application context.
     * @return the Weather information
     */
    public static String getIrisWeatherInfo(Context context) {
        return getPreferences(context).getString(Iris_Weather, "");
    }

    /**
     * Set First time app launch
     * @param context - the calling application context.
     * @param isFirstTimeLaunch - the first time app launch
     */
    public static void setIsFirstTimeLaunch(Context context, boolean isFirstTimeLaunch) {
        getPreferences(context).edit().putBoolean(Iris_FirstTimeLaunch, isFirstTimeLaunch).apply();
    }

    /**
     * Is Iris Launched First Time
     * @param context - the calling application context.
     * @return True if launched first time.
     */
    public static boolean isFirstTimeLaunch(Context context) {
        return getPreferences(context).getBoolean(Iris_FirstTimeLaunch, true);
    }

    /**
     * Set the value if setup is complete
     * @param context - the calling application context.
     * @param setupComplete - the setup is complete.
     */
    public static void setIrisSetupComplete(Context context, boolean setupComplete) {
        getPreferences(context).edit().putBoolean(Iris_Setup, setupComplete).apply();
    }

    /**
     * Return the status Setup
     * @param context - the calling application context.
     * @return Status
     */
    public static boolean isIrisSetupComplete(Context context) {
        return getPreferences(context).getBoolean(Iris_Setup, false);
    }

    /**
     * Set Iris AI initialized
     * @param context - the calling application context.
     * @param init - the flag for the initialization status.
     */
    public static void setIrisAIInitialized(Context context, boolean init) {
        getPreferences(context).edit().putBoolean(Iris_AI_Initialized, init).apply();
    }

    /**
     * Returns the value if the AI has initialized.
     * @param context - the calling application context.
     * @return True if initialized, False otherwise.
     */
    public static boolean isIrisAIInitialized(Context context) {
        return getPreferences(context).getBoolean(Iris_AI_Initialized, false);
    }


    /**
     * Set the battery status
     * @param context - - the calling application context.
     * @param isCharging - is charging status
     * @param usbCharge - USB charge connected
     * @param acCharge - AC charge connected
     * @param percentage - percentage of the battery status
     */
    public static void setBatteryStatus(Context context, boolean isCharging, boolean usbCharge, boolean acCharge, float percentage) {
        try {
            JSONObject root = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("charging", isCharging);
            content.put("usbcharge", usbCharge);
            content.put("accharge" ,acCharge);
            content.put("percentage", (double) percentage);
            root.put("battery_status", content);
            getPreferences(context).edit().putString(Viva_BatterStatus, root.toString()).apply();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the battery percentage
     * @param context - the calling application context.
     * @return return the battery percentage.
     */
    public static float getBatteryPercentage(Context context) {
        float batteryPercentage = 0.0f;
        try {
            String batteryStatusJson = getPreferences(context).getString(Viva_BatterStatus, null);
            if (!TextUtils.isEmpty(batteryStatusJson)) {
                JSONObject root = new JSONObject(batteryStatusJson);
                JSONObject content = root.getJSONObject("battery_status");
                batteryPercentage = (float) content.getDouble("percentage");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return batteryPercentage;
    }

    public static void setVivaCurrentLocation(Context context, Location location) {
        try {
            String loc = location.getLatitude() + "," + location.getLongitude();
            getPreferences(context).edit().putString(Viva_Current_Location, loc).apply();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double[] getVivaCurrentLocation(Context context) {
        String loc = getPreferences(context).getString(Viva_Current_Location, "0,0");
        String[] locArray = loc.split(Pattern.quote(","));
        return new double[] { Double.parseDouble(locArray[0]), Double.parseDouble(locArray[1]) };
    }

    public static void setVivaCurrentTemp(Context context, float currentTemp) {
        getPreferences(context).edit().putFloat(VivaCurrentTemp, currentTemp).apply();
    }

    public static float getVivaCurrentTemp(Context context) {
        return getPreferences(context).getFloat(VivaCurrentTemp, 0.0f);
    }

    public static void setVivaCurrentWeatherState(Context context, String vivaCurrentWeatherState) {
        getPreferences(context).edit().putString(VivaCurrentWeatherState, vivaCurrentWeatherState).apply();
    }

    public static String getVivaCurrentWeatherState(Context context) {
        return getPreferences(context).getString(VivaCurrentWeatherState, "");
    }

    public static void setVivaCurrentWindSpeed(Context context, float vivaCurrentWindSpeed) {
        getPreferences(context).edit().putFloat(VivaCurrentWindSpeed, vivaCurrentWindSpeed).apply();
    }

    public static float getVivaCurrentSpeed(Context context) {
        return getPreferences(context).getFloat(VivaCurrentWindSpeed, 0.0f);
    }

}

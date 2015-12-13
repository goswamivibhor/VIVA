package com.govibs.viva;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.iriscorelibrary.handler.VivaManagerHandler;
import com.govibs.iriscorelibrary.storage.IrisPreferenceHelper;
import com.govibs.viva.storage.VivaPreferenceHelper;

/**
 * This is the handler class for managing communication with the library.
 * Created by Vibhor on 12/8/15.
 */
public class VivaHandler implements VivaManagerHandler {

    public static final String ACTION_INITIALIZE = "VivaInitialize";
    public static final String INIT_STATUS = "VivaInitStatus";

    private static VivaHandler mVivaHandler = new VivaHandler();

    private VivaHandler() {}

    public static VivaHandler getInstance() {
        return mVivaHandler;
    }

    @Override
    public void initialize(Activity activity, Context context) {
        VivaManager.getInstance().initialize(activity, context, this);
    }

    @Override
    public void onInitialized(Context context) {
        Intent intent = new Intent(ACTION_INITIALIZE);
        intent.putExtra(INIT_STATUS, true);
        context.sendBroadcast(intent);
    }

    @Override
    public void onInitializationFailed(Context context) {
        Intent intent = new Intent(ACTION_INITIALIZE);
        intent.putExtra(INIT_STATUS, false);
        context.sendBroadcast(intent);
    }

    @Override
    public void shutdown(Context context) {
        VivaManager.getInstance().shutdown(context);
    }

    @Override
    public void restart(Context context) {

    }

    @Override
    public void onVivaResponse(String s) {

    }

    @Override
    public void speak(Context context, String s) {
        VivaManager.getInstance().speak(context, s);
    }

    @Override
    public void sayToViva(Context context, String s) {
        VivaManager.getInstance().sayToViva(context, s);
    }

    @Override
    public float getBatteryPercentage(Context context) {
        return VivaManager.getInstance().getBatteryPercentage(context);
    }

    @Override
    public String getWeatherInformation(Context context) {
        String info = "Temperature: " + IrisPreferenceHelper.getVivaCurrentTemp(context) + " degree Celcius\n"
                + "Wind Speed: " + IrisPreferenceHelper.getVivaCurrentSpeed(context) + "Miles Per Hour\n"
                + "Other Info: " + IrisPreferenceHelper.getVivaCurrentWeatherState(context);
        if (!TextUtils.isEmpty(info)) {
            return info;
        } else {
            return context.getString(R.string.weather_info_default_display);
        }
    }

    /**
     * Weather information to speak.
     * @param context - the calling application context
     * @return Weather information.
     */
    public String getWeatherInformationToSpeak(Context context) {
        String info = VivaManager.getInstance().getWeatherInformation(context);
        if (!TextUtils.isEmpty(info)) {
            return info;
        } else {
            return VivaPreferenceHelper.getCallSign(context) +
                    context.getString(R.string.weather_info_default);
        }
    }
}

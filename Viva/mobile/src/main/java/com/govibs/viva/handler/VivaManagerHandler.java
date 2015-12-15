package com.govibs.viva.handler;

import android.app.Activity;
import android.content.Context;

/**
 * <p>
 *     Iris Manager Handler is the interface class for communicating with relative apps.
 * </p>
 *
 * Created by vgoswami on 9/14/15.
 */
public interface VivaManagerHandler {

    /**
     * Method for application to initialize the library
     * @param activity - the application activity
     * @param context - the calling application context.
     */
    void initialize(Activity activity, Context context);

    /**
     * Initialization is complete.
     * @param context - the calling application context.
     */
    void onInitialized(Context context);

    /**
     * Initialization failed
     * @param context - the calling application context.
     */
    void onInitializationFailed(Context context);

    /**
     * Shutdown manager with the application context.
     * @param context - the calling application context.
     */
    void shutdown(Context context);

    /**
     * Restart the manager with the application context
     * @param context - the calling application context.
     */
    void restart(Context context);

    /**
     * The response from VIVA.
     * @param context - the calling application context.
     * @param response - the response from VIVA
     */
    void onVivaResponse(Context context, String response);

    /**
     * Iris will say this.
     * @param context - the calling application context.
     * @param messageToSpeak - the message to be speak.
     */
    void speak(Context context, String messageToSpeak);

    /**
     * Say to Iris.
     * @param context - the calling application context.
     * @param messageToSay - the message to be spoken.
     */
    void sayToViva(Context context, String messageToSay);

    /**
     * Get the battery percentage
     * @param context - the calling application context.
     * @return Battery Percentage
     */
    float getBatteryPercentage(Context context);


    String getWeatherInformation(Context context);

}

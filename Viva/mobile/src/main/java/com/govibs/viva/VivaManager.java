package com.govibs.viva;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.ai.VivaAIManager;
import com.govibs.viva.ai.conversation.AIType;
import com.govibs.viva.ai.handler.OnAIResponse;
import com.govibs.viva.global.Global;
import com.govibs.viva.handler.VivaManagerHandler;
import com.govibs.viva.receiver.VivaBroadcastReceiver;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.voice.VivaVoiceManager;
import com.govibs.viva.weather.WeatherIntentService;


/**
 * <p>
 *     Iris Manager for handling the Core library. This class is the controller class for the library
 *     and will be used for all method calls.
 *     This is a singleton class
 * </p>
 * Created by Vibhor Goswami on 9/14/15.
 */
public class VivaManager implements OnAIResponse, LocationListener {


    /**
     * Iris Manager
     */
    private static VivaManager mIrisManager;

    private VivaManagerHandler mIrisManagerHandler;

    private Context mContext;

    private VivaBroadcastReceiver mVivaBroadcastReceiver = new VivaBroadcastReceiver();

    /**
     * Private constructor
     */
    private VivaManager() {
    }

    /**
     * Get the singleton instance of Iris Manager.
     * @return Singleton instance
     */
    public static VivaManager getInstance() {
        if (mIrisManager == null) {
            mIrisManager = new VivaManager();
        }
        return mIrisManager;
    }

    /**
     * Initialize the Manager components.
     * @param activity - the activity of the application
     * @param context - the calling application context.
     * @param irisManagerHandler - the handler
     */
    public void initialize(Activity activity, Context context, VivaManagerHandler irisManagerHandler) {
        mIrisManagerHandler = irisManagerHandler;
        mContext = context;
        VivaAIManager.getInstance().initialize(context, this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.getApplicationContext().registerReceiver(mVivaBroadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        context.getApplicationContext().registerReceiver(mVivaBroadcastReceiver, intentFilter);
        intentFilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        context.getApplicationContext().registerReceiver(mVivaBroadcastReceiver, intentFilter);
        intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        context.getApplicationContext().registerReceiver(mVivaBroadcastReceiver, intentFilter);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Global.ACTION_NOTIFICATION_SERVICE);
        context.getApplicationContext().registerReceiver(mVivaBroadcastReceiver, intentFilter);
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Global.TIME_LOCATION_UPDATE, 100, this);
            VivaLibraryPreferenceHelper.setVivaCurrentLocation(mContext, locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    public void onAIResponseReceived(AIType aiType, String response) {
        if (aiType != AIType.INITIALIZE) {
            // TODO Tell Iris to say the response.
            if (mIrisManagerHandler != null) {
                if (!TextUtils.isEmpty(response)) {
                    Log.i(Global.TAG, "AI Response: " + response);
                    mIrisManagerHandler.onVivaResponse(mContext, response);
                }
            }
        } else {
            if (mIrisManagerHandler != null) {
                if (!TextUtils.isEmpty(response)) {
                    mIrisManagerHandler.onInitialized(mContext);
                } else {
                    speak(mContext, "I need internet connection to be smart!");
                }
            }
        }
    }

    @Override
    public void onNLPResponseReceived(String response) {
        VivaAIManager.getInstance().speakToAI(mContext, response);
    }

    /**
     * Message that Iris has to say
     * @param context - the calling application context.
     * @param messageToSpeak - the message to speak.
     */
    public void speak(Context context, String messageToSpeak) {
        VivaVoiceManager.getInstance().speak(context, messageToSpeak);
    }

    /**
     * Message to be spoken to Iris
     * @param context - the calling application context.
     * @param messageToSay - the message to speak.
     */
    public void sayToViva(Context context, String messageToSay) {
        VivaAIManager.getInstance().analyzeText(messageToSay);
        VivaAIManager.getInstance().speakToAI(context, messageToSay);
    }

    /**
     * Shutdown all processes of VIVA
     * @param context - the calling application context.
     */
    public void shutdown(Context context) {
        context.getApplicationContext().unregisterReceiver(mVivaBroadcastReceiver);
    }

    public float getBatteryPercentage(Context context) {
        return VivaLibraryPreferenceHelper.getBatteryPercentage(context);
    }

    @Override
    public void onLocationChanged(Location location) {
        VivaLibraryPreferenceHelper.setVivaCurrentLocation(mContext, location);
        WeatherIntentService.startActionFetchCity(mContext, location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public String getWeatherInformation(Context context) {
        double[] location = VivaLibraryPreferenceHelper.getVivaCurrentLocation(context);
        WeatherIntentService.startActionFetchCity(mContext, location[0], location[1]);
        return VivaLibraryPreferenceHelper.getIrisWeatherInfo(context);
    }

    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event");
            Log.i(Global.TAG, temp);
        }
    }

    /**
     * Get Image details
     * @param bitmap - the bitmap to be analyzed.
     * @return Image details.
     */
    public String analyzeImageDetails(final Bitmap bitmap) {
        return VivaAIManager.getInstance().analyzeImageDetails(bitmap);
    }
}

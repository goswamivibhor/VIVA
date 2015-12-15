package com.govibs.viva.global;

import java.util.Locale;

/**
 * This is the global class for tags and other final static values.
 * Created by vgoswami on 9/14/15.
 */
public class Global {

    /**
     * Iris Core Library TAG
     */
    public static final String TAG = "VivaCoreLibrary";

    public static final String ACTION_NOTIFICATION_SERVICE = "com.govibs.iriscorelibrary.ACTION_NOTIFICATION_SERVICE";

    /**
     * Iris Keyword for waking the application or communicating.
     */
    public static final String VIVA = "Viva";

    /**
     * The VIVA locale
     */
    public static final Locale VIVA_LOCALE = Locale.getDefault();

    public static final int GPS_PERMISSION_REQUEST = 123;

    public static final  int VOICE_RECOGNITION_REQUEST_CODE = 2403;

    /**
     * 1 hour location update.
     */
    public static final long TIME_LOCATION_UPDATE = 60 * 60 * 1000;


}

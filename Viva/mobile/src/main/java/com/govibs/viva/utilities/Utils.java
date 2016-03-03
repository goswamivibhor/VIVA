package com.govibs.viva.utilities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.govibs.viva.BuildConfig;
import com.govibs.viva.R;
import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaPreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * The utility class for generic utility methods.
 * Created by Vibhor on 12/8/15.
 */
public class Utils {

    /**
     * Slide and disappear animation. Starts from the bottom to the top.
     * @param view - the view which will have this animation.
     * @param duration - the duration of the animation.
     * @param disappear - true if the view should disappear.
     */
    public static void slideToTopAndDisappear(final View view, int duration, final boolean disappear) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);
        slide.setDuration(duration);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (disappear) view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setVisibility(View.VISIBLE);
        view.startAnimation(slide);
    }

    /**
     * Slide to down and disappear
     * @param view - the view which will have this animation.
     * @param duration - the duration of the animation.
     * @param disappear - true if the view should disappear.
     */
    public void slideToDownAndDisappear(final View view, int duration, final boolean disappear) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);
        slide.setDuration(duration);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (disappear) view.setVisibility(View.GONE);
            }

        });
        view.setVisibility(View.VISIBLE);
        view.startAnimation(slide);
    }


    /**
     * Show dialog with a button
     * @param context - the calling application context
     * @param message - the message of the dialog
     * @param buttonText - the button text.
     * @param onClickListener - the on click listener.
     */
    public static void showDialogWithButton(Context context, String message, String buttonText, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, onClickListener);
        builder.create().show();
    }

    /**
     * Show dialog with a button
     * @param context - the calling application context.
     * @param message - the message of the dialog.
     * @param positiveButtonText - the text for positive button.
     * @param positiveButtonOnClickListener - the on click listener for positive button.
     * @param negativeButtonText - the text for negative button.
     * @param negativeButtonOnClickListener - the on click listener for negative button.
     */
    public static void showDialogWithButton(Context context, String message, String positiveButtonText,
                                            DialogInterface.OnClickListener positiveButtonOnClickListener,
                                            String negativeButtonText,
                                            DialogInterface.OnClickListener negativeButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveButtonOnClickListener);
        builder.setNegativeButton(negativeButtonText, negativeButtonOnClickListener);
        builder.create().show();
    }

    /**
     * Show dialog
     * @param context - the calling application context
     * @param message - the message of the dialog
     */
    public static void showDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.create().show();
    }

    /**
     * Get current date for display or for speaking.
     * @return Current date
     */
    public static String getCurrentDate(boolean forDisplay) {
        Calendar calendar = Calendar.getInstance(Global.VIVA_LOCALE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("MMM-dd-yyyy", Global.VIVA_LOCALE);
        SimpleDateFormat sdfSpeak = new SimpleDateFormat("MMM/dd/yyyy", Global.VIVA_LOCALE);
        if (forDisplay) {
            return sdfDisplay.format(calendar.getTime());
        } else {
            return sdfSpeak.format(calendar.getTime());
        }
    }

    public static String getCurrentTime(boolean forDisplay) {
        Calendar calendar = Calendar.getInstance(Global.VIVA_LOCALE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("HH:mm a", Global.VIVA_LOCALE);
        SimpleDateFormat sdfSpeak = new SimpleDateFormat("HH:mm", Global.VIVA_LOCALE);
        if (forDisplay) {
            return sdfDisplay.format(calendar.getTime());
        } else {
            return sdfSpeak.format(calendar.getTime());
        }
    }

    public static String convertCurrentTimeMillsToDisplayTime(long currentTimeMills) {
        Calendar calendar = Calendar.getInstance(Global.VIVA_LOCALE);
        calendar.setTimeInMillis(currentTimeMills);
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("MMM-dd-yyyy HH:mm a", Global.VIVA_LOCALE);
        return sdfDisplay.format(calendar.getTime());
    }

    public static long convertCurrentDisplayTimeToMills(String currentTime) {
        long currentTimeMills = System.currentTimeMillis();
        try {
            Calendar calendar = Calendar.getInstance(Global.VIVA_LOCALE);
            SimpleDateFormat sdfDisplay = new SimpleDateFormat("MMM-dd-yyyy HH:mm a", Global.VIVA_LOCALE);
            calendar.setTime(sdfDisplay.parse(currentTime));
            currentTimeMills = calendar.getTimeInMillis();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentTimeMills;
    }

    /**
     * Get the application name from package name.
     * @param context - the calling application context
     * @param packageName - the name of the package
     * @return Application Name
     */
    public static String getApplicationName(Context context, String packageName) {
        String appName = context.getString(R.string.unknown);
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                appName = packageManager.getApplicationLabel(applicationInfo).toString();
            } else {
                appName = context.getString(R.string.unknown);
            }
            //appName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return appName;
    }


    /***
     * Check if the Phone in Silent mode and determine if Viva has to annoy.
     * @param context - the calling application context
     * @return True if in Silent mode, False otherwise.
     */
    public static boolean isPhoneInSilent(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            switch (audioManager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    Log.i(Global.TAG, "Silent mode");
                    return true;
                case AudioManager.RINGER_MODE_VIBRATE:
                    Log.i(Global.TAG, "Vibrate mode");
                    return true;
                case AudioManager.RINGER_MODE_NORMAL:
                    Log.i(Global.TAG, "Normal mode");
                    return false;
                default:
                    return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Get the contact name from contact number.
     * @param context - the calling application context
     * @param phoneNumber - the phone number which needs to be check against contact.
     * @return the name of the contact from the phone number.
     */
    public static String getContactName(Context context, String phoneNumber) {
        Cursor cursor = null;
        String name = context.getString(R.string.unknown);
        try {

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return name;
            }
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return name;
    }

    /**
     * Get the color for battery based on the percentage.
     * @param context - the calling application context
     * @param percentage - the percentage of the battery.
     * @return Color resource.
     */
    public static int getColorForBattery(Context context, float percentage) {
        if (percentage <= 5) {
            return context.getResources().getColor(android.R.color.holo_red_dark);
        } else if (percentage > 5 && percentage <= 15) {
            return context.getResources().getColor(android.R.color.holo_orange_dark);
        } else {
            return context.getResources().getColor(android.R.color.holo_green_dark);
        }
    }

    /**
     * Get the message that needs to be spoken depending on the app name.
     * @param context - the calling application context
     * @param appName - the name of the app.
     * @param notificationText - the notification text.
     * @return Message to speak.
     */
    public static String getMessageToSpeakForAppName(Context context, String appName, String notificationText) {
        String msg = VivaPreferenceHelper.getCallSign(context) + context.getString(R.string.notification_received) + " " + appName;
        try {
            if (appName.equalsIgnoreCase(context.getString(R.string.notification_app_system))
                    || appName.equalsIgnoreCase(context.getString(R.string.notification_app_google_apps))) {
                if (!TextUtils.isEmpty(notificationText)) {
                    msg = VivaPreferenceHelper.getCallSign(context) + ", " + notificationText;
                }
            } else if (appName.equalsIgnoreCase(context.getString(R.string.notification_app_inbox))) {
                msg = VivaPreferenceHelper.getCallSign(context) + ", I have got an email.";
            } else if (appName.equalsIgnoreCase(context.getString(R.string.notification_app_talk))) {
                msg = VivaPreferenceHelper.getCallSign(context) + ", " + notificationText.split(Pattern.quote(":"))[0];
            } else if (appName.equalsIgnoreCase(context.getString(R.string.notification_app_whats_app))) {
                msg = VivaPreferenceHelper.getCallSign(context) + ", I have got a message on " + appName;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i(Global.TAG, msg);
        return msg;
    }

}

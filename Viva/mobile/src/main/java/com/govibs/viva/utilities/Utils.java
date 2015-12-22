package com.govibs.viva.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.govibs.viva.global.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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


    /**
     * Get the application name from package name.
     * @param context - the calling application context
     * @param packageName - the name of the package
     * @return Application Name
     */
    public static String getApplicationName(Context context, String packageName) {
        String appName = "Unknown";
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            appName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return appName;
    }


}

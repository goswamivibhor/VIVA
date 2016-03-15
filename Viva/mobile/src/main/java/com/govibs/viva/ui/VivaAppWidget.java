package com.govibs.viva.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import com.govibs.viva.R;
import com.govibs.viva.VivaManager;
import com.govibs.viva.WidgetTransparentActivity;
import com.govibs.viva.ai.VivaAIManager;
import com.govibs.viva.global.Global;

/**
 * Implementation of App Widget functionality.
 */
public class VivaAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.viva_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        //views.setOnClickPendingIntent(R.id.ibVivaVoiceRecognition, get);
        ComponentName vivaVoiceRecognition = new ComponentName(context, VivaAppWidget.class);
        views.setOnClickPendingIntent(R.id.ibVivaVoiceRecognition,
                getPendingSelfIntent(context, Global.ACTION_BROADCAST_WIDGET_VOICE_RECOGNITION));

        ComponentName vivaCaptureAndProcess = new ComponentName(context, VivaAppWidget.class);
        views.setOnClickPendingIntent(R.id.ibVivaAnalyzeImage,
                getPendingSelfIntent(context, Global.ACTION_BROADCAST_WIDGET_CAPTURE_AND_PROCESS));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(Global.ACTION_BROADCAST_WIDGET_VOICE_RECOGNITION)) {
            Log.i(Global.TAG, "Voice recognition button clicked.");
            Intent voiceRecognitionIntent = new Intent(context, WidgetTransparentActivity.class);
            voiceRecognitionIntent.setAction(Global.ACTION_BROADCAST_WIDGET_VOICE_RECOGNITION);
            voiceRecognitionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(voiceRecognitionIntent);
        } else if (intent.getAction().equalsIgnoreCase(Global.ACTION_BROADCAST_WIDGET_CAPTURE_AND_PROCESS)) {
            Log.i(Global.TAG, "Capture process start.");
            Intent cameraAnalyzeIntent = new Intent(context, WidgetTransparentActivity.class);
            cameraAnalyzeIntent.setAction(Global.ACTION_BROADCAST_WIDGET_CAPTURE_AND_PROCESS);
            cameraAnalyzeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(cameraAnalyzeIntent);
        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, VivaAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}


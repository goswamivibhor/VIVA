package com.govibs.viva;

import android.content.Context;
import android.content.Intent;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.iriscorelibrary.handler.VivaManagerHandler;

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
    public void initialize(Context context) {
        VivaManager.getInstance().initialize(context, this);
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
}

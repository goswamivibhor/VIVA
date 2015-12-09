package com.govibs.viva;

import android.content.Context;
import android.content.Intent;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.iriscorelibrary.handler.VivaManagerHandler;

/**
 * Created by Vibhor on 12/8/15.
 */
public class VivaHandler implements VivaManagerHandler {

    public static final String ACTION_INITIALIZE = "VivaInitialize";

    private static VivaHandler mVivaHandler = new VivaHandler();

    private VivaHandler() {}

    public static VivaHandler getInstance() {
        return mVivaHandler;
    }

    @Override
    public void onInitialized(Context context) {
        Intent intent = new Intent(ACTION_INITIALIZE);
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

    }

    @Override
    public void sayToViva(Context context, String s) {

    }
}

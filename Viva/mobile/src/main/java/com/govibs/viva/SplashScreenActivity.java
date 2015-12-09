package com.govibs.viva;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.iriscorelibrary.handler.VivaManagerHandler;
import com.govibs.viva.storage.VivaPreferenceHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private VivaInitializedBroadcast mVivaInitializedBroadcast = new VivaInitializedBroadcast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        VivaPreferenceHelper.setFirstTimeLaunch(this, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(VivaHandler.ACTION_INITIALIZE);
        registerReceiver(mVivaInitializedBroadcast, filter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                VivaManager.getInstance().initialize(SplashScreenActivity.this, VivaHandler.getInstance());
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mVivaInitializedBroadcast);
    }

    private class VivaInitializedBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startDashboardActivity = new Intent(context, DashboardActivity.class);
            startActivity(startDashboardActivity);
            SplashScreenActivity.this.finish();
        }
    }
}

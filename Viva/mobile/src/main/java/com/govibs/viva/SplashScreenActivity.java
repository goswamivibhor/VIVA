package com.govibs.viva;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.iriscorelibrary.handler.VivaManagerHandler;
import com.govibs.viva.storage.VivaPreferenceHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private VivaInitializedBroadcast mVivaInitializedBroadcast = new VivaInitializedBroadcast();
    boolean mInternetPermission = false, mGpsPermission = false;
    private static final int REQ_INTERNET_PERMISSION = 1;
    private TextView tvSplashScreenInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        VivaPreferenceHelper.setFirstTimeLaunch(this, true);
        VivaPreferenceHelper.setSetupComplete(this, false);
        tvSplashScreenInfo = (TextView) findViewById(R.id.tvSplashScreenInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(VivaHandler.ACTION_INITIALIZE);
        registerReceiver(mVivaInitializedBroadcast, filter);
        mInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        mGpsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (mInternetPermission && mGpsPermission) {
            tvSplashScreenInfo.setVisibility(View.VISIBLE);
            VivaHandler.getInstance().initialize(SplashScreenActivity.this, SplashScreenActivity.this);
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, REQ_INTERNET_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mVivaInitializedBroadcast);
    }

    private class VivaInitializedBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(VivaHandler.INIT_STATUS, true)) {
                startDashboarActivity(SplashScreenActivity.this, true);
            } else {
                startDashboarActivity(SplashScreenActivity.this, false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQ_INTERNET_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    tvSplashScreenInfo.setVisibility(View.VISIBLE);
                    VivaHandler.getInstance().initialize(SplashScreenActivity.this, SplashScreenActivity.this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    startDashboarActivity(SplashScreenActivity.this, false);
                }
                break;
        }
    }

    /**
     * Start Dashboard activity.
     * @param context - the calling application context
     * @param functionalityEnabled - the functionality is enabled or not.
     */
    private void startDashboarActivity(Context context, boolean functionalityEnabled) {
        Intent intentDashboardActivity = new Intent(context, DashboardActivity.class);
        intentDashboardActivity.putExtra("", functionalityEnabled);
        startActivity(intentDashboardActivity);
        SplashScreenActivity.this.finish();
    }

}

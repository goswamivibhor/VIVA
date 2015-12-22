package com.govibs.viva;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaPreferenceHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private VivaInitializedBroadcast mVivaInitializedBroadcast = new VivaInitializedBroadcast();
    boolean mInternetPermission = false, mGpsPermission = false, mSMSPermission = false;
    private static final int REQ_INTERNET_PERMISSION = 1;
    private TextView tvSplashScreenInfo, tvSplashScreenLetterV, tvSplashScreenDescVery,
            tvSplashScreenLetterI, tvSplashScreenDescIntelligent, tvSplashScreenLetterViv,
            tvSplashScreenDescVirtual, tvSplashScreenLetterViva, tvSplashScreenDescAssistant;
    RotateAnimation rotateV, rotateVi, rotateViv, rotateViva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        VivaPreferenceHelper.setFirstTimeLaunch(this, true);
        VivaPreferenceHelper.setSetupComplete(this, false);
        tvSplashScreenInfo = (TextView) findViewById(R.id.tvSplashScreenInfo);
        tvSplashScreenLetterV = (TextView) findViewById(R.id.tvSplashScreenLetterV);
        tvSplashScreenLetterI = (TextView) findViewById(R.id.tvSplashScreenLetterI);
        tvSplashScreenLetterI.setVisibility(View.GONE);
        tvSplashScreenLetterViv = (TextView) findViewById(R.id.tvSplashScreenLetterViv);
        tvSplashScreenLetterViv.setVisibility(View.GONE);
        tvSplashScreenLetterViva = (TextView) findViewById(R.id.tvSplashScreenLetterViva);
        tvSplashScreenLetterViva.setVisibility(View.GONE);
        tvSplashScreenDescVery = (TextView) findViewById(R.id.tvSplashScreenDescVery);
        tvSplashScreenDescVery.setVisibility(View.GONE);
        tvSplashScreenDescIntelligent = (TextView) findViewById(R.id.tvSplashScreenDescIntelligent);
        tvSplashScreenDescIntelligent.setVisibility(View.GONE);
        tvSplashScreenDescVirtual = (TextView) findViewById(R.id.tvSplashScreenDescVirtual);
        tvSplashScreenDescVirtual.setVisibility(View.GONE);
        tvSplashScreenDescAssistant = (TextView) findViewById(R.id.tvSplashScreenDescAssistant);
        tvSplashScreenDescAssistant.setVisibility(View.GONE);
        // V
        rotateV = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateV.setDuration(1000);
        rotateV.setAnimationListener(mAnimationListenerLetterV);
        // Vi
        rotateVi = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateVi.setDuration(1000);
        rotateVi.setAnimationListener(mAnimationListenerLetterVi);
        // Viv
        rotateViv = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateViv.setDuration(1000);
        rotateViv.setAnimationListener(mAnimationListenerLetterViv);
        // Viva
        rotateViva = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateViva.setDuration(1000);
        rotateViva.setAnimationListener(mAnimationListenerLetterViva);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(VivaHandler.ACTION_INITIALIZE);
        registerReceiver(mVivaInitializedBroadcast, filter);
        mInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        mGpsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        mSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        if (mInternetPermission && mGpsPermission && mSMSPermission) {
            tvSplashScreenLetterV.startAnimation(rotateV);
            tvSplashScreenInfo.setVisibility(View.VISIBLE);
            VivaHandler.getInstance().initialize(SplashScreenActivity.this, SplashScreenActivity.this);
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS },
                    REQ_INTERNET_PERMISSION);
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
                //startDashboarActivity(SplashScreenActivity.this, true);
            } else {
                //startDashboarActivity(SplashScreenActivity.this, false);
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
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

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
        intentDashboardActivity.putExtra(Global.VIVA_FUNCTIONALITY_ENABLED, functionalityEnabled);
        startActivity(intentDashboardActivity);
        SplashScreenActivity.this.finish();
    }

    private Animation.AnimationListener mAnimationListenerLetterV = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescVery.setVisibility(View.VISIBLE);
            tvSplashScreenLetterI.setVisibility(View.VISIBLE);
            tvSplashScreenLetterI.startAnimation(rotateVi);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterVi = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescIntelligent.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViv.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViv.startAnimation(rotateViv);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterViv = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescVirtual.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViva.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViva.startAnimation(rotateViva);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterViva = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescAssistant.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

}

package com.govibs.viva;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaPreferenceHelper;
import com.govibs.viva.ui.UIDisplayAdapter;
import com.govibs.viva.utilities.Utils;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mFloatingActionButton;
    private static final int REQUEST_SETUP = 1;
    private TextView tvInfoMessage, tvDashboardWeatherInfo, tvDashboardBatteryPercentage;
    private RelativeLayout rlConfiguredDashboard;
    private HoloCircularProgressBar holoCircularProgressBarBattery;
    private ListView lvDashboardResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);
        tvInfoMessage = (TextView) findViewById(R.id.tvInfoMessage);
        rlConfiguredDashboard = (RelativeLayout) findViewById(R.id.rlConfiguredDashboard);
        holoCircularProgressBarBattery = (HoloCircularProgressBar) findViewById(R.id.holoCircularProgressBar);
        holoCircularProgressBarBattery.setOnClickListener(this);
        tvDashboardWeatherInfo = (TextView) findViewById(R.id.tvDashboardWeatherInfo);
        lvDashboardResults = (ListView) findViewById(R.id.lvDashboardResults);
        tvDashboardBatteryPercentage = (TextView) findViewById(R.id.tvDashboardBatteryPercentage);
        ImageButton ibDashboardEmail = (ImageButton) findViewById(R.id.ibDashboardEmail);
        ibDashboardEmail.setOnClickListener(this);
        ImageButton ibDashboardSearch = (ImageButton) findViewById(R.id.ibDashboardWebSearch);
        ibDashboardSearch.setOnClickListener(this);
        ImageButton ibDashboardSettings = (ImageButton) findViewById(R.id.ibDashboardSettings);
        ibDashboardSettings.setOnClickListener(this);

        boolean functionalityEnabled = getIntent().getBooleanExtra(Global.VIVA_FUNCTIONALITY_ENABLED, true);
        if (!functionalityEnabled) {
            Log.i(Global.TAG, "Functionality is enabled.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VivaPreferenceHelper.isFirstTimeLaunch(DashboardActivity.this)) {
                    startSetupActivity(false);
                }
            }
        }, 2000);
        float batteryPercentage = VivaHandler.getInstance().getBatteryPercentage(DashboardActivity.this);
        if (batteryPercentage > 0) {
            holoCircularProgressBarBattery.setProgress(batteryPercentage);
            holoCircularProgressBarBattery.setMarkerProgress(100);
            holoCircularProgressBarBattery.setMarkerEnabled(true);
            holoCircularProgressBarBattery.setProgressColor(Utils.getColorForBattery(DashboardActivity.this, batteryPercentage));
            String battery = (int) batteryPercentage + "%";
            tvDashboardBatteryPercentage.setText(battery);
        }
        String info = VivaHandler.getInstance().getWeatherInformation(DashboardActivity.this);
        if (!info.equalsIgnoreCase(VivaPreferenceHelper.getCallSign(DashboardActivity.this) + getString(R.string.weather_info_default))) {
            tvDashboardWeatherInfo.setText(info);
        } else {
            tvDashboardWeatherInfo.setText(R.string.weather_info_default_display);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        VivaHandler.getInstance().shutdown(this);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                performSetup();
                break;
            case R.id.holoCircularProgressBar:
                speakBatteryStatus();
                break;
            case R.id.tvDashboardWeatherInfo:
                VivaHandler.getInstance().getWeatherInformationToSpeak(this);
                break;
            case R.id.ibDashboardEmail:
                break;
            case R.id.ibDashboardSettings:
                Utils.showDialogWithButton(DashboardActivity.this, getString(R.string.voice_over_option_setup),
                        "Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startSetupActivity(true);
                            }
                        },
                        "Nope",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startSetupActivity(false);
                            }
                        });
                break;
            case R.id.ibDashboardWebSearch:
                listenForRequest("Search for...");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SETUP:
                if (resultCode == RESULT_OK) {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.hey_there));
                    mFloatingActionButton.setTag("Start");
                    VivaPreferenceHelper.setFirstTimeLaunch(DashboardActivity.this, false);
                    tvInfoMessage.setVisibility(View.GONE);
                    rlConfiguredDashboard.setVisibility(View.VISIBLE);
                    VivaPreferenceHelper.setSetupComplete(DashboardActivity.this, true);
                    holoCircularProgressBarBattery.setProgress(
                            VivaHandler.getInstance().getBatteryPercentage(DashboardActivity.this));
                } else {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.failed_setup));
                    VivaPreferenceHelper.setSetupComplete(DashboardActivity.this, false);
                }
                break;
            case VivaHandler.VIVA_VOICE_RECOGNITION_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> arrTextResponses = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    UIDisplayAdapter arrayAdapter = new UIDisplayAdapter(DashboardActivity.this,
                            R.layout.layout_list_text_view, arrTextResponses);
                    lvDashboardResults.setAdapter(arrayAdapter);
                    lvDashboardResults.invalidate();
                    for (String res : arrTextResponses) {
                        Log.d(Global.TAG, res);
                    }
                    VivaHandler.getInstance().sayToViva(DashboardActivity.this, arrTextResponses.get(0));
                } else {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.unable_to_understand));
                }
                break;
        }
    }

    /**
     * This method checks if setup is required.
     */
    private void performSetup() {
        if (mFloatingActionButton.getTag().equals("Start") &&
                !VivaPreferenceHelper.isSetupComplete(DashboardActivity.this)) {
            mFloatingActionButton.setTag("Speak");
            Utils.showDialogWithButton(DashboardActivity.this, getString(R.string.voice_over_option_setup),
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSetupActivity(true);
                        }
                    },
                    "Nope",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSetupActivity(false);
                        }
                    });

        } else {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Speak", null).show();*/
            listenForRequest(getString(R.string.viva_listening));
        }
    }

    /**
     * Speak the battery status.
     */
    private void speakBatteryStatus() {
        String batteryStatus = VivaPreferenceHelper.getCallSign(DashboardActivity.this) + getString(R.string.battery_string_part_1)
                + " " + VivaHandler.getInstance().getBatteryPercentage(DashboardActivity.this) + " " + getString(R.string.battery_string_part_2);
        Log.i(Global.TAG, batteryStatus);
        VivaHandler.getInstance().speak(DashboardActivity.this, batteryStatus);
    }

    /**
     * Listen for request.
     * @param header - the dialog label for the search box.
     */
    private void listenForRequest(String header) {
        startActivityForResult(VivaHandler.getInstance().startListening(DashboardActivity.this, header),
                VivaHandler.VIVA_VOICE_RECOGNITION_REQUEST);
    }

    /**
     * Start setup activity.
     * @param flag - the flag for determining if voice over is required.
     */
    private void startSetupActivity(boolean flag) {
        Intent intent = new Intent(DashboardActivity.this, SetupActivity.class);
        intent.putExtra(SetupActivity.SETUP_VOICE_OVER, flag);
        startActivityForResult(intent, REQUEST_SETUP);
    }
}

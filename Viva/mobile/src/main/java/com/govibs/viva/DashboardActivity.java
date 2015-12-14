package com.govibs.viva;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.govibs.iriscorelibrary.global.Global;
import com.govibs.viva.storage.VivaPreferenceHelper;
import com.govibs.viva.utilities.Utils;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mFloatingActionButton;
    private static final int REQUEST_SETUP = 1;
    private TextView tvInfoMessage, tvDashboardWeatherInfo;
    private RelativeLayout rlConfiguredDashboard;
    private HoloCircularProgressBar holoCircularProgressBarBattery;
    private ArrayList<String> arrTextResponses = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VivaPreferenceHelper.isFirstTimeLaunch(DashboardActivity.this)) {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.hey_there));
                    mFloatingActionButton.setTag("Start");
                    VivaPreferenceHelper.setFirstTimeLaunch(DashboardActivity.this, false);
                }
                String info = VivaHandler.getInstance().getWeatherInformation(DashboardActivity.this);
                if (!info.equalsIgnoreCase(getString(R.string.weather_info_default))) {
                    tvDashboardWeatherInfo.setText(info);
                } else {
                    tvDashboardWeatherInfo.setText(R.string.weather_info_default_display);
                }
            }
        }, 2000);
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
                performSetup(v);
                break;
            case R.id.holoCircularProgressBar:
                speakBatteryStatus();
                break;
            case R.id.tvDashboardWeatherInfo:
                VivaHandler.getInstance().getWeatherInformationToSpeak(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SETUP:
                if (resultCode == RESULT_OK) {
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
                    arrTextResponses = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    arrayAdapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_list_item_1, arrTextResponses);
                    lvDashboardResults.setAdapter(arrayAdapter);
                    lvDashboardResults.invalidate();
                    for (String res : arrTextResponses) {
                        Log.d(Global.TAG, res);
                    }
                }
                break;
        }
    }

    private void performSetup(View view) {
        if (mFloatingActionButton.getTag().equals("Start")) {
            mFloatingActionButton.setTag("Speak");
            Utils.showDialogWithButton(DashboardActivity.this, getString(R.string.voice_over_option_setup),
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DashboardActivity.this, SetupActivity.class);
                            intent.putExtra(SetupActivity.SETUP_VOICE_OVER, true);
                            startActivityForResult(intent, REQUEST_SETUP);
                        }
                    },
                    "Nope",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DashboardActivity.this, SetupActivity.class);
                            intent.putExtra(SetupActivity.SETUP_VOICE_OVER, false);
                            startActivityForResult(intent, REQUEST_SETUP);
                        }
                    });

        } else {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Speak", null).show();*/
            startActivityForResult(VivaHandler.getInstance().startListening(DashboardActivity.this),
                    VivaHandler.VIVA_VOICE_RECOGNITION_REQUEST);
        }
    }

    /**
     * Speak the battery status.
     */
    private void speakBatteryStatus() {
        String batteryStatus = VivaPreferenceHelper.getCallSign(DashboardActivity.this) + getString(R.string.battery_string_part_1)
                + (int) holoCircularProgressBarBattery.getProgress() + getString(R.string.battery_string_part_2);
        Log.i(Global.TAG, batteryStatus);
        VivaHandler.getInstance().speak(DashboardActivity.this, batteryStatus);
    }
}

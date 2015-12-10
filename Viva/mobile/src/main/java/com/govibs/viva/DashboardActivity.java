package com.govibs.viva;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.govibs.viva.storage.VivaPreferenceHelper;
import com.govibs.viva.utilities.Utils;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mFloatingActionButton;
    private static final int REQUEST_SETUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VivaPreferenceHelper.isFirstTimeLaunch(DashboardActivity.this)) {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.dashboard_welcome_message));
                    mFloatingActionButton.setTag("Start");
                    VivaPreferenceHelper.setFirstTimeLaunch(DashboardActivity.this, false);
                }
            }
        }, 2000);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                performSetup(v);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SETUP:
                if (resultCode == RESULT_OK) {
                    VivaPreferenceHelper.setSetupComplete(DashboardActivity.this, true);
                } else {
                    VivaHandler.getInstance().speak(DashboardActivity.this, getString(R.string.failed_setup));
                    VivaPreferenceHelper.setSetupComplete(DashboardActivity.this, false);
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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Speak", null).show();
        }
    }
}

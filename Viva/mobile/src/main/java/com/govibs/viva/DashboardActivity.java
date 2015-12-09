package com.govibs.viva;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.govibs.iriscorelibrary.VivaManager;
import com.govibs.viva.storage.VivaPreferenceHelper;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Speak", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VivaPreferenceHelper.isFirstTimeLaunch(this)) {
            VivaManager.getInstance().sayToIris(this, getString(R.string.dashboard_welcome_message));
            VivaPreferenceHelper.setFirstTimeLaunch(this, false);
        }
    }
}

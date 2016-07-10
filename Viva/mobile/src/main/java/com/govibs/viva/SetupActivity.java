package com.govibs.viva;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.storage.VivaPreferenceHelper;

public class SetupActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    public static final String SETUP_VOICE_OVER = "SetupVoiceOver";

    private boolean isSetupVoiceOver;
    private EditText etSetupName;
    private RadioGroup radioGroupCallSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isSetupVoiceOver = getIntent().getBooleanExtra(SETUP_VOICE_OVER, true);
        etSetupName = (EditText) findViewById(R.id.etSetupName);
        etSetupName.setText(VivaPreferenceHelper.getMasterName(this));
        etSetupName.setOnFocusChangeListener(this);
        radioGroupCallSign = (RadioGroup) findViewById(R.id.rgrpGender);
        Switch switchSetupNotification = (Switch) findViewById(R.id.switchSetupNotification);
        switchSetupNotification.setChecked(VivaPreferenceHelper.isNotificationListeningEnabled(this));
        switchSetupNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            }
        });
        Switch switchSetupCallRecord = (Switch) findViewById(R.id.switchSetupCallRecord);
        switchSetupCallRecord.setChecked(VivaLibraryPreferenceHelper.isVivaCallRecordEnabled(this));
        switchSetupCallRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VivaLibraryPreferenceHelper.setVivaCallRecordEnabled(SetupActivity.this, isChecked);
            }
        });
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        int amStreamMusicMaxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int amStreamCurrentVol = VivaLibraryPreferenceHelper.getVivaVolume(this);
        SeekBar seekBarVolume = (SeekBar) findViewById(R.id.seekVivaVolume);
        final TextView textViewVolume = (TextView) findViewById(R.id.tvSetupVivaVolume);
        String vol = getString(R.string.viva_volume) + ": " + VivaLibraryPreferenceHelper.getVivaVolume(this);
        textViewVolume.setText(vol);
        seekBarVolume.setMax(amStreamMusicMaxVol);
        seekBarVolume.setProgress(amStreamCurrentVol);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    VivaLibraryPreferenceHelper.setVivaVolume(SetupActivity.this, progress);
                    String vol = getString(R.string.viva_volume) + ": " + progress;
                    textViewVolume.setText(vol);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (etSetupName.getEditableText() != null
                && etSetupName.getEditableText().length() > 0) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (etSetupName.getEditableText() != null
                        && etSetupName.getEditableText().length() > 0) {
                    setResult(RESULT_OK);
                }
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.etSetupName:
                if (hasFocus) {
                    if (isSetupVoiceOver) {
                        speak(R.string.question_name);
                    }
                }
                break;



        }
    }

    /**
     * Validate and move forward.
     */
    private void validate() {
        if (etSetupName.getEditableText() != null && etSetupName.getEditableText().length() > 0) {
            setRadioGroupCallSign();
            VivaPreferenceHelper.setCallSign(SetupActivity.this, getCallSign());
            VivaPreferenceHelper.setMasterName(SetupActivity.this, etSetupName.getEditableText().toString());
            setResult(RESULT_OK);
            finish();
        } else {
            if (isSetupVoiceOver) {
                speak(R.string.failed_setup);
            } else {
                etSetupName.setHint(R.string.failed_setup);
            }
        }
    }

    /**
     * Speak method
     * @param resource - the resource that needs to be spoken.
     */
    private void speak(int resource) {
        VivaHandler.getInstance().speak(SetupActivity.this, getString(resource));
    }

    private String getCallSign() {
        switch (radioGroupCallSign.getCheckedRadioButtonId()) {
            case R.id.radioBtnMale:
                return getString(R.string.call_sir);
            case R.id.radioBtnFemale:
                return getString(R.string.call_mam);
            case R.id.radioBtnDefault:
            default:
                return etSetupName.getEditableText().toString();
        }
    }

    private void setRadioGroupCallSign() {
        String callSign = VivaPreferenceHelper.getCallSign(SetupActivity.this);
        if (TextUtils.isEmpty(callSign)) {
            VivaPreferenceHelper.setCallSign(SetupActivity.this, getString(R.string.call_sir));
        } else if (callSign.equalsIgnoreCase(getString(R.string.call_sir))) {
            radioGroupCallSign.check(R.id.radioBtnMale);
        } else if (callSign.equalsIgnoreCase(getString(R.string.call_mam))) {
            radioGroupCallSign.check(R.id.radioBtnFemale);
        } else {
            radioGroupCallSign.check(R.id.radioBtnDefault);
        }
    }
}

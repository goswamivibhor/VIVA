package com.govibs.viva;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
        etSetupName.setOnFocusChangeListener(this);
        radioGroupCallSign = (RadioGroup) findViewById(R.id.rgrpGender);
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
            VivaPreferenceHelper.setCallSign(SetupActivity.this, getCallSign());
            setResult(RESULT_OK);
            finish();
        } else {
            if (isSetupVoiceOver) {
                speak(R.string.question_name);
            } else {
                etSetupName.setHint(R.string.question_name);
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
                return getString(R.string.answer_male);
            case R.id.radioBtnFemale:
                return getString(R.string.answer_female);
            case R.id.radioBtnDefault:
            default:
                return etSetupName.getEditableText().toString();
        }
    }
}

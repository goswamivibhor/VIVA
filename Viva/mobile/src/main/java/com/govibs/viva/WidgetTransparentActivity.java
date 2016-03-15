package com.govibs.viva;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.govibs.viva.global.Global;
import com.govibs.viva.utilities.Utils;

import java.io.InputStream;
import java.util.ArrayList;

public class WidgetTransparentActivity extends AppCompatActivity {


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_transparent);
        String action = getIntent().getAction();
        switch (action) {

            case Global.ACTION_BROADCAST_WIDGET_CAPTURE_AND_PROCESS:
                startCameraCaptureActivity();
                break;

            case Global.ACTION_BROADCAST_WIDGET_VOICE_RECOGNITION:
                listenForRequest(getString(R.string.app_name));
                break;

            default:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VivaHandler.VIVA_VOICE_RECOGNITION_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> arrTextResponses = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String res : arrTextResponses) {
                        Log.d(Global.TAG, res);
                    }
                    VivaHandler.getInstance().sayToViva(getApplicationContext(), arrTextResponses.get(0));
                } else {
                    VivaHandler.getInstance().speak(getApplicationContext(), getString(R.string.unable_to_understand));
                }
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                try {
                    Uri selectedImage = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    VivaManager.getInstance().analyzeImageDetails(bitmap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
        finish();
    }

    /**
     * Listen for request.
     * @param header - the dialog label for the search box.
     */
    private void listenForRequest(String header) {
        startActivityForResult(VivaHandler.getInstance().startListening(getApplicationContext(), header),
                VivaHandler.VIVA_VOICE_RECOGNITION_REQUEST);
    }

    /**
     * Start Camera Capture Activity
     */
    private void startCameraCaptureActivity() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = Utils.getOutputMediaFileUri(Global.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
}

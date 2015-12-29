package com.govibs.viva.voice.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.global.Global;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class VivaVoiceResponseService extends IntentService {

    private static final String ACTION_RESPOND = "com.vg.iriscorelibrary.voice.services.action.RESPOND";
    private static final String EXTRA_CONTENT = "com.vg.iriscorelibrary.voice.services.extra.CONTENT";


    private TextToSpeech mTextToSpeech;

    private static Context mContext;

    private static boolean isIrisSpeaking = false;

    /**
     * Starts this service to perform text to speech response with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionRespond(Context context, String messageToSay) {
        Intent intent = new Intent(context, VivaVoiceResponseService.class);
        intent.setAction(ACTION_RESPOND);
        intent.putExtra(EXTRA_CONTENT, messageToSay);
        mContext = context;
        mContext.startService(intent);
    }

    public VivaVoiceResponseService() {
        super("VivaVoiceResponseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RESPOND.equals(action)) {
                final String messageToSay = intent.getStringExtra(EXTRA_CONTENT);
                mTextToSpeech = new TextToSpeech(mContext.getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        handleActionRespond(messageToSay);
                    }
                });
                mTextToSpeech.setLanguage(Global.VIVA_LOCALE);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param messageToSay - the message to speak.
     */
    private void handleActionRespond(String messageToSay) {
        if (!TextUtils.isEmpty(messageToSay)) {
            try {
                mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {
                        Log.i(Global.TAG, "Speaking...");
                        isIrisSpeaking = true;
                    }

                    @Override
                    public void onDone(String s) {
                        if (s.equalsIgnoreCase("1")) {
                            isIrisSpeaking = false;
                            Log.i(Global.TAG, "Finished speaking.");
                            mTextToSpeech.shutdown();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        Log.e(Global.TAG, "Error while speaking.. " + s);
                        isIrisSpeaking = false;
                        mTextToSpeech.shutdown();
                    }
                });
                mTextToSpeech.speak(messageToSay, TextToSpeech.QUEUE_FLUSH, null, "1");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(Global.TAG, "Error completing speech..");
            }
        }
    }

}

package com.govibs.viva.ai;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.R;
import com.govibs.viva.ai.conversation.AIType;
import com.govibs.viva.ai.conversation.Conversation;
import com.govibs.viva.ai.handler.OnAIResponse;
import com.govibs.viva.ai.services.OnAIServiceCallback;
import com.govibs.viva.ai.services.VivaAIService;
import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.utilities.Utils;
import com.govibs.viva.voice.VivaVoiceManager;

/**
 * This is the VIVA AI manager which communicates with AI logic.
 * Created by vgoswami on 9/14/15.
 */
public class VivaAIManager implements OnAIServiceCallback {

    /**
     * Iris AI Manager static class
     */
    private static VivaAIManager mVivaAIManager;
    /**
     * Calling application context
     */
    private Context mContext;
    /**
     * On AI Response callback
     */
    private OnAIResponse mOnAIResponse;

    private AIType mAiType;

    /**
     * Iris AI manager constructor
     */
    private VivaAIManager() { }

    /**
     * This is the singleton class for Iris AI manager.
     * @return the singleton class for Iris AI.
     */
    public static VivaAIManager getInstance() {
        if (mVivaAIManager == null) mVivaAIManager = new VivaAIManager();
        return mVivaAIManager;
    }


    /**
     * Initialize the AI service
     * @param context - the calling application context.
     * @param onAIResponse - the AI response callback
     */
    public void initialize(Context context, OnAIResponse onAIResponse) {
        mContext = context;
        mOnAIResponse = onAIResponse;
        mAiType = AIType.INITIALIZE;
        VivaAIService.startActionFetchAIResponse(context, "Hi", this);
    }

    /**
     * Speak to AI
     * @param context - the calling application context.
     * @param messageToAI - the message to AI.
     */
    public void speakToAI(Context context, String messageToAI) {
        mContext = context;
        mAiType = AIType.DEFAULT;
        if (Conversation.isCommand(messageToAI)) {
            switch (Conversation.getCommandType(messageToAI)) {
                case BATTERY:
                    String battery = "Battery is at " + VivaLibraryPreferenceHelper.getBatteryPercentage(context) + " percent";
                    VivaVoiceManager.getInstance().speak(context, battery);
                    break;
                case CALL:
                    break;
                case TIME:
                    String time = "It is " + Utils.getCurrentTime(false);
                    VivaVoiceManager.getInstance().speak(context, time);
                    break;
                case WEATHER:
                case TEMPERATURE:
                    VivaVoiceManager.getInstance().speak(context, VivaLibraryPreferenceHelper.getIrisWeatherInfo(context));
                    break;
                case SMS:
                case TEXT:
                case MESSAGE:
                    VivaVoiceManager.getInstance().speak(context, "Who do you want to send a message to?");
                    break;
                case NAME:
                    VivaVoiceManager.getInstance().speak(context, "My name is VIVA.");
                    break;
                case DEFAULT:
                    VivaAIService.startActionFetchAIResponse(context, messageToAI, this);
                    break;
            }
        } else {
            if (!Conversation.matchAffirmativeResponse(messageToAI)) {
                VivaAIService.startActionFetchAIResponse(context, messageToAI, this);
            }
        }
    }

    @Override
    public void onAIResponseReceived(String messageFromAI) {
        if (mOnAIResponse != null) {
            Log.i(Global.TAG, "Response from AI: " + messageFromAI);
            mOnAIResponse.onAIResponseReceived(mAiType, messageFromAI);
        }
    }

    @Override
    public void onAIResponseFailed() {
        if (mOnAIResponse != null) {
            Log.e(Global.TAG, "No Response from AI.");
            mOnAIResponse.onAIResponseReceived(mAiType, "");
        }
    }

    public Intent getVoiceRecognitionIntent(Context context, String header) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        // Display an hint to the user about what he should say.
        String message = TextUtils.isEmpty(header) ? header : context.getString(R.string.app_name);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, message);

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        // Specify the recognition language. This parameter has to be specified only if the
        // recognition has to be done in a specific language and not the default one (i.e., the
        // system locale). Most of the applications do not have to set this parameter.
        /*if (!mSupportedLanguageView.getSelectedItem().toString().equals("Default")) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    mSupportedLanguageView.getSelectedItem().toString());
        }*/

        return intent;
    }
}

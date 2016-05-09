package com.govibs.viva.ai;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.R;
import com.govibs.viva.ai.conversation.AIType;
import com.govibs.viva.ai.conversation.Conversation;
import com.govibs.viva.ai.handler.OnAIResponse;
import com.govibs.viva.ai.nlp.api.AlchemyAPI;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_ImageParams;
import com.govibs.viva.ai.nlp.bean.AI_Response_Viva;
import com.govibs.viva.ai.services.OnAIServiceCallback;
import com.govibs.viva.ai.services.VivaAIService;
import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.utilities.Utils;
import com.govibs.viva.voice.VivaVoiceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;

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

    private AlchemyAPI mAlchemyAPI;

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
        AI_Response_Viva aiResponseViva = new AI_Response_Viva();
        aiResponseViva.setQuestion("Hi");
        aiResponseViva.setInit(true);
        VivaAIService.startActionFetchAIResponse(context, aiResponseViva, this);
        try {
            mAlchemyAPI = AlchemyAPI.GetInstanceFromString(context.getString(R.string.alchemy_api_key));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Speak to AI
     * @param context - the calling application context.
     * @param messageToAI - the message to AI.
     */
    public void speakToAI(Context context, String messageToAI) {
        mContext = context;
        mAiType = AIType.DEFAULT;
        final AI_Response_Viva aiResponseViva = new AI_Response_Viva();
        aiResponseViva.setQuestion(messageToAI);
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
                    VivaAIService.startActionFetchAIResponse(context, aiResponseViva, this);
                    break;
            }
        } else {
            if (!Conversation.matchAffirmativeResponse(messageToAI)) {
                VivaAIService.startActionFetchAIResponse(context, aiResponseViva, this);
            }
        }
    }

    @Override
    public void onAIResponse(AI_Response_Viva aiResponseViva) {
        if (mOnAIResponse != null) {
            Log.i(Global.TAG, "VIVA Response received.");

            mOnAIResponse.onAIResponseReceived(mAiType, aiResponseViva);
        }
    }

    @Override
    public void onAIResponseFailed() {
        if (mOnAIResponse != null) {
            Log.e(Global.TAG, "No Response from AI.");
            mOnAIResponse.onAIResponseReceived(mAiType, new AI_Response_Viva());
        }
    }

    @Override
    public AlchemyAPI getAlchemyAPI() {
        return mAlchemyAPI;
    }

    /**
     * Get Voice recognition intent
     * @param context - the calling application context.
     * @param header - the header text.
     * @return Intent for voice recognition.
     */
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

    /**
     * Get Image details.
     * @param bitmap - the bitmap to be analyzed.
     * @return Details of the image.
     */
    public String analyzeImageDetails(final Bitmap bitmap) {
        new AsyncTask<Bitmap, Void, String>() {
            final StringBuilder stringBuilder = new StringBuilder();

            @Override
            protected String doInBackground(Bitmap... params) {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageByteArray = stream.toByteArray();

                    AlchemyAPI_ImageParams imageParams = new AlchemyAPI_ImageParams();
                    imageParams.setImage(imageByteArray);
                    imageParams.setImagePostMode(AlchemyAPI_ImageParams.RAW);
                    Document doc = mAlchemyAPI.ImageGetRankedImageKeywords(imageParams);
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("text");
                    for (int i=0;i<items.getLength();i++) {
                        Node concept = items.item(i);
                        String astring = concept.getNodeValue();
                        astring = concept.getChildNodes().item(0).getNodeValue();
                        stringBuilder.append(astring);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return stringBuilder.toString();
            }
        }.execute(bitmap);
        return "";
    }

    /**
     * Analyze text
     * @param text - the text which needs to be analyzed.
     */
    public void analyzeText(final String text) {
        AI_Response_Viva ai_response_viva = new AI_Response_Viva();
        ai_response_viva.setQuestion(text);
        VivaAIService.startActionFetchNLPResponse(mContext, ai_response_viva, this);
    }

    private void processAIResponse(AI_Response_Viva aiResponseViva) {
        if (!TextUtils.isEmpty(aiResponseViva.getConcept())) {
            // Send a search for the concept.

        }
    }

}

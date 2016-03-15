package com.govibs.viva.ai.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.ai.components.ChatterBot;
import com.govibs.viva.ai.components.ChatterBotFactory;
import com.govibs.viva.ai.components.ChatterBotType;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_Params;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_TaxonomyParams;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_TextParams;
import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class VivaAIService extends IntentService {


    private static final String ACTION_FETCH_AI_RESPONSE = "com.vg.iriscorelibrary.ai.services.action.FETCH_AI_RESPONSE";
    private static final String ACTION_FETCH_NLP_RESPONSE = "com.vg.iriscorelibrary.ai.services.action.FETCH_NLP_RESPONSE";

    private static final String EXTRA_MESSAGE_TO_AI = "com.vg.iriscorelibrary.ai.services.extra.MESSAGE_TO_AI";
    //private static final String EXTRA_RESPONSE_BACK = "com.vg.iriscorelibrary.ai.services.extra.RESPONSE_BACK";

    private static OnAIServiceCallback mOnAIServiceCallback;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFetchAIResponse(Context context, String messageToAI, OnAIServiceCallback aiServiceCallback) {
        Intent intent = new Intent(context, VivaAIService.class);
        intent.setAction(ACTION_FETCH_AI_RESPONSE);
        intent.putExtra(EXTRA_MESSAGE_TO_AI, messageToAI);
        //intent.putExtra(EXTRA_RESPONSE_BACK, aiServiceCallback);
        mOnAIServiceCallback = aiServiceCallback;
        context.startService(intent);
    }

    public static void startActionFetchNLPResponse(Context context, String messageToAI, OnAIServiceCallback aiServiceCallback) {
        Intent intent = new Intent(context, VivaAIService.class);
        intent.setAction(ACTION_FETCH_NLP_RESPONSE);
        intent.putExtra(EXTRA_MESSAGE_TO_AI, messageToAI);
        mOnAIServiceCallback = aiServiceCallback;
        context.startService(intent);
    }


    public VivaAIService() {
        super("VivaAIService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_AI_RESPONSE.equals(action)) {
                final String messageToAI = intent.getStringExtra(EXTRA_MESSAGE_TO_AI);
                final OnAIServiceCallback onAIServiceCallback = mOnAIServiceCallback;
                        /*(OnAIServiceCallback) intent.getSerializableExtra(EXTRA_RESPONSE_BACK);*/
                handleFetchAIResponse(messageToAI, onAIServiceCallback);
            } else if (ACTION_FETCH_NLP_RESPONSE.equals(action)) {
                final  String messageToAI = intent.getStringExtra(EXTRA_MESSAGE_TO_AI);
                final OnAIServiceCallback onAIServiceCallback = mOnAIServiceCallback;
                handleFetchNLPResponse(messageToAI, onAIServiceCallback);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param messageToAI - the message to the AI chat server.
     * @param onAIServiceCallback - the AI Service callback.
     */
    private void handleFetchAIResponse(String messageToAI, OnAIServiceCallback onAIServiceCallback) {
        Log.i(Global.TAG, "Initializing AI..");
        try {
            ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
            ChatterBot chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
            Log.i(Global.TAG, "Message to AI: " + messageToAI);
            String response = chatterBot.createSession().think(messageToAI);
            if (onAIServiceCallback != null) {
                if (TextUtils.isEmpty(response)) {
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), false);
                    onAIServiceCallback.onAIResponseFailed();
                } else {
                    Log.i(Global.TAG, "Message from AI: " + messageToAI);
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), true);
                    onAIServiceCallback.onAIResponseReceived(response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            onAIServiceCallback.onAIResponseFailed();
        }
    }

    /**
     * Handle Fetch NLP Response
     * @param messageToAI - the message to the AI chat server.
     * @param onAIServiceCallback - the AI Service callback.
     */
    private void handleFetchNLPResponse(String messageToAI, OnAIServiceCallback onAIServiceCallback) {
        Log.i(Global.TAG, "NLP processing... " + messageToAI);
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            Document doc = onAIServiceCallback.getAlchemyAPI().TextGetTextSentiment(messageToAI);
            Element root = doc.getDocumentElement();
            String dataContent = root.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue() + "|";
            stringBuilder.append(dataContent);
            AlchemyAPI_TaxonomyParams taxonomyParams = new AlchemyAPI_TaxonomyParams();
            taxonomyParams.setOutputMode(AlchemyAPI_Params.OUTPUT_XML);
            taxonomyParams.setText(messageToAI);
            doc = onAIServiceCallback.getAlchemyAPI().TextGetTaxonomy(messageToAI, taxonomyParams);
            root = doc.getDocumentElement();
            dataContent = root.getElementsByTagName("label").item(0).getChildNodes().item(0).getNodeValue();
            stringBuilder.append(dataContent);

            /*doc = onAIServiceCallback.getAlchemyAPI().TextGetRankedNamedEntities(messageToAI);
            root = doc.getDocumentElement();*/


            mOnAIServiceCallback.onNLPResponse(stringBuilder.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            onAIServiceCallback.onAIResponseFailed();
        }
    }

}

package com.govibs.viva.ai.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.govibs.viva.ai.components.ChatterBot;
import com.govibs.viva.ai.components.ChatterBotFactory;
import com.govibs.viva.ai.components.ChatterBotType;
import com.govibs.viva.ai.nlp.api.AlchemyAPI;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_CombinedParams;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_ConceptParams;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_Params;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_TaxonomyParams;
import com.govibs.viva.ai.nlp.api.AlchemyAPI_TextParams;
import com.govibs.viva.ai.nlp.bean.AI_Response_Viva;
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
    public static void startActionFetchAIResponse(Context context, AI_Response_Viva aiResponseViva,
                                                  OnAIServiceCallback aiServiceCallback) {
        Intent intent = new Intent(context, VivaAIService.class);
        intent.setAction(ACTION_FETCH_AI_RESPONSE);
        intent.putExtra(EXTRA_MESSAGE_TO_AI, aiResponseViva);
        //intent.putExtra(EXTRA_RESPONSE_BACK, aiServiceCallback);
        mOnAIServiceCallback = aiServiceCallback;
        context.startService(intent);
    }

    public static void startActionFetchNLPResponse(Context context, AI_Response_Viva aiResponseViva, OnAIServiceCallback aiServiceCallback) {
        Intent intent = new Intent(context, VivaAIService.class);
        intent.setAction(ACTION_FETCH_NLP_RESPONSE);
        intent.putExtra(EXTRA_MESSAGE_TO_AI, aiResponseViva);
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
                final AI_Response_Viva messageToAI = (AI_Response_Viva) intent.getSerializableExtra(EXTRA_MESSAGE_TO_AI);
                final OnAIServiceCallback onAIServiceCallback = mOnAIServiceCallback;
                        /*(OnAIServiceCallback) intent.getSerializableExtra(EXTRA_RESPONSE_BACK);*/
                //handleFetchAIResponse(messageToAI, onAIServiceCallback);
                handleAIConversation(messageToAI, onAIServiceCallback);
            } else if (ACTION_FETCH_NLP_RESPONSE.equals(action)) {
                final AI_Response_Viva messageToAI = (AI_Response_Viva) intent.getSerializableExtra(EXTRA_MESSAGE_TO_AI);
                final OnAIServiceCallback onAIServiceCallback = mOnAIServiceCallback;
                handleFetchNLPResponse(messageToAI, onAIServiceCallback);
            }
        }
    }

    /**
     * Handle AI Conversation
     * @param aiResponseViva - the message to AI.
     * @param onAIServiceCallback - the interface for AI.
     */
    private void handleAIConversation(AI_Response_Viva aiResponseViva, OnAIServiceCallback onAIServiceCallback) {
        try {
            if (onAIServiceCallback == null) {
                return;
            }
            try {
                ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
                ChatterBot chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
                Log.i(Global.TAG, "Message to AI: " + aiResponseViva.getQuestion());
                String response = chatterBot.createSession().think(aiResponseViva.getQuestion());
                if (TextUtils.isEmpty(response)) {
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), false);
                } else {
                    Log.i(Global.TAG, "Message from AI: " + response);
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), true);
                    aiResponseViva.setChatResponse(response);
                    if (aiResponseViva.isInit()) {
                        onAIServiceCallback.onAIResponse(aiResponseViva);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Document doc = onAIServiceCallback.getAlchemyAPI().TextGetTextSentiment(aiResponseViva.getQuestion());
            Element root = doc.getDocumentElement();
            String dataContent = "";
            try {
                dataContent = root.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setSentiment(dataContent);
                Log.i(Global.TAG, "Sentiment: " + dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            AlchemyAPI_TaxonomyParams taxonomyParams = new AlchemyAPI_TaxonomyParams();
            taxonomyParams.setOutputMode(AlchemyAPI_Params.OUTPUT_XML);
            taxonomyParams.setText(aiResponseViva.getQuestion());
            doc = onAIServiceCallback.getAlchemyAPI().TextGetTaxonomy(aiResponseViva.getQuestion(), taxonomyParams);
            root = doc.getDocumentElement();
            try {
                dataContent = root.getElementsByTagName("language").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setLanguage(dataContent);
                Log.i(Global.TAG, "Language: " + dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                dataContent = root.getElementsByTagName("label").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setCategory(dataContent);
                Log.i(Global.TAG, "Label: " + dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                dataContent = root.getElementsByTagName("taxonomy").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setTaxonomy(dataContent);
                Log.i(Global.TAG, "Taxonomy: " + dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                doc = onAIServiceCallback.getAlchemyAPI().TextGetRankedConcepts(aiResponseViva.getQuestion());
                root = doc.getDocumentElement();
                dataContent = root.getElementsByTagName("text").item(0).getChildNodes().item(0).getNodeValue();
                Log.i(Global.TAG, "Concept: " + dataContent);
                aiResponseViva.setConcept(dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*doc = onAIServiceCallback.getAlchemyAPI().TextGetRankedNamedEntities(messageToAI);
            root = doc.getDocumentElement();*/
            mOnAIServiceCallback.onAIResponse(aiResponseViva);
        } catch (Exception ex) {
            ex.printStackTrace();
            onAIServiceCallback.onAIResponseFailed();
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param aiResponseViva - the message to the AI chat server.
     * @param onAIServiceCallback - the AI Service callback.
     */
    private void handleFetchAIResponse(AI_Response_Viva aiResponseViva, OnAIServiceCallback onAIServiceCallback) {
        Log.i(Global.TAG, "Initializing AI..");
        try {
            ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
            ChatterBot chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
            Log.i(Global.TAG, "Message to AI: " + aiResponseViva.getQuestion());
            String response = chatterBot.createSession().think(aiResponseViva.getQuestion());
            if (onAIServiceCallback != null) {
                if (TextUtils.isEmpty(response)) {
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), false);
                    onAIServiceCallback.onAIResponseFailed();
                } else {
                    Log.i(Global.TAG, "Message from AI: " + aiResponseViva.getQuestion());
                    VivaLibraryPreferenceHelper.setIrisAIInitialized(getApplicationContext(), true);
                    aiResponseViva.setChatResponse(response);
                    onAIServiceCallback.onAIResponse(aiResponseViva);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            onAIServiceCallback.onAIResponseFailed();
        }
    }

    /**
     * Handle Fetch NLP Response
     * @param aiResponseViva - the message to the AI chat server.
     * @param onAIServiceCallback - the AI Service callback.
     */
    private void handleFetchNLPResponse(AI_Response_Viva aiResponseViva, OnAIServiceCallback onAIServiceCallback) {
        Log.i(Global.TAG, "NLP processing... " + aiResponseViva.getQuestion());
        try {
            Document doc = onAIServiceCallback.getAlchemyAPI().TextGetTextSentiment(aiResponseViva.getQuestion());
            Element root = doc.getDocumentElement();
            String dataContent = "";
            try {
                dataContent = root.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setSentiment(dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            AlchemyAPI_TaxonomyParams taxonomyParams = new AlchemyAPI_TaxonomyParams();
            taxonomyParams.setOutputMode(AlchemyAPI_Params.OUTPUT_XML);
            taxonomyParams.setText(aiResponseViva.getQuestion());
            doc = onAIServiceCallback.getAlchemyAPI().TextGetTaxonomy(aiResponseViva.getQuestion(), taxonomyParams);
            root = doc.getDocumentElement();
            try {
                dataContent = root.getElementsByTagName("language").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setLanguage(dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                dataContent = root.getElementsByTagName("label").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setCategory(dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                dataContent = root.getElementsByTagName("taxonomy").item(0).getChildNodes().item(0).getNodeValue();
                aiResponseViva.setTaxonomy(dataContent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*doc = onAIServiceCallback.getAlchemyAPI().TextGetRankedNamedEntities(messageToAI);
            root = doc.getDocumentElement();*/
            mOnAIServiceCallback.onAIResponse(aiResponseViva);
        } catch (Exception ex) {
            ex.printStackTrace();
            onAIServiceCallback.onAIResponseFailed();
        }
    }

}

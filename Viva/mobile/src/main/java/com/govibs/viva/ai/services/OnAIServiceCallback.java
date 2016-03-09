package com.govibs.viva.ai.services;

import com.govibs.viva.ai.nlp.api.AlchemyAPI;

/**
 * Interface class for returning the AI service response.
 */
public interface OnAIServiceCallback {

    /**
     * AI Response Received
     * @param messageFromAI - the message from AI server.
     */
    void onAIResponseReceived(String messageFromAI);

    /**
     * AI Response failed.
     */
    void onAIResponseFailed();

    /**
     * Get the Alchemy API from manager.
     * @return AlchemyAPI
     */
    AlchemyAPI getAlchemyAPI();

    /**
     * NLP response from server.
     * @param nlpResponse - the NLP response.
     */
    void onNLPResponse(String nlpResponse);

}

package com.govibs.viva.ai.services;

import com.govibs.viva.ai.nlp.api.AlchemyAPI;
import com.govibs.viva.ai.nlp.bean.AI_Response_Viva;

/**
 * Interface class for returning the AI service response.
 */
public interface OnAIServiceCallback {

    /**
     * AI Response received.
     * @param aiResponseViva - the response received from AI.
     */
    void onAIResponse(AI_Response_Viva aiResponseViva);
    /**
     * AI Response failed.
     */
    void onAIResponseFailed();

    /**
     * Get the Alchemy API from manager.
     * @return AlchemyAPI
     */
    AlchemyAPI getAlchemyAPI();

}

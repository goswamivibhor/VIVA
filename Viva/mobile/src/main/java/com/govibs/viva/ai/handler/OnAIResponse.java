package com.govibs.viva.ai.handler;

import com.govibs.viva.ai.conversation.AIType;

/**
 * <p>
 *     This interface is the response from AI manager which can be implemented by the calling class
 *     for listening to responses from the AI.
 * </p>
 * Created by vgoswami on 9/15/15.
 */
public interface OnAIResponse {

    /**
     * Response received from the AI server.
     * @param aiType - the type of AI request/response
     * @param response - the response from AI server.
     */
    void onAIResponseReceived(AIType aiType, String response);

    /**
     * Response received from the NLP API.
     * @param response - the response from AI server.
     */
    void onNLPResponseReceived(String response);

}

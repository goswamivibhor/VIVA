package com.govibs.viva.ai.services;

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

}

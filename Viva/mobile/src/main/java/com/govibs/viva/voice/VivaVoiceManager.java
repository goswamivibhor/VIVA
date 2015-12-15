package com.govibs.viva.voice;

import android.content.Context;

import com.govibs.viva.voice.services.VivaVoiceResponseService;


/**
 * This is the voice manager handling the service call for speech.
 * Created by goswamiv on 12/9/15.
 */
public class VivaVoiceManager {
    private static VivaVoiceManager ourInstance = new VivaVoiceManager();

    public static VivaVoiceManager getInstance() {
        return ourInstance;
    }

    private VivaVoiceManager() {
    }

    public void speak(Context context, String messageToSpeak) {
        VivaVoiceResponseService.startActionRespond(context, messageToSpeak);
    }
}

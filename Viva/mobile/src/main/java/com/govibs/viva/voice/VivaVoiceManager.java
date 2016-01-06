package com.govibs.viva.voice;

import android.content.Context;
import android.util.Log;

import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;
import com.govibs.viva.storage.VivaPreferenceHelper;
import com.govibs.viva.utilities.Utils;
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
        if (!Utils.isPhoneInSilent(context)) {
            if (!VivaLibraryPreferenceHelper.isVivaLastSentenceSame(context, messageToSpeak)) {
                Log.i(Global.TAG, "Viva says: " + messageToSpeak);
                VivaLibraryPreferenceHelper.setVivaLastSentence(context, messageToSpeak);
                VivaVoiceResponseService.startActionRespond(context, messageToSpeak);
            }
        }
    }
}

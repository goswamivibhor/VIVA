package com.govibs.viva.ai.conversation;

import android.util.Log;

import com.govibs.viva.ai.nlp.api.AlchemyAPI;
import com.govibs.viva.global.Global;

import org.w3c.dom.Document;

import java.util.Locale;

/**
 * Conversation for AI.
 * Created by vgoswami on 1/12/15.
 */
public class Conversation {

    /**
     * Matches the affirmative response for Jarvis to undestand.
     * @param matchResult - the result heard by Jarvis.
     * @return True if the response is matched or else will be False.
     * @author Vibhor
     */
    public static boolean matchAffirmativeResponse(String matchResult) {
        boolean status = false;
        try {
            matchResult = matchResult.toLowerCase(Global.VIVA_LOCALE);
            if (matchResult.toLowerCase().contains("yes") || matchResult.toLowerCase().contains("ofcourse") || matchResult.toLowerCase().contains("sure")
                    || matchResult.toLowerCase().contains("please") || matchResult.toLowerCase().contains("ya") || matchResult.toLowerCase().contains("yep")
                    || matchResult.toLowerCase().contains("ok") || matchResult.toLowerCase().contains("ye") || matchResult.toLowerCase().contains("yeh")) {
                status = true;
            }
            else {
                status = false;
            }
        }
        catch (Exception ex) {
            Log.e(Global.TAG, "Exception in matching affirmative response. " + ex.getMessage());
        }
        return status;
    }

    /**
     * Returns the type of Command which Jarvis has understood.
     * @param command - the command to respond to.
     * @return CommandType
     */
    public static CommandType getCommandType(String command) {
        CommandType commandType = CommandType.DEFAULT;
        if (command.contains(CommandType.BATTERY.toString())) {
            return CommandType.BATTERY;
        } else if (command.contains(CommandType.CALL.toString())) {
            return CommandType.CALL;
        } else if (command.contains(CommandType.WEATHER.toString())) {
            return CommandType.WEATHER;
        } else if (command.contains(CommandType.TEMPERATURE.toString())) {
            return CommandType.TEMPERATURE;
        } else if (command.contains(CommandType.TIME.toString())) {
            return CommandType.TIME;
        } else if (command.contains(CommandType.SEARCH.toString())) {
            return CommandType.SEARCH;
        } else if (command.contains(CommandType.MESSAGE.toString())) {
            return CommandType.MESSAGE;
        } else if (command.contains(CommandType.SMS.toString())) {
            return CommandType.SMS;
        } else if (command.contains(CommandType.TEXT.toString())) {
            return CommandType.TEXT;
        } else if (command.contains(CommandType.NAME.toString())) {
            return CommandType.NAME;
        }
        return commandType;
    }

    /**
     * Determine if Jarvis has been given a command.
     * @param message - determine if its a command
     * @return True if its a command, else False.
     */
    public static boolean isCommand(String message) {
        boolean status = false;
        message = message.toLowerCase(Global.VIVA_LOCALE);

        if (message.toLowerCase().contains(CommandType.BATTERY.toString())
                || message.toLowerCase().contains(CommandType.CALL.toString())
                || message.toLowerCase().contains(CommandType.WEATHER.toString())
                || message.toLowerCase().contains(CommandType.TEMPERATURE.toString())
                || message.toLowerCase().contains(CommandType.TIME.toString())
                || message.toLowerCase().contains(CommandType.SEARCH.toString())
                || message.toLowerCase().contains(CommandType.TEXT.toString())
                || message.toLowerCase().contains(CommandType.MESSAGE.toString())
                || message.toLowerCase().contains(CommandType.SMS.toString())
                || message.toLowerCase().contains("what is your name")
                || message.toLowerCase().contains("your name")) {
            status = true;
        }
        return status;
    }

}

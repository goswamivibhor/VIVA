package com.govibs.viva.ai.components;

import com.govibs.viva.utilities.AIUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/*
    chatter-bot-api
    Copyright (C) 2011 pierredavidbelanger@gmail.com
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
class Cleverbot implements ChatterBot {
    private final String baseUrl;
    private final String serviceUrl;
    private int endIndex;

    public Cleverbot(String baseUrl, String serviceUrl, int endIndex) {
        this.baseUrl = baseUrl;
        this.serviceUrl = serviceUrl;
        this.endIndex = endIndex;
    }

    public ChatterBotSession createSession() {
        return new Session();
    }

    private class Session implements ChatterBotSession {
        private final Map<String, String> vars;
        private final Map<String, String> cookies;

        public Session() {
            vars = new LinkedHashMap<String, String>();
            vars.put("start", "y");
            vars.put("icognoid", "wsf");
            vars.put("fno", "0");
            vars.put("sub", "Say");
            vars.put("islearning", "1");
            vars.put("cleanslate", "false");
            cookies = new LinkedHashMap<String, String>();
            try {
                AIUtils.request(baseUrl, cookies, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        public ChatterBotThought think(ChatterBotThought thought) throws Exception {
            vars.put("stimulus", thought.getText());

            String formData = AIUtils.parametersToWWWFormURLEncoded(vars);
            String formDataToDigest = formData.substring(9, endIndex);
            String formDataDigest = AIUtils.md5(formDataToDigest);
            vars.put("icognocheck", formDataDigest);

            String response = AIUtils.request(serviceUrl, cookies, vars);
            
            String[] responseValues = response.split("\r");
            
            //vars.put("", AIUtils.stringAtIndex(responseValues, 0)); ??
            vars.put("sessionid", AIUtils.stringAtIndex(responseValues, 1));
            vars.put("logurl", AIUtils.stringAtIndex(responseValues, 2));
            vars.put("vText8", AIUtils.stringAtIndex(responseValues, 3));
            vars.put("vText7", AIUtils.stringAtIndex(responseValues, 4));
            vars.put("vText6", AIUtils.stringAtIndex(responseValues, 5));
            vars.put("vText5", AIUtils.stringAtIndex(responseValues, 6));
            vars.put("vText4", AIUtils.stringAtIndex(responseValues, 7));
            vars.put("vText3", AIUtils.stringAtIndex(responseValues, 8));
            vars.put("vText2", AIUtils.stringAtIndex(responseValues, 9));
            vars.put("prevref", AIUtils.stringAtIndex(responseValues, 10));
            //vars.put("", AIUtils.stringAtIndex(responseValues, 11)); ??
            vars.put("emotionalhistory", AIUtils.stringAtIndex(responseValues, 12));
            vars.put("ttsLocMP3", AIUtils.stringAtIndex(responseValues, 13));
            vars.put("ttsLocTXT", AIUtils.stringAtIndex(responseValues, 14));
            vars.put("ttsLocTXT3", AIUtils.stringAtIndex(responseValues, 15));
            vars.put("ttsText", AIUtils.stringAtIndex(responseValues, 16));
            vars.put("lineRef", AIUtils.stringAtIndex(responseValues, 17));
            vars.put("lineURL", AIUtils.stringAtIndex(responseValues, 18));
            vars.put("linePOST", AIUtils.stringAtIndex(responseValues, 19));
            vars.put("lineChoices", AIUtils.stringAtIndex(responseValues, 20));
            vars.put("lineChoicesAbbrev", AIUtils.stringAtIndex(responseValues, 21));
            vars.put("typingData", AIUtils.stringAtIndex(responseValues, 22));
            vars.put("divert", AIUtils.stringAtIndex(responseValues, 23));
            
            ChatterBotThought responseThought = new ChatterBotThought();

            responseThought.setText(AIUtils.stringAtIndex(responseValues, 16));
            
            return responseThought;
        }

        public String think(String text) throws Exception {
            ChatterBotThought thought = new ChatterBotThought();
            thought.setText(text);
            return think(thought).getText();
        }
    }
}
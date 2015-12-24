package com.govibs.viva.ai.conversation;

/**
 * Command type for AI.
 * Created by vgoswami on 1/16/15.
 */
public enum CommandType {

    BATTERY {
        @Override
        public String toString() {
            return "battery";
        }
    },

    WEATHER {
        @Override
        public String toString() {
            return "weather";
        }
    },

    TIME {
        @Override
        public String toString() {
            return "time";
        }
    },

    CALL {
        @Override
        public String toString() {
            return "call";
        }
    },

    SEARCH {
        @Override
        public String toString() {
            return "search";
        }
    },

    DEFAULT

}

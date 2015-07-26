package com.concept2.api;

import android.app.Activity;

/**
 * Status codes reported by the Concept2 APIs.
 */
public class Concept2StatusCodes {

    public static final int OK = Activity.RESULT_OK;
    public static final int INTERNAL_ERROR = 1;
    public static final int TIMEOUT = 2;
    public static final int PACE_MONITOR_NOT_FOUND = 3;
    public static final int CANCELED = 4;
    public static final int PACE_MONITOR_COMMUNICATION_ERROR = 5;
    public static final int PACE_MONITOR_DATA_ERROR = 6;
    public static final int COMMAND_NOT_FOUND = 7;

    // Not instantiable
    private Concept2StatusCodes() {}

    public static String toString(int statusCode) {
        switch (statusCode) {
            case OK: return "OK";
            case INTERNAL_ERROR: return "INTERNAL_ERROR";
            case TIMEOUT: return "TIMEOUT";
            case PACE_MONITOR_NOT_FOUND: return "PACE_MONITOR_NOT_FOUND";
            case CANCELED: return "CANCELED";
            case PACE_MONITOR_COMMUNICATION_ERROR: return "PACE_MONITOR_COMMUNICATION_ERROR";
            case PACE_MONITOR_DATA_ERROR: return "PACE_MONITOR_DATA_ERROR";
            case COMMAND_NOT_FOUND: return "COMMAND_NOT_FOUND";
            default:
                throw new IllegalArgumentException("Status code " + statusCode + " not recognized");
        }
    }
}

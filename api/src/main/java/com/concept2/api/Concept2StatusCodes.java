package com.concept2.api;

import android.app.Activity;

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
            default:
                throw new IllegalArgumentException("Status code " + statusCode + " not recognized");
        }
    }
}

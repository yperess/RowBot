package com.concept2.api;

import android.app.Activity;

public interface Concept2StatusCodes {
    int OK = Activity.RESULT_OK;

    int INTERNAL_ERROR = 1;
    int TIMEOUT = 2;
    int PACE_MONITOR_NOT_FOUND = 3;
    int CANCELED = 4;
    int PACE_MONITOR_COMMUNICATION_ERROR = 5;
}

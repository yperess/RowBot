package com.concept2.api.model;

import android.content.Context;

import com.concept2.api.constants.ReportId;

public interface VirtualPaceMonitorApi {

    class ConnectionException extends Exception {
        public ConnectionException(String message) {
            super(message);
        }
    }

    boolean start(Context context);

    void stop();

    boolean isConnected();

    byte[] executeCommandBytes(ReportId reportId, byte[] commandBytes) throws ConnectionException;
}

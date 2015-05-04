package com.concept2.api.pacemonitor;

import android.content.Context;

import com.concept2.api.pacemonitor.commands.ReportId;

public interface VirtualPaceMonitorApi {

    class ConnectionException extends Exception {
        public ConnectionException(String message) {
            super(message);
        }
    }

    boolean start(Context context);

    /**
     *
     * @param context
     * @param timeout The timeout value in milliseconds.
     * @return
     */
    boolean start(Context context, long timeout);

    void stop();

    boolean isConnected();

    byte[] executeCommandBytes(ReportId reportId, byte[] commandBytes) throws ConnectionException;
}

package com.concept2.api.pacemonitor.impl.connection;

import android.content.Context;
import android.os.Handler;

import com.concept2.api.pacemonitor.commands.ReportId;

public interface Engine {

    public static final String EXTRA_RESPONSE_VALUE = "Value";

    /**
     * Start an engine connection.
     *
     * @param context The connecting context.
     * @return True upon a successful connection.
     */
    boolean start(Context context);

    /**
     * Stop the engine connection.
     */
    void stop();

    /**
     * @return True if the engine is connected to a pace monitor.
     */
    boolean isConnected();

    /**
     * Get data from the Pace Monitor.
     *
     * @param handler The handler to report the data to or null to return the result.
     * @param reportId The report's identifier ({@link ReportId}).
     * @param command The command to send to the Pace Monitor.
     * @return The Pace Monitor response or null if sent to the handler.
     */
    byte[] getPMData(Handler handler, ReportId reportId, byte[] command);
}

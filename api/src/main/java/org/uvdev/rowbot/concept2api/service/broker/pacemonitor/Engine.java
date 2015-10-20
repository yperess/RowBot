package org.uvdev.rowbot.concept2api.service.broker.pacemonitor;

import android.content.Context;

public interface Engine {

    final class Concept2EngineConnectionException extends Exception {}

    /**
     * Start an engine connection.
     *
     * @param context The connecting context.
     * @throws Concept2EngineConnectionException if a connection couldn't be formed.
     */
    void start(Context context) throws Concept2EngineConnectionException;

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
     * @param command The command to send to the Pace Monitor.
     * @return The Pace Monitor response or null if sent to the handler.
     * @throws Concept2EngineConnectionException if a connection wasn't found.
     */
    byte[] getPMData(byte[] command) throws Concept2EngineConnectionException,
            Csafe.CsafeExtractException, Csafe.DestuffResult.DestuffException;
}

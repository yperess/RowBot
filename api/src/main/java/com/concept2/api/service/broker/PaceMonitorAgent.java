package com.concept2.api.service.broker;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.common.Constants;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.Csafe;
import com.concept2.api.service.broker.pacemonitor.Engine;
import com.concept2.api.service.broker.pacemonitor.ReportId;
import com.concept2.api.service.broker.pacemonitor.USBEngine;

/**
 * Agent used to access the Pace Monitor.
 */
public class PaceMonitorAgent {

    private static final String TAG = "PaceMonitorAgent";
    private static final boolean DBG = false | Constants.DBG;

    /**
     * Basic commands to communicate with the Pace Monitor.
     */
    private interface Commands {
        byte[] GET_STATUS = new byte[] { (byte) 0x80 };
    }

    /** {@link Engine} instance for a USB connection. */
    private final Engine mUsbEngine = new USBEngine();

    // TODO - Add a BT engine.

    /**
     * Get the status of the connected monitor. If no monitor is connected, this method will return
     * an empty {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status.
     * If an error occured during communication an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The status of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorStatus(Context context) {
        try {
            validateConnection(context);
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor not connected", e);
            return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        }
        byte[] statusBytes;
        try {
            statusBytes = mUsbEngine.getPMData(ReportId.SMALL, Commands.GET_STATUS);
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor connection error", e);
            return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        } catch (Csafe.CsafeExtractException|Csafe.DestuffResult.DestuffException e) {
            Log.e(TAG, "Pace monitor communication error", e);
            return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR);
        }
        if (statusBytes == null || statusBytes.length == 0) {
            return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR);
        }
        int status = (statusBytes[0] & 0x0F) == 1 ? PaceMonitor.Status.READY :
                PaceMonitor.Status.WORKOUT_ACTIVE;

        // place data into content values.
        ContentValues values = new ContentValues();
        values.put(PaceMonitorColumnContract.StatusColumns.STATUS, status);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    private void validateConnection(Context context) throws
            Engine.Concept2EngineConnectionException {
        if (!mUsbEngine.isConnected()) {
            mUsbEngine.start(context);
        }
    }
}

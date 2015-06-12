package com.concept2.api.pacemonitor;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;

/**
 * Pace Monitor APIs.
 */
public interface PaceMonitor {

    /**
     * Status values reported by {@link GetStatusResult}.
     * @see #getStatus(Context)
     */
    interface Status {
        /** Pace monitor is not connected. */
        int WAITING_FOR_CONNECTION = 0;
        /** Pace monitor is ready and waiting. */
        int READY = 1;
        /** Pace monitor has an active workout. */
        int WORKOUT_ACTIVE = 2;
    }

    /**
     * Result returned by {@link #getStatus(Context)}.
     */
    interface GetStatusResult extends Result {
        int getPaceMonitorStatus();
    }

    /**
     * Gets the status of the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetStatusResult> getStatus(Context context);
}

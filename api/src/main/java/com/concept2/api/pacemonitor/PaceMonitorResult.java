package com.concept2.api.pacemonitor;

import com.concept2.api.Result;

/**
 * Pace Monitor result that contains details about the Pace Monitor's current status.
 */
public interface PaceMonitorResult extends Result {

    /**
     * @return The current pace monitor's status via {@link PaceMonitorStatus}.
     */
    PaceMonitorStatus getPaceMonitorStatus();
}

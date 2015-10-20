package org.uvdev.rowbot.concept2api.pacemonitor;

import org.uvdev.rowbot.concept2api.Result;

/**
 * Pace Monitor result that contains details about the Pace Monitor's current status.
 */
public interface PaceMonitorResult extends Result {

    /**
     * @return The current pace monitor's status via {@link PaceMonitorStatus}.
     */
    PaceMonitorStatus getPaceMonitorStatus();
}

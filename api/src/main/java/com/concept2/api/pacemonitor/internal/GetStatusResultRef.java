package com.concept2.api.pacemonitor.internal;

import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract.StatusColumns;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetStatusResult} via a {@link DataHolder}.
 */
public class GetStatusResultRef extends DataHolder implements PaceMonitor.GetStatusResult {

    /**
     * Create a new status result reference around the given data.
     *
     * @param data The data needed to report the pace monitor result.
     */
    public GetStatusResultRef(DataHolder data) {
        super(data);
    }

    @Override
    public int getPaceMonitorStatus() {
        return getInt(StatusColumns.STATUS, PaceMonitor.Status.WAITING_FOR_CONNECTION);
    }

    @Override
    public String toString() {
        return Objects.toString("GetStatusResult")
                .addVal("Status", getStatus())
                .addVal("PaceMonitorStatus", getPaceMonitorStatus())
                .toString();
    }
}

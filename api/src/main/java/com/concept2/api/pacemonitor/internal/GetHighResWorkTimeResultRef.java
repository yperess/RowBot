package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkTimeResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetHighResWorkTimeResult} via a {@link DataHolder}.
 */
public class GetHighResWorkTimeResultRef extends PaceMonitorResultRef implements GetHighResWorkTimeResult {

    /** Column containing high resolution work time. */
    private static final String COLUMN_SECONDS = PaceMonitorColumnContract.HIGH_RES_SECONDS;

    /**
     * Create a new high resolution work time result reference around the given data.
     *
     * @param dataHolder The data needed to report the high resolution work time.
     * @param row The row of data to read.
     */
    public GetHighResWorkTimeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public double getSeconds() {
        return getDouble(COLUMN_SECONDS, 0.0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Seconds", getSeconds());
    }
}

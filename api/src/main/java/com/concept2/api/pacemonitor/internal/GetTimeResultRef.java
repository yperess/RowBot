package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetTimeResult} via a {@link DataHolder}.
 */
public class GetTimeResultRef extends PaceMonitorResultRef implements PaceMonitor.GetTimeResult {

    /** Column containing seconds value. */
    private static final String COLUMN_SECONDS = PaceMonitorColumnContract.SECONDS;

    /**
     * Create a new work time result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace monitor work time.
     * @param row The row of data to read.
     */
    public GetTimeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public long getSeconds() {
        return getLong(COLUMN_SECONDS, 0L);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Seconds", getSeconds());
    }
}

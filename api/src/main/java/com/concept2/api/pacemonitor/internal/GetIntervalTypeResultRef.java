package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitor.GetIntervalTypeResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetIntervalTypeResult} via a {@link DataHolder}.
 */
public class GetIntervalTypeResultRef extends PaceMonitorResultRef implements GetIntervalTypeResult {

    /** Column containing interval type. */
    private static final String COLUMN_INTERVAL_TYPE = PaceMonitorColumnContract.INTERVAL_TYPE;

    /**
     * Create a new interval type result reference around the given data.
     *
     * @param dataHolder The data needed to report the interval type.
     * @param row The row of data to read.
     */
    public GetIntervalTypeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getIntervalType() {
        return getInt(COLUMN_INTERVAL_TYPE, PaceMonitor.IntervalType.INVALID);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("IntervalType", getIntervalType());
    }
}

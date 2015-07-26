package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutIntervalCountResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutIntervalCountResult} via a {@link DataHolder}.
 */
public class GetWorkoutIntervalCountResultRef extends PaceMonitorResultRef implements
        GetWorkoutIntervalCountResult {

    /** Column containing interval count. */
    private static final String COLUMN_INTERVAL_COUNT = PaceMonitorColumnContract.INTERVAL_COUNT;

    /**
     * Create a new interval count result reference around the given data.
     *
     * @param dataHolder The data needed to report the interval count.
     * @param row The row of data to read.
     */
    public GetWorkoutIntervalCountResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getWorkoutIntervalCount() {
        return getInt(COLUMN_INTERVAL_COUNT, Integer.MIN_VALUE);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("WorkoutIntervalCount", getWorkoutIntervalCount());
    }
}

package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutIntervalCountResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutIntervalCountResult} via a {@link DataHolder}.
 */
public class GetWorkoutIntervalCountResultRef extends PaceMonitorResultRef implements
        GetWorkoutIntervalCountResult {

    /** Column containing interval count. */
    private static final String COLUMN_INTERVAL_COUNT = "intervalCount";

    /**
     * Create a new {@link DataHolder} for a {@link GetWorkoutIntervalCountResult}. Using this
     * method assumes that the communication returned a status code of
     * {@link Concept2StatusCodes#OK}.
     *
     * @param intervalCount The interval count.
     * @return {@link DataHolder} representing a {@link GetWorkoutIntervalCountResult}.
     */
    public static DataHolder createDataHolder(int intervalCount) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_INTERVAL_COUNT, intervalCount);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new interval count result reference around the given data.
     *
     * @param dataHolder The data needed to report the interval count.
     */
    public GetWorkoutIntervalCountResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

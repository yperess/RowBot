package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutTypeResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutTypeResult} via a {@link DataHolder}.
 */
public class GetWorkoutTypeResultRef extends PaceMonitorResultRef implements GetWorkoutTypeResult {

    /** Column containing workout type. */
    private static final String COLUMN_WORKOUT_TYPE = PaceMonitorColumnContract.WORKOUT_TYPE;

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout type.
     * @param row The row of data to read.
     */
    public GetWorkoutTypeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getWorkoutType() {
        return getInt(COLUMN_WORKOUT_TYPE, PaceMonitor.WorkoutType.INVALID);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("WorkoutType", getWorkoutType());
    }
}

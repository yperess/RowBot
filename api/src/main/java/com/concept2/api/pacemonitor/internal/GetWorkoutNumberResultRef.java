package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutNumberResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutNumberResult} via a {@link DataHolder}.
 */
public class GetWorkoutNumberResultRef extends PaceMonitorResultRef implements GetWorkoutNumberResult {

    /** Column containing workout number. */
    private static final String COLUMN_WORKOUT_NUMBER = PaceMonitorColumnContract.WORKOUT_NUMBER;

    /**
     * Create a new workout number result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout number.
     * @param row The row of data to read.
     */
    public GetWorkoutNumberResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getWorkoutNumber() {
        return getInt(COLUMN_WORKOUT_NUMBER, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("WorkoutNumber", getWorkoutNumber());
    }
}

package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutNumberResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutNumberResult} via a {@link DataHolder}.
 */
public class GetWorkoutNumberResultRef extends PaceMonitorResultRef implements GetWorkoutNumberResult {

    /** Column containing workout number. */
    private static final String COLUMN_WORKOUT_NUMBER = "workoutNumber";

    /**
     * Create a new {@link DataHolder} for a {@link GetWorkoutNumberResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param workoutNumber The workout type.
     * @return {@link DataHolder} representing a {@link GetWorkoutNumberResult}.
     */
    public static DataHolder createDataHolder(int workoutNumber) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_WORKOUT_NUMBER, workoutNumber);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new workout number result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout number.
     */
    public GetWorkoutNumberResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

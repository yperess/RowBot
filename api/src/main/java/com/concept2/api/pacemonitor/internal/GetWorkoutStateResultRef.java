package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutStateResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutStateResult} via a {@link DataHolder}.
 */
public class GetWorkoutStateResultRef extends PaceMonitorResultRef implements GetWorkoutStateResult {

    /** Column containing workout state. */
    private static final String COLUMN_WORKOUT_STATE = "workoutState";

    /**
     * Create a new {@link DataHolder} for a {@link GetWorkoutStateResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param workoutState The workout state.
     * @return {@link DataHolder} representing a {@link GetWorkoutStateResult}.
     */
    public static DataHolder createDataHolder(int workoutState) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_WORKOUT_STATE, workoutState);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout state.
     */
    public GetWorkoutStateResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getWorkoutState() {
        return getInt(COLUMN_WORKOUT_STATE, PaceMonitor.WorkoutState.INVALID);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("WorkoutState", getWorkoutState());
    }
}

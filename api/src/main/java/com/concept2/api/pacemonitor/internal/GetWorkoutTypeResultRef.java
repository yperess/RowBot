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
     * Create a new {@link DataHolder} for a {@link GetWorkoutTypeResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param workoutType The workout type.
     * @return {@link DataHolder} representing a {@link GetWorkoutTypeResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int workoutType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_TYPE, workoutType);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout type.
     */
    public GetWorkoutTypeResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

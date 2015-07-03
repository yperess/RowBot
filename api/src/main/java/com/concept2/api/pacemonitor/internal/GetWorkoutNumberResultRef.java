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
     * Create a new {@link DataHolder} for a {@link GetWorkoutNumberResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param workoutNumber The workout type.
     * @return {@link DataHolder} representing a {@link GetWorkoutNumberResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int workoutNumber) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_NUMBER, workoutNumber);
        values.putAll(status.toContentValues());
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

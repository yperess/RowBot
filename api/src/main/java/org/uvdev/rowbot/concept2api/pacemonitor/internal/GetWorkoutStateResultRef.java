package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetWorkoutStateResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link GetWorkoutStateResult} via a {@link DataHolder}.
 */
public class GetWorkoutStateResultRef extends PaceMonitorResultRef implements GetWorkoutStateResult {

    /** Column containing workout state. */
    private static final String COLUMN_WORKOUT_STATE = PaceMonitorColumnContract.WORKOUT_STATE;

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout state.
     * @param row The row of data to read.
     */
    public GetWorkoutStateResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
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

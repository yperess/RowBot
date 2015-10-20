package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetWorkoutNumberResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

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

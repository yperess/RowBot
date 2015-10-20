package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetWorkoutIntervalCountResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

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

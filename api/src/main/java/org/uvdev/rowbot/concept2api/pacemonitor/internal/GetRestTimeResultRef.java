package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetRestTimeResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link GetRestTimeResult} via a {@link DataHolder}.
 */
public class GetRestTimeResultRef extends PaceMonitorResultRef implements GetRestTimeResult {

    /** Column containing rest time. */
    private static final String COLUMN_REST_TIME = PaceMonitorColumnContract.REST_TIME;

    /**
     * Create a new rest time result reference around the given data.
     *
     * @param dataHolder The data needed to report the rest time.
     * @param row The row of data to read.
     */
    public GetRestTimeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getRestTime() {
        return getInt(COLUMN_REST_TIME, Integer.MIN_VALUE);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("RestTime", getRestTime());
    }
}

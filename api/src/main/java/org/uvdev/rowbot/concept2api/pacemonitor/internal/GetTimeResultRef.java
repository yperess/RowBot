package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetTimeResult} via a {@link DataHolder}.
 */
public class GetTimeResultRef extends PaceMonitorResultRef implements PaceMonitor.GetTimeResult {

    /** Column containing seconds value. */
    private static final String COLUMN_SECONDS = PaceMonitorColumnContract.SECONDS;

    /**
     * Create a new work time result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace monitor work time.
     * @param row The row of data to read.
     */
    public GetTimeResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public long getSeconds() {
        return getLong(COLUMN_SECONDS, 0L);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Seconds", getSeconds());
    }
}

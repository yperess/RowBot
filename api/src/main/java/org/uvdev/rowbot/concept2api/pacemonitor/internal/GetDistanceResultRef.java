package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetDistanceResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * A reference to {@link GetDistanceResult} via a {@link DataHolder}.
 */
public class GetDistanceResultRef extends PaceMonitorResultRef implements GetDistanceResult {

    /** Column containing distance value. */
    private static final String COLUMN_DISTANCE = PaceMonitorColumnContract.METERS;

    /**
     * Create a new distance result reference around the given data.
     *
     * @param dataHolder The data needed to report distance.
     * @param row The row of data to read.
     */
    public GetDistanceResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getDistance() {
        return getInt(COLUMN_DISTANCE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Distance", getDistance());
    }
}

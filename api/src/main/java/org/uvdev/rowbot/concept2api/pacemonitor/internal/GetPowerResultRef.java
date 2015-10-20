package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetPowerResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link GetPowerResult} via a {@link DataHolder}.
 */
public class GetPowerResultRef extends PaceMonitorResultRef implements GetPowerResult {

    /** Column containing power in watts. */
    private static final String COLUMN_POWER = PaceMonitorColumnContract.POWER;

    /**
     * Create a new power result reference around the given data.
     *
     * @param dataHolder The data needed to report the current power.
     * @param row The row of data to read.
     */
    public GetPowerResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getPower() {
        return getInt(COLUMN_POWER, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Power", getPower());
    }
}

package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetHeartRateResult} via a {@link DataHolder}.
 */
public class GetHeartRateResultRef extends PaceMonitorResultRef implements PaceMonitor.GetHeartRateResult {

    /** Column containing heart rate value. */
    private static final String COLUMN_HEART_RATE = PaceMonitorColumnContract.HEART_RATE;

    /**
     * Create a new heart rate result reference around the given data.
     *
     * @param dataHolder The data needed to report the heart rate.
     */
    public GetHeartRateResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getHeartRate() {
        return getInt(COLUMN_HEART_RATE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("HeartRate", getHeartRate());
    }
}

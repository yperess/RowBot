package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetErrorValueResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

/**
 * Reference to the {@link GetErrorValueResult} via a {@link DataHolder}.
 */
public class GetErrorValueResultRef extends PaceMonitorResultRef implements GetErrorValueResult {

    /** Column containing workout type. */
    private static final String COLUMN_ERROR_VALUE = PaceMonitorColumnContract.ERROR_VALUE;

    /**
     * Create a new error value result reference around the given data.
     *
     * @param dataHolder The data needed to report the error value.
     * @param row The row of data to read.
     */
    public GetErrorValueResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getErrorValue() {
        return getInt(COLUMN_ERROR_VALUE, Integer.MIN_VALUE);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("ErrorValue", getErrorValue());
    }
}

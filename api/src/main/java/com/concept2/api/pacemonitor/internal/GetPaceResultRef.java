package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetPaceResult} via a {@link DataHolder}.
 */
public class GetPaceResultRef extends PaceMonitorResultRef implements PaceMonitor.GetPaceResult {

    /** Column containing the pace in seconds / km. */
    private static final String COLUMN_PACE = PaceMonitorColumnContract.PACE;

    /**
     * Create a new pace result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace.
     * @param row The row of data to read.
     */
    public GetPaceResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getPace() {
        return getInt(COLUMN_PACE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Pace", getPace());
    }
}

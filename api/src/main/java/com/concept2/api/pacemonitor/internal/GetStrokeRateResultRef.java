package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetStrokeRateResult} via a {@link DataHolder}.
 */
public class GetStrokeRateResultRef extends PaceMonitorResultRef implements PaceMonitor.GetStrokeRateResult {

    /** Column containing the stroke rate value. */
    private static final String COLUMN_STROKE_RATE = PaceMonitorColumnContract.STROKE_RATE;

    /**
     * Create a new stroke rate result reference around the given data.
     *
     * @param dataHolder The data needed to report the stroke rate.
     * @param row The row of data to read.
     */
    public GetStrokeRateResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getStrokeRate() {
        return getInt(COLUMN_STROKE_RATE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("StrokeRate", getStrokeRate());
    }
}

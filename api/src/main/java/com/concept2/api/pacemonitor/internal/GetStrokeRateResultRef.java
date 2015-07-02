package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetStrokeRateResult} via a {@link DataHolder}.
 */
public class GetStrokeRateResultRef extends PaceMonitorResultRef implements PaceMonitor.GetStrokeRateResult {

    /** Column containing the stroke rate value. */
    private static final String COLUMN_STROKE_RATE = "strokeRate";

    /**
     * Create a new {@link DataHolder} for a given stroke rate. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param strokeRate The stroke rating.
     * @return {@link DataHolder} representing the stroke rate.
     */
    public static DataHolder createDataHolder(int strokeRate) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_STROKE_RATE, strokeRate);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new stroke rate result reference around the given data.
     *
     * @param dataHolder The data needed to report the stroke rate.
     */
    public GetStrokeRateResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getStrokeRate() {
        return getInt(COLUMN_STROKE_RATE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("StrokeRate", builder);
    }
}

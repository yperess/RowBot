package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitor.GetIntervalTypeResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetIntervalTypeResult} via a {@link DataHolder}.
 */
public class GetIntervalTypeResultRef extends PaceMonitorResultRef implements GetIntervalTypeResult {

    /** Column containing interval type. */
    private static final String COLUMN_INTERVAL_TYPE = "intervalType";

    /**
     * Create a new {@link DataHolder} for a {@link GetIntervalTypeResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param intervalType The interval type.
     * @return {@link DataHolder} representing a {@link GetIntervalTypeResult}.
     */
    public static DataHolder createDataHolder(int intervalType) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_INTERVAL_TYPE, intervalType);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new interval type result reference around the given data.
     *
     * @param dataHolder The data needed to report the interval type.
     */
    public GetIntervalTypeResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getIntervalType() {
        return getInt(COLUMN_INTERVAL_TYPE, PaceMonitor.IntervalType.INVALID);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("ItervalType", getIntervalType());
    }
}

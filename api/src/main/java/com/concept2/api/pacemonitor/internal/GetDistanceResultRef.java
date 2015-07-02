package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetDistanceResult;
import com.concept2.api.utils.Objects;

/**
 * A reference to {@link GetDistanceResult} via a {@link DataHolder}.
 */
public class GetDistanceResultRef extends PaceMonitorResultRef implements GetDistanceResult {

    /** Column containing distance value. */
    private static final String COLUMN_DISTANCE = "distance";

    /**
     * Creates a new {@link DataHolder} for a specified distance. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param distance The distance represented by this result.
     * @return {@link DataHolder} representing the distance.
     */
    public static DataHolder createDataHolder(int distance) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_DISTANCE, distance);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new distance result reference around the given data.
     *
     * @param dataHolder The data needed to report distance.
     */
    public GetDistanceResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

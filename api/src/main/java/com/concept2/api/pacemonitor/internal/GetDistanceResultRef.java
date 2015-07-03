package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetDistanceResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * A reference to {@link GetDistanceResult} via a {@link DataHolder}.
 */
public class GetDistanceResultRef extends PaceMonitorResultRef implements GetDistanceResult {

    /** Column containing distance value. */
    private static final String COLUMN_DISTANCE = PaceMonitorColumnContract.METERS;

    /**
     * Creates a new {@link DataHolder} for a specified distance. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param distance The distance represented by this result.
     * @return {@link DataHolder} representing the distance.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int distance) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTANCE, distance);
        values.putAll(status.toContentValues());
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

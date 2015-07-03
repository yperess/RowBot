package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkDistanceResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetHighResWorkDistanceResult} via a {@link DataHolder}.
 */
public class GetHighResWorkDistanceResultRef extends PaceMonitorResultRef implements
        GetHighResWorkDistanceResult {

    /** Column containing high resolution work distance. */
    private static final String COLUMN_DISTANCE = PaceMonitorColumnContract.HIGH_RES_DISTANCE;

    /**
     * Create a new {@link DataHolder} for a {@link GetHighResWorkDistanceResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param meters The workout type.
     * @return {@link DataHolder} representing a {@link GetHighResWorkDistanceResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, double meters) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISTANCE, meters);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new high resolution work distance result reference around the given data.
     *
     * @param dataHolder The data needed to report the high resolution work distance.
     */
    public GetHighResWorkDistanceResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public double getDistance() {
        return getDouble(COLUMN_DISTANCE, 0.0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("WorkDistance", getDistance());
    }
}

package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitor.GetDistanceResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

import java.nio.ByteBuffer;

/**
 * A reference to {@link GetDistanceResult} via a {@link DataHolder}.
 */
public class GetDistanceResultRef extends PaceMonitorResultRef implements GetDistanceResult {

    /** Column containing distance value. */
    private static final String COLUMN_DISTANCE = PaceMonitorColumnContract.METERS;

    /**
     * Create a new distance result reference around the given data.
     *
     * @param dataHolder The data needed to report distance.
     * @param row The row of data to read.
     */
    public GetDistanceResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
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

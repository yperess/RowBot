package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRatePlotResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Reference to the {@link GetHeartRatePlotResult} via a {@link DataHolder}.
 */
public class GetHeartRatePlotResultRef extends PaceMonitorResultRef implements
        GetHeartRatePlotResult {

    /** Column containing heart rate plot. */
    private static final String COLUMN_HEART_RATE_PLOT = PaceMonitorColumnContract.HEART_RATE_PLOT;

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout type.
     * @param row The row of data to read.
     */
    public GetHeartRatePlotResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int[] getHeartRatePlot() {
        return getIntArray(COLUMN_HEART_RATE_PLOT, null /* defaultValue */);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("HeartRatePlot", Arrays.toString(getHeartRatePlot()));
    }
}

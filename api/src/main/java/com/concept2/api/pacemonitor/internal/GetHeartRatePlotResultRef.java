package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRatePlotResult;
import com.concept2.api.utils.Objects;

import java.util.Arrays;

/**
 * Reference to the {@link GetHeartRatePlotResult} via a {@link DataHolder}.
 */
public class GetHeartRatePlotResultRef extends PaceMonitorResultRef implements GetHeartRatePlotResult {

    /** Column containing heart rate plot. */
    private static final String COLUMN_HEART_RATE_PLOT = "heartRatePlot";

    /**
     * Create a new {@link DataHolder} for a {@link GetHeartRatePlotResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param heartRatePlot The heart rate plot.
     * @return {@link DataHolder} representing a {@link GetHeartRatePlotResult}.
     */
    public static DataHolder createDataHolder(int[] heartRatePlot) {
        Bundle values = new Bundle();
        values.putIntArray(COLUMN_HEART_RATE_PLOT, heartRatePlot);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new workout type result reference around the given data.
     *
     * @param dataHolder The data needed to report the workout type.
     */
    public GetHeartRatePlotResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

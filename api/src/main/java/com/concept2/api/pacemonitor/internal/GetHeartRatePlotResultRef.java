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
     * Create a new {@link DataHolder} for a {@link GetHeartRatePlotResult}. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param heartRatePlot The heart rate plot.
     * @return {@link DataHolder} representing a {@link GetHeartRatePlotResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int[] heartRatePlot) {
        ByteBuffer buffer = ByteBuffer.allocate(heartRatePlot.length * 4);
        buffer.asIntBuffer().put(heartRatePlot);

        ContentValues values = new ContentValues();
        values.put(COLUMN_HEART_RATE_PLOT, buffer.array());
        values.putAll(status.toContentValues());
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

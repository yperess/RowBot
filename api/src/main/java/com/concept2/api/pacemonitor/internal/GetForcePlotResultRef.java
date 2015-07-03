package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;
import android.util.Log;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetForcePlotResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Reference to the {@link GetForcePlotResult} via a {@link DataHolder}.
 */
public class GetForcePlotResultRef extends PaceMonitorResultRef implements GetForcePlotResult {

    /** Column containing forcePlot. */
    private static final String COLUMN_FORCE_PLOT = PaceMonitorColumnContract.FORCE_PLOT;

    /**
     * Create a new {@link DataHolder} for a {@link GetForcePlotResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param forcePlot The force plot.
     * @return {@link DataHolder} representing a {@link GetForcePlotResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int[] forcePlot) {
        Log.d("Plot", "Raw force plot: " + Arrays.toString(forcePlot));
        ByteBuffer buffer = ByteBuffer.allocate(forcePlot.length * 4);
        buffer.asIntBuffer().put(forcePlot);
        Log.d("Plot", "Byte value: " + Arrays.toString(buffer.array()));

        ContentValues values = new ContentValues();
        values.put(COLUMN_FORCE_PLOT, buffer.array());
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new force plot result reference around the given data.
     *
     * @param dataHolder The data needed to report the force plot.
     */
    public GetForcePlotResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int[] getForcePlot() {
        return getIntArray(COLUMN_FORCE_PLOT, null /* defaultValue */);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("ForcePlot", Arrays.toString(getForcePlot()));
    }
}

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
     * Create a new force plot result reference around the given data.
     *
     * @param dataHolder The data needed to report the force plot.
     * @param row The row of data to read.
     */
    public GetForcePlotResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int[] getForcePlot() {
        return getIntArray(COLUMN_FORCE_PLOT, new int[0] /* defaultValue */);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("ForcePlot", Arrays.toString(getForcePlot()));
    }
}

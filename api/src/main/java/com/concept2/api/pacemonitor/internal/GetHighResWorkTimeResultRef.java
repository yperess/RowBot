package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkTimeResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetHighResWorkTimeResult} via a {@link DataHolder}.
 */
public class GetHighResWorkTimeResultRef extends PaceMonitorResultRef implements GetHighResWorkTimeResult {

    /** Column containing high resolution work time. */
    private static final String COLUMN_SECONDS = PaceMonitorColumnContract.HIGH_RES_SECONDS;

    /**
     * Create a new {@link DataHolder} for a {@link GetHighResWorkTimeResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param seconds The high resolution work time.
     * @return {@link DataHolder} representing a {@link GetHighResWorkTimeResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, double seconds) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SECONDS, seconds);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new high resolution work time result reference around the given data.
     *
     * @param dataHolder The data needed to report the high resolution work time.
     */
    public GetHighResWorkTimeResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public double getSeconds() {
        return getDouble(COLUMN_SECONDS, 0.0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Seconds", getSeconds());
    }
}

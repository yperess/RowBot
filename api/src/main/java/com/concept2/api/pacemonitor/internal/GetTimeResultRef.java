package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetTimeResult} via a {@link DataHolder}.
 */
public class GetTimeResultRef extends PaceMonitorResultRef implements PaceMonitor.GetTimeResult {

    /** Column containing seconds value. */
    private static final String COLUMN_SECONDS = "seconds";

    /**
     * Creates a new {@link DataHolder} for a given number of work seconds. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param seconds The number of seconds displayed on the pace monitor.
     * @return {@link DataHolder} representing the command status and work time.
     */
    public static DataHolder createDataHolder(long seconds) {
        Bundle values = new Bundle();
        values.putLong(COLUMN_SECONDS, seconds);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new work time result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace monitor work time.
     */
    public GetTimeResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public long getSeconds() {
        return getLong(COLUMN_SECONDS, 0L);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Seconds", getSeconds());
    }
}

package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetRestTimeResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetRestTimeResult} via a {@link DataHolder}.
 */
public class GetRestTimeResultRef extends PaceMonitorResultRef implements GetRestTimeResult {

    /** Column containing rest time. */
    private static final String COLUMN_REST_TIME = "restTime";

    /**
     * Create a new {@link DataHolder} for a {@link GetRestTimeResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param restTime The rest time in seconds.
     * @return {@link DataHolder} representing a {@link GetRestTimeResult}.
     */
    public static DataHolder createDataHolder(int restTime) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_REST_TIME, restTime);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new rest time result reference around the given data.
     *
     * @param dataHolder The data needed to report the rest time.
     */
    public GetRestTimeResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getRestTime() {
        return getInt(COLUMN_REST_TIME, Integer.MIN_VALUE);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("RestTime", getRestTime());
    }
}

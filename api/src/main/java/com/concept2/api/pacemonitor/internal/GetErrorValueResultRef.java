package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetErrorValueResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetErrorValueResult} via a {@link DataHolder}.
 */
public class GetErrorValueResultRef extends PaceMonitorResultRef implements GetErrorValueResult {

    /** Column containing workout type. */
    private static final String COLUMN_ERROR_VALUE = PaceMonitorColumnContract.ERROR_VALUE;

    /**
     * Create a new {@link DataHolder} for a {@link GetErrorValueResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param errorValue The error value.
     * @return {@link DataHolder} representing a {@link GetErrorValueResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int errorValue) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ERROR_VALUE, errorValue);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new error value result reference around the given data.
     *
     * @param dataHolder The data needed to report the error value.
     */
    public GetErrorValueResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getErrorValue() {
        return getInt(COLUMN_ERROR_VALUE, Integer.MIN_VALUE);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("ErrorValue", getErrorValue());
    }
}

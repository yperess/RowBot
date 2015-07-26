package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;
import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitorResult;
import com.concept2.api.pacemonitor.PaceMonitorStatus;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitorResult} via a {@link DataHolder}.
 */
public class PaceMonitorResultRef extends DataHolder implements PaceMonitorResult {

    /** Column containing the status value. */
    private static final String COLUMN_FRAME_COUNT = PaceMonitorColumnContract.FRAME_COUNT;
    private static final String COLUMN_PREV_FRAME_STATUS =
            PaceMonitorColumnContract.PREV_FRAME_STATUS;
    private static final String COLUMN_SLAVE_STATUS = PaceMonitorColumnContract.SLAVE_STATUS;

    private final int mRow;
    private final PaceMonitorStatus mPaceMonitorStatus;

    /**
     * Create a new status result reference around the given data.
     *
     * @param data The data needed to report the pace monitor result.
     * @param row The row of data to read.
     */
    public PaceMonitorResultRef(DataHolder data, int row) {
        super(data);
        mRow = row;
        mPaceMonitorStatus = new PaceMonitorStatusImpl(getInt(COLUMN_FRAME_COUNT, 0),
                getInt(COLUMN_PREV_FRAME_STATUS, 0), getInt(COLUMN_SLAVE_STATUS, 0));
    }

    protected final int getInt(String columnName, int defaultValue) {
        return getInt(mRow, columnName, defaultValue);
    }

    protected final long getLong(String columnName, long defaultValue) {
        return getLong(mRow, columnName, defaultValue);
    }

    protected final double getDouble(String columnName, double defaultValue) {
        return getDouble(mRow, columnName, defaultValue);
    }

    protected int[] getIntArray(String columnName, int[] defaultValue) {
        return getIntArray(mRow, columnName, defaultValue);
    }

    @Override
    public PaceMonitorStatus getPaceMonitorStatus() {
        return mPaceMonitorStatus;
    }

    @Override
    public String toString() {
        Objects.ObjectsStringBuilder builder = Objects.stringBuilder()
                .addVal("Status", getStatus())
                .addVal("PaceMonitorStatus", getPaceMonitorStatus());
        buildString(builder);
        return builder.toString();
    }

    protected void buildString(Objects.ObjectsStringBuilder builder) {}
}

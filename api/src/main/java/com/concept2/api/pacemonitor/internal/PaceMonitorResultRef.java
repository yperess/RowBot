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

    /**
     * Creates a new {@link DataHolder} for a given pace monitor status. Using this method
     * assumes that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param status The current status of the pace monitor.
     * @return {@link DataHolder} representing the command and pace monitor status.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status) {
        return new DataHolder(Concept2StatusCodes.OK, status.toContentValues());
    }

    private final PaceMonitorStatus mPaceMonitorStatus;

    /**
     * Create a new status result reference around the given data.
     *
     * @param data The data needed to report the pace monitor result.
     */
    public PaceMonitorResultRef(DataHolder data) {
        super(data);
        mPaceMonitorStatus = new PaceMonitorStatusImpl(getInt(COLUMN_FRAME_COUNT, 0),
                getInt(COLUMN_PREV_FRAME_STATUS, 0), getInt(COLUMN_SLAVE_STATUS, 0));
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
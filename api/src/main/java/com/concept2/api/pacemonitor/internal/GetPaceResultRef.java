package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetPaceResult} via a {@link DataHolder}.
 */
public class GetPaceResultRef extends PaceMonitorResultRef implements PaceMonitor.GetPaceResult {

    /** Column containing the pace in seconds / km. */
    private static final String COLUMN_PACE = "pace";

    /**
     * Creates a new {@link DataHolder} for a given pace. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param pace The pace to store.
     * @return {@link DataHolder} representing the pace.
     */
    public static DataHolder createDataHolder(int pace) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_PACE, pace);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new pace result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace.
     */
    public GetPaceResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getPace() {
        return getInt(COLUMN_PACE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Pace", getPace());
    }
}

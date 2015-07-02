package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitor.GetStrokeStateResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetStrokeStateResult} via a {@link DataHolder}.
 */
public class GetStrokeStateResultRef extends PaceMonitorResultRef implements GetStrokeStateResult {

    /** Column containing the stroke state. */
    private static final String COLUMN_STROKE_STATE = "strokeState";

    /**
     * Create a new {@link DataHolder} for a {@link GetStrokeStateResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param strokeState The stroke state.
     * @return {@link DataHolder} representing a {@link GetStrokeStateResult}.
     */
    public static DataHolder createDataHolder(int strokeState) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_STROKE_STATE, strokeState);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new stroke state result reference around the given data.
     *
     * @param dataHolder The data needed to report the stroke state.
     */
    public GetStrokeStateResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getStrokeState() {
        return getInt(COLUMN_STROKE_STATE, PaceMonitor.StrokeState.INVALID);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("StrokeState", getStrokeState());
    }
}

package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetDragFactorResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetDragFactorResult} via a {@link DataHolder}.
 */
public class GetDragFactorResultRef extends PaceMonitorResultRef implements GetDragFactorResult {

    /** Column containing the drag factor.*/
    private static final String COLUMN_DRAG_FACTOR = PaceMonitorColumnContract.DRAG_FACTOR;

    /**
     * Create a new {@link DataHolder} for a {@link GetDragFactorResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param dragFactor The drag factor.
     * @return {@link DataHolder} representing a {@link GetDragFactorResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int dragFactor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRAG_FACTOR, dragFactor);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new drag factor result reference around the given data.
     *
     * @param dataHolder The data needed to report the drag factor.
     */
    public GetDragFactorResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getDragFactor() {
        return getInt(COLUMN_DRAG_FACTOR, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("DragFactor", getDragFactor());
    }
}

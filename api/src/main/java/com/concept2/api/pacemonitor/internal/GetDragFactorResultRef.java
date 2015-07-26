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
     * Create a new drag factor result reference around the given data.
     *
     * @param dataHolder The data needed to report the drag factor.
     * @param row The row of data to read.
     */
    public GetDragFactorResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
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

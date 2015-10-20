package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.GetDragFactorResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

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

package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetCaloriesResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetCaloriesResult} via a {@link DataHolder}.
 */
public class GetCaloriesResultRef extends PaceMonitorResultRef implements GetCaloriesResult {

    /** Column containing calorie value. */
    private static final String COLUMN_CALORIES = PaceMonitorColumnContract.CALORIES;

    /**
     * Create a new calorie result reference around the given data.
     *
     * @param dataHolder The data needed to report the calories.
     * @param row The row of data to read.
     */
    public GetCaloriesResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
    }

    @Override
    public int getCalories() {
        return getInt(COLUMN_CALORIES, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Calories", getCalories());
    }
}

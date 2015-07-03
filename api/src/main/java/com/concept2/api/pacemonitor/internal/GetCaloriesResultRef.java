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
     * Create a new {@link DataHolder} for a {@link GetCaloriesResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param status The status returned by the pace monitor.
     * @param calories The calorie value.
     * @return {@link DataHolder} representing a {@link GetCaloriesResult}.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int calories) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CALORIES, calories);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new calorie result reference around the given data.
     *
     * @param dataHolder The data needed to report the calories.
     */
    public GetCaloriesResultRef(DataHolder dataHolder) {
        super(dataHolder);
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

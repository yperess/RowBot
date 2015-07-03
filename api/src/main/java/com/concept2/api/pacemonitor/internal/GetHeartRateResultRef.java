package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetHeartRateResult} via a {@link DataHolder}.
 */
public class GetHeartRateResultRef extends PaceMonitorResultRef implements PaceMonitor.GetHeartRateResult {

    /** Column containing heart rate value. */
    private static final String COLUMN_HEART_RATE = PaceMonitorColumnContract.HEART_RATE;

    /**
     * Creates a new {@link DataHolder} a given heart rate. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param heartRate The heart rate to store.
     * @return {@link DataHolder} representing this result.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int heartRate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEART_RATE, heartRate);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new heart rate result reference around the given data.
     *
     * @param dataHolder The data needed to report the heart rate.
     */
    public GetHeartRateResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getHeartRate() {
        return getInt(COLUMN_HEART_RATE, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("HeartRate", getHeartRate());
    }
}

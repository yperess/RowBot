package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetPowerResult;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link GetPowerResult} via a {@link DataHolder}.
 */
public class GetPowerResultRef extends PaceMonitorResultRef implements GetPowerResult {

    /** Column containing power in watts. */
    private static final String COLUMN_POWER = "power";

    /**
     * Create a new {@link DataHolder} for a {@link GetPowerResult}. Using this method assumes
     * that the communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param power The power in watts.
     * @return {@link DataHolder} representing a {@link GetPowerResult}.
     */
    public static DataHolder createDataHolder(int power) {
        Bundle values = new Bundle();
        values.putInt(COLUMN_POWER, power);
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new power result reference around the given data.
     *
     * @param dataHolder The data needed to report the current power.
     */
    public GetPowerResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getPower() {
        return getInt(COLUMN_POWER, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Power", getPower());
    }
}

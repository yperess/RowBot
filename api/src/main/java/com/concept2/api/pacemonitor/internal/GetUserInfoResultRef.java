package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.PaceMonitorStatusImpl;
import com.concept2.api.utils.Objects;

/**
 * Reference to the {@link PaceMonitor.GetUserInfoResult} via a {@link DataHolder}.
 */
public class GetUserInfoResultRef extends PaceMonitorResultRef implements PaceMonitor.GetUserInfoResult {

    /** Column containing the weight value. */
    private static final String COLUMN_WEIGHT = PaceMonitorColumnContract.WEIGHT;

    /** Column containing the age value. */
    private static final String COLUMN_AGE = PaceMonitorColumnContract.AGE;

    /** Column containing the gender value. */
    private static final String COLUMN_GENDER = PaceMonitorColumnContract.GENDER;

    /**
     * Creates a new {@link DataHolder} for a given user info. Using this method assumes that the
     * communication returned a status code of {@link Concept2StatusCodes#OK}.
     *
     * @param weight The pace monitor user's weight in Kg.
     * @param age The pace monitor user's age in years.
     * @param gender The pace monitor user's gender code.
     * @return {@link DataHolder} representing the user's info.
     */
    public static DataHolder createDataHolder(PaceMonitorStatusImpl status, int weight, int age,
            int gender) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.putAll(status.toContentValues());
        return new DataHolder(Concept2StatusCodes.OK, values);
    }

    /**
     * Create a new user info result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace monitor's user info.
     */
    public GetUserInfoResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getWeight() {
        return getInt(COLUMN_WEIGHT, 0);
    }

    @Override
    public int getAge() {
        return getInt(COLUMN_AGE, 0);
    }

    @Override
    public int getGender() {
        return getInt(COLUMN_GENDER, 0);
    }

    @Override
    protected void buildString(Objects.ObjectsStringBuilder builder) {
        builder.addVal("Weight", getWeight())
                .addVal("Age", getAge())
                .addVal("Gender", getGender());
    }
}

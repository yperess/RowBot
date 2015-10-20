package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

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
     * Create a new user info result reference around the given data.
     *
     * @param dataHolder The data needed to report the pace monitor's user info.
     * @param row The row of data to read.
     */
    public GetUserInfoResultRef(DataHolder dataHolder, int row) {
        super(dataHolder, row);
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

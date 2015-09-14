package com.concept2.api.rowbot.profile.internal;

import android.content.ContentValues;
import android.os.Parcel;

import com.concept2.api.internal.DataHolder;
import com.concept2.api.rowbot.internal.RowBotContract.ProfileColumns;
import com.concept2.api.rowbot.profile.ProfileSettings;
import com.concept2.api.utils.Preconditions;

public class ProfileSettingsRef extends DataHolder implements ProfileSettings {

    private final int mRow;

    public ProfileSettingsRef(DataHolder dataHolder, int row) {
        super(dataHolder);
        mRow = row;
    }

    @Override
    public boolean provideUseStatistics() {
        return getBoolean(mRow, ProfileColumns.PROVIDE_USE_STATISTICS, true);
    }

    @Override
    public boolean applyWeightAdjustment() {
        return getBoolean(mRow, ProfileColumns.APPLY_WEIGHT_ADJUSTMENT, false);
    }

    @Override
    public boolean applyAgeAdjustment() {
        return getBoolean(mRow, ProfileColumns.APPLY_AGE_ADJUSTMENT, false);
    }

    @Override
    public boolean applyBoatAdjustment() {
        return getBoolean(mRow, ProfileColumns.APPLY_BOAT_ADJUSTMENT, false);
    }

    @Override
    public int getBoatType() {
        return getInt(mRow, ProfileColumns.BOAT_TYPE, BoatType.UNSPECIFIED);
    }

    @Override
    public int getDataResolution() {
        return getInt(mRow, ProfileColumns.DATA_RESOLUTION, DataResolution.MEDIUM);
    }

    public static ContentValues toDatabaseValues(ProfileSettings settings) {
        Preconditions.assertNotNull(settings);
        ContentValues values = new ContentValues();
        values.put(ProfileColumns.PROVIDE_USE_STATISTICS, settings.provideUseStatistics());
        values.put(ProfileColumns.APPLY_WEIGHT_ADJUSTMENT, settings.applyWeightAdjustment());
        values.put(ProfileColumns.APPLY_AGE_ADJUSTMENT, settings.applyAgeAdjustment());
        values.put(ProfileColumns.APPLY_BOAT_ADJUSTMENT, settings.applyBoatAdjustment());
        values.put(ProfileColumns.BOAT_TYPE, settings.getBoatType());
        values.put(ProfileColumns.DATA_RESOLUTION, settings.getDataResolution());
        return values;
    }
}

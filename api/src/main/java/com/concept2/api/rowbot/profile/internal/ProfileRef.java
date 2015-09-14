package com.concept2.api.rowbot.profile.internal;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;

import com.concept2.api.internal.DataHolder;
import com.concept2.api.rowbot.internal.RowBotContract.ProfileColumns;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileSettings;
import com.concept2.api.utils.CalendarUtils;
import com.concept2.api.utils.Preconditions;

import java.util.Calendar;

public class ProfileRef extends DataHolder implements Profile {

    private final int mRow;
    private final ProfileSettings mProfileSettings;

    public ProfileRef(DataHolder dataHolder, int row) {
        super(dataHolder);
        mRow = row;
        mProfileSettings = new ProfileSettingsRef(dataHolder, row);
    }

    @Override
    public String getProfileId() {
        return getString(mRow, ProfileColumns.PROFILE_ID, null);
    }

    @Override
    public int getImageId() {
        return getInt(mRow, ProfileColumns.IMAGE_ID, 0);
    }

    @Override
    public String getName() {
        return getString(mRow, ProfileColumns.NAME, null);
    }

    @Override
    public String getTeamName() {
        return getString(mRow, ProfileColumns.TEAM_NAME, null);
    }

    @Override
    public int getGender() {
        return getInt(mRow, ProfileColumns.GENDER, Gender.NONE);
    }

    @Override
    public float getWeight() {
        return getFloat(mRow, ProfileColumns.WEIGHT, 0.0f);
    }

    @Override
    public Calendar getBirthday() {
        String str = getString(mRow, ProfileColumns.BIRTHDAY, null);
        return TextUtils.isEmpty(str) ? null : CalendarUtils.parse(str);
    }

    @Override
    public int getLifetimeMeters() {
        return getInt(mRow, ProfileColumns.LIFETIME_METERS, 0);
    }

    @Override
    public int getSeasonMeters() {
        return getInt(mRow, ProfileColumns.SEASON_METERS, 0);
    }

    @Override
    public ProfileSettings getProfileSettings() {
        return mProfileSettings;
    }

    public static ContentValues toDatabaseValues(Profile profile) {
        Preconditions.assertNotNull(profile);
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(profile.getProfileId())) {
            values.put(ProfileColumns.PROFILE_ID, profile.getProfileId());
        }
        values.put(ProfileColumns.IMAGE_ID, profile.getImageId());
        values.put(ProfileColumns.NAME, profile.getName());
        values.put(ProfileColumns.TEAM_NAME, profile.getTeamName());
        values.put(ProfileColumns.GENDER, profile.getGender());
        values.put(ProfileColumns.WEIGHT, profile.getWeight());
        Calendar birthday = profile.getBirthday();
        values.put(ProfileColumns.BIRTHDAY, birthday == null ? null
                : CalendarUtils.toDateString(birthday));
        values.putAll(ProfileSettingsRef.toDatabaseValues(profile.getProfileSettings()));
        return values;
    }
}

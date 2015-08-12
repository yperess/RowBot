package com.rowbot.api;

import android.content.Context;

import com.rowbot.internal.ProfileRef;
import com.rowbot.internal.ProfileSettingsRef;
import com.rowbot.model.BoatType;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileSettings;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class RowBot {

    public interface ProfileImageId {
        int ID0 = 0;
        int ID1 = 1;
        int ID2 = 2;
    }

    private static ProfileSettings sProfileSettings = new ProfileSettingsRef.Builder(null)
            .setProvideUseStatistics(true)
            .setApplyAgeAdjustment(true)
            .setApplyBoatAdjustment(false)
            .setApplyWeightAdjustment(false)
            .setBoatType(BoatType.OTHER)
            .build();

    public static Profile getProfile(Context context) {
        return new ProfileRef.Builder()
                .setImageId("image0")
                .setName("Row Bot User")
                .setTeamName("Rowing Rowing Club")
                .setGender(Profile.Gender.MALE)
                .setWeight(80.0f)
                .setBirthday(new GregorianCalendar(1984, Calendar.MAY, 8))
                .setLifetimeMeters(1301234)
                .setSeasonMeters(12345)
                .setProfileSettings(sProfileSettings)
                .build(context);
    }

    public static void updateProfile(Profile profile) {
        sProfileSettings = profile.getProfileSettings();
    }
}

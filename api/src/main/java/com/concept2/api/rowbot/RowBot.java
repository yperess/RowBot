package com.concept2.api.rowbot;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Releaseable;
import com.concept2.api.Result;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileCreator;

import java.util.List;

public interface RowBot {

    interface LoadProfilesResult extends Result, Releaseable {
        List<Profile> getProfiles();
    }

    PendingResult<LoadProfilesResult> loadProfiles(Context context, String profileId);

    PendingResult<LoadProfilesResult> createNewProfile(Context context, ProfileCreator newProfile);
    PendingResult<LoadProfilesResult> updateProfile(Context context, ProfileCreator newProfile);
}

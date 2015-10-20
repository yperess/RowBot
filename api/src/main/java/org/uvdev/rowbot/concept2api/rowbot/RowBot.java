package org.uvdev.rowbot.concept2api.rowbot;

import android.content.Context;

import org.uvdev.rowbot.concept2api.PendingResult;
import org.uvdev.rowbot.concept2api.Releaseable;
import org.uvdev.rowbot.concept2api.Result;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.concept2api.rowbot.profile.ProfileCreator;

import java.util.List;

public interface RowBot {

    interface LoadProfilesResult extends Result, Releaseable {
        List<Profile> getProfiles();
    }

    PendingResult<LoadProfilesResult> loadProfiles(Context context, String profileId);

    PendingResult<LoadProfilesResult> createNewProfile(Context context, ProfileCreator newProfile);
    PendingResult<LoadProfilesResult> updateProfile(Context context, ProfileCreator newProfile);
    PendingResult<Result> deleteProfile(Context context, String profileId);
}

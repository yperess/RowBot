package com.concept2.api.rowbot.internal;

import android.content.Context;
import android.text.TextUtils;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.rowbot.RowBot;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileCreator;
import com.concept2.api.service.Concept2AsyncTaskService;
import com.concept2.api.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class RowBotImpl implements RowBot {

    private static final class StatusPendingResult extends PendingResultImpl<Result> {
        @Override
        protected Result getFailedResult(final int statusCode) {
            return new Result() {
                @Override
                public int getStatus() {
                    return statusCode;
                }
            };
        }
    }

    private static final class LoadProfilePendingResult extends
            PendingResultImpl<LoadProfilesResult> {
        @Override
        protected LoadProfilesResult getFailedResult(final int statusCode) {
            return new LoadProfilesResult() {
                @Override
                public List<Profile> getProfiles() {
                    return new ArrayList<>();
                }

                @Override
                public void release() {}

                @Override
                public int getStatus() {
                    return statusCode;
                }
            };
        }
    }

    @Override
    public PendingResult<LoadProfilesResult> loadProfiles(Context context, String profileId) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        PendingResultImpl<LoadProfilesResult> pendingResult = new LoadProfilePendingResult();
        Concept2AsyncTaskService.loadProfiles(context, pendingResult, profileId);
        return pendingResult;
    }

    @Override
    public PendingResult<LoadProfilesResult> createNewProfile(Context context,
            ProfileCreator newProfile) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertNotNull(newProfile, "New profile cannot be null");
        PendingResultImpl<LoadProfilesResult> pendingResult = new LoadProfilePendingResult();
        Concept2AsyncTaskService.createProfile(context, pendingResult, newProfile);
        return pendingResult;
    }

    @Override
    public PendingResult<LoadProfilesResult> updateProfile(Context context,
            ProfileCreator newProfile) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertNotNull(newProfile, "New profile cannot be null");
        PendingResultImpl<LoadProfilesResult> pendingResult = new LoadProfilePendingResult();
        Concept2AsyncTaskService.updateProfile(context, pendingResult, newProfile);
        return pendingResult;
    }

    @Override
    public PendingResult<Result> deleteProfile(Context context, String profileId) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertTrue(!TextUtils.isEmpty(profileId), "Profile ID cannot be empty");
        PendingResultImpl<Result> pendingResult = new StatusPendingResult();
        Concept2AsyncTaskService.deleteProfile(context, pendingResult, profileId);
        return pendingResult;
    }
}

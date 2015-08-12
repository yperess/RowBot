package com.concept2.api.rowbot.internal;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.rowbot.RowBot;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.utils.Preconditions;

public class RowBotImpl implements RowBot {

    private static final class LoadProfilePendingResult extends
            PendingResultImpl<LoadProfileResult> {
        @Override
        protected LoadProfileResult getFailedResult(final int statusCode) {
            return new LoadProfileResult() {
                @Override
                public Profile getProfile() {
                    return null;
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
    public PendingResult<LoadProfileResult> loadProfile(Context context) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        PendingResultImpl<LoadProfileResult> pendingResult = new LoadProfilePendingResult();
        return null;
    }

    @Override
    public PendingResult<Result> updateProfile(Context context, Profile newProfile) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertNotNull(newProfile, "New profile cannot be null");
        return null;
    }
}

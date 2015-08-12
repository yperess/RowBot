package com.concept2.api.rowbot;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Releaseable;
import com.concept2.api.Result;
import com.concept2.api.rowbot.profile.Profile;

public interface RowBot {

    interface LoadProfileResult extends Result, Releaseable {
        Profile getProfile();
    }

    PendingResult<LoadProfileResult> loadProfile(Context context);

    PendingResult<Result> updateProfile(Context context, Profile newProfile);
}

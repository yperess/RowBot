package com.concept2.api.rowbot.profile.internal;

import com.concept2.api.internal.DataBufferRef;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.rowbot.RowBot;
import com.concept2.api.rowbot.profile.Profile;

import java.util.List;

public class LoadProfilesResultRef extends DataBufferRef<Profile> implements
        RowBot.LoadProfilesResult {

    public LoadProfilesResultRef(DataHolder holder) {
        super(holder);
    }

    @Override
    protected Profile getItem(DataHolder holder, int row) {
        return new ProfileRef(holder, row);
    }

    @Override
    public List<Profile> getProfiles() {
        return mResults;
    }
}

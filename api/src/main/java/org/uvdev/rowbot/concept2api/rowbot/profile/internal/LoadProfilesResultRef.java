package org.uvdev.rowbot.concept2api.rowbot.profile.internal;

import org.uvdev.rowbot.concept2api.DataBufferRef;
import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.rowbot.RowBot;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;

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

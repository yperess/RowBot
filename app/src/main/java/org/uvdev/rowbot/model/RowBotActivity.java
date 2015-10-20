package org.uvdev.rowbot.model;

import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.utils.Observable;

public interface RowBotActivity {

    Observable<Profile> CURRENT_PROFILE = new Observable<>();

    void setActionBarTitle(int resId);
}

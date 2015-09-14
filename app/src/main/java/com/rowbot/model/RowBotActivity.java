package com.rowbot.model;

import com.concept2.api.rowbot.profile.Profile;
import com.rowbot.utils.Observable;

public interface RowBotActivity {

    Observable<Profile> CURRENT_PROFILE = new Observable<>();

    void setActionBarTitle(int resId);
}

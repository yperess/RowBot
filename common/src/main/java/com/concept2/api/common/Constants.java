package com.concept2.api.common;

import com.concept2.api.common.data.Version;

public interface Constants {
    boolean DBG = true;
    String SHARED_PREF_FILE = "rowbot.pref";
    String SHARED_PREF_LAST_RUN_VERSION = "lastRunVersion";
    String SHARED_PREF_SHARING_DATA = "sharingData";
    String SHARED_PREF_USER_NAME = "userName";
    String SHARED_PREF_CLUB_NAME = "clubName";

    Version VERSION = new Version(true /* isBeta */, 0 /* majorRelease */, 1 /* minorRelease */);
}

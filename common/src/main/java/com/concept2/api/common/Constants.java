package com.concept2.api.common;

import com.concept2.api.common.data.Version;

public interface Constants {
    String SHARED_PREF_FILE = "rowbot.pref";
    String SHARED_PREF_LAST_RUN_VERSION = "lastRunVersion";

    Version VERSION = new Version(true /* isBeta */, 0 /* majorRelease */, 1 /* minorRelease */);
}

package com.concept2.api.common;

import com.concept2.api.common.data.Version;

public interface Constants {
    boolean DBG = true;
    String SHARED_PREF_FILE = "rowbot.pref";
    String SHARED_PREF_LAST_RUN_VERSION = "lastRunVersion";
    String SHARED_PREF_SELECTED_PROFILE_IDS = "selectedProfileIds";

    Version VERSION = new Version();
}

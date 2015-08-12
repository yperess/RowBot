package com.concept2.api.rowbot.profile;

import android.net.Uri;

import com.concept2.api.pacemonitor.PaceMonitor;

import java.util.Calendar;

public interface Profile {

    interface Gender extends PaceMonitor.Gender {}

    String getProfileId();
    Uri getImageUri();
    String getName();
    String getTeamName();
    int getGender();
    float getWeight();
    Calendar getBirthday();
    int getLifetimeMeters();
    int getSeasonMeters();
    ProfileSettings getProfileSettings();
}

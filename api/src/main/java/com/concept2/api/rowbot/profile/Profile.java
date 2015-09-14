package com.concept2.api.rowbot.profile;

import com.concept2.api.Releaseable;
import com.concept2.api.pacemonitor.PaceMonitor;

import java.util.Calendar;

public interface Profile extends Releaseable {

    interface Gender extends PaceMonitor.Gender {}

    String getProfileId();
    int getImageId();
    String getName();
    String getTeamName();
    int getGender();
    float getWeight();
    Calendar getBirthday();
    int getLifetimeMeters();
    int getSeasonMeters();
    ProfileSettings getProfileSettings();
}

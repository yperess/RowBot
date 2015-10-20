package org.uvdev.rowbot.concept2api.rowbot.profile;

import org.uvdev.rowbot.concept2api.Releaseable;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;

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

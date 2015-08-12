package com.concept2.api.rowbot.profile;

public interface ProfileSettings {

    interface DataResolution {
        int LOW = 0;
        int MEDIUM = 1;
        int HIGH = 2;
    }
    boolean provideUseStatistics();
    boolean applyWeightAdjustment();
    boolean applyAgeAdjustment();
    boolean applyBoatAdjustment();
    int getBoatType();
    int getDataResolution();
}

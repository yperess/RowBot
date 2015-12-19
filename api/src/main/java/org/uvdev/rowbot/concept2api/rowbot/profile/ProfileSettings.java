package org.uvdev.rowbot.concept2api.rowbot.profile;

import org.uvdev.rowbot.concept2api.Releaseable;

public interface ProfileSettings extends Releaseable {

    interface DataResolution {
        int LOW = 0;
        int MEDIUM = 1;
        int HIGH = 2;
    }

    interface BoatType {
        int UNSPECIFIED = -1;
        int SINGLE = 0;
        int PAIR = 1;
        int DOUBLE = 2;
        int FOUR = 3;
        int STRAIGHT_FOUR = 4;
        int QUAD = 5;
        int EIGHT = 6;
        int MIXED_DOUBLE = 7;
        int MIXED_FOUR = 8;
        int MIXED_STRAIGHT_FOUR = 9;
        int MIXED_QUAD = 10;
        int MIXED_EIGHT = 11;
    }
    boolean provideUseStatistics();
    boolean applyWeightAdjustment();
    boolean applyAgeAdjustment();
    boolean applyBoatAdjustment();
    int getBoatType();
    int getDataResolution();
    String getAccountName();
}

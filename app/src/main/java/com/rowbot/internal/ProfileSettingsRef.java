package com.rowbot.internal;

import com.concept2.api.rowbot.profile.ProfileSettings;

public class ProfileSettingsRef implements ProfileSettings {

    private final boolean mProvideUseStatistics;
    private final boolean mApplyWeightAdjustment;
    private final boolean mApplyAgeAdjustment;
    private final boolean mApplyBoatAdjustment;
    private final int mBoatType;

    private ProfileSettingsRef(Builder builder) {
        mProvideUseStatistics = builder.mProvideUseStatistics;
        mApplyWeightAdjustment = builder.mApplyWeightAdjustment;
        mApplyAgeAdjustment = builder.mApplyAgeAdjustment;
        mApplyBoatAdjustment = builder.mApplyBoatAdjustment;
        mBoatType = builder.mBoatType;
    }

    @Override
    public boolean provideUseStatistics() {
        return false;
    }

    @Override
    public boolean applyWeightAdjustment() {
        return false;
    }

    @Override
    public boolean applyAgeAdjustment() {
        return false;
    }

    @Override
    public boolean applyBoatAdjustment() {
        return false;
    }

    @Override
    public int getBoatType() {
        return 0;
    }

    @Override
    public int getDataResolution() {
        return 0;
    }

    public static final class Builder {

        private boolean mProvideUseStatistics;
        private boolean mApplyWeightAdjustment;
        private boolean mApplyAgeAdjustment;
        private boolean mApplyBoatAdjustment;
        private int mBoatType;

        public Builder(ProfileSettings baseSettings) {
            if (baseSettings == null) {
                return;
            }
            mProvideUseStatistics = baseSettings.provideUseStatistics();
            mApplyWeightAdjustment = baseSettings.applyWeightAdjustment();
            mApplyAgeAdjustment = baseSettings.applyAgeAdjustment();
            mApplyBoatAdjustment = baseSettings.applyBoatAdjustment();
            mBoatType = baseSettings.getBoatType();
        }

        public Builder setApplyAgeAdjustment(boolean applyAgeAdjustment) {
            mApplyAgeAdjustment = applyAgeAdjustment;
            return this;
        }

        public Builder setApplyBoatAdjustment(boolean applyBoatAdjustment) {
            mApplyBoatAdjustment = applyBoatAdjustment;
            return this;
        }

        public Builder setApplyWeightAdjustment(boolean applyWeightAdjustment) {
            mApplyWeightAdjustment = applyWeightAdjustment;
            return this;
        }

        public Builder setBoatType(int boatType) {
            mBoatType = boatType;
            return this;
        }

        public Builder setProvideUseStatistics(boolean provideUseStatistics) {
            mProvideUseStatistics = provideUseStatistics;
            return this;
        }

        public ProfileSettings build() {
            return new ProfileSettingsRef(this);
        }
    }
}

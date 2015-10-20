package org.uvdev.rowbot.concept2api.rowbot.profile;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileSettingsCreator implements ProfileSettings, Parcelable {

    private boolean mProvideUseStatistics = true;
    private boolean mApplyWeightAdjustment = false;
    private boolean mApplyAgeAdjustment = false;
    private boolean mApplyBoatAdjustment = false;
    private int mBoatType = BoatType.UNSPECIFIED;
    private int mDataResolution = DataResolution.MEDIUM;

    public ProfileSettingsCreator() {}

    public ProfileSettingsCreator(ProfileSettings base) {
        this(base.provideUseStatistics(), base.applyWeightAdjustment(), base.applyAgeAdjustment(),
                base.applyBoatAdjustment(), base.getBoatType(), base.getDataResolution());
    }

    private ProfileSettingsCreator(boolean provideUseStatistics, boolean applyWeightAdjustment,
            boolean applyAgeAdjustment, boolean applyBoatAdjustment, int boatType,
            int dataResolution) {
        mProvideUseStatistics = provideUseStatistics;
        mApplyWeightAdjustment = applyWeightAdjustment;
        mApplyAgeAdjustment = applyAgeAdjustment;
        mApplyBoatAdjustment = applyBoatAdjustment;
        mBoatType = boatType;
        mDataResolution = dataResolution;
    }

    public ProfileSettingsCreator setProvideUseStatistics(boolean provideUseStatistics) {
        mProvideUseStatistics = provideUseStatistics;
        return this;
    }

    public ProfileSettingsCreator setApplyWeightAdjustment(boolean applyWeightAdjustment) {
        mApplyWeightAdjustment = applyWeightAdjustment;
        return this;
    }

    public ProfileSettingsCreator setApplyAgeAdjustment(boolean applyAgeAdjustment) {
        mApplyAgeAdjustment = applyAgeAdjustment;
        return this;
    }

    public ProfileSettingsCreator setApplyBoatAdjustment(boolean applyBoatAdjustment) {
        mApplyBoatAdjustment = applyBoatAdjustment;
        return this;
    }

    public ProfileSettingsCreator setBoatType(int boatType) {
        mBoatType = boatType;
        return this;
    }

    public ProfileSettingsCreator setDataResolution(int dataResolution) {
        mDataResolution = dataResolution;
        return this;
    }

    @Override
    public boolean provideUseStatistics() {
        return mProvideUseStatistics;
    }

    @Override
    public boolean applyWeightAdjustment() {
        return mApplyWeightAdjustment;
    }

    @Override
    public boolean applyAgeAdjustment() {
        return mApplyAgeAdjustment;
    }

    @Override
    public boolean applyBoatAdjustment() {
        return mApplyBoatAdjustment;
    }

    @Override
    public int getBoatType() {
        return mBoatType;
    }

    @Override
    public int getDataResolution() {
        return mDataResolution;
    }

    @Override
    public void release() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(toByte(mProvideUseStatistics));
        dest.writeByte(toByte(mApplyWeightAdjustment));
        dest.writeByte(toByte(mApplyAgeAdjustment));
        dest.writeByte(toByte(mApplyBoatAdjustment));
        dest.writeInt(mBoatType);
        dest.writeInt(mDataResolution);
    }

    private static byte toByte(boolean bool) {
        return (byte) (bool ? 1 : 0);
    }

    public static final Creator<ProfileSettingsCreator> CREATOR =
            new Creator<ProfileSettingsCreator>() {
        @Override
        public ProfileSettingsCreator createFromParcel(Parcel source) {
            boolean provideUseStatistics = source.readByte() == 1;
            boolean applyWeightAdjustment = source.readByte() == 1;
            boolean applyAgeAdjustment = source.readByte() == 1;
            boolean applyBoatAdjustment = source.readByte() == 1;
            int boatType = source.readInt();
            int dataResolution = source.readInt();
            return new ProfileSettingsCreator(provideUseStatistics, applyWeightAdjustment,
                    applyAgeAdjustment, applyBoatAdjustment, boatType, dataResolution);
        }

        @Override
        public ProfileSettingsCreator[] newArray(int size) {
            return new ProfileSettingsCreator[size];
        }
    };
}

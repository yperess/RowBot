package com.concept2.api.rowbot.profile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProfileCreator implements Profile, Parcelable {

    private final String mProfileId;
    private int mImageId = 0;
    private String mName = null;
    private String mTeamName = null;
    private int mGender = Gender.NONE;
    private float mWeight = 0.0f;
    private Calendar mBirthday = null;
    private ProfileSettingsCreator mProfileSettings = new ProfileSettingsCreator();

    public ProfileCreator() {
        mProfileId = null;
    }

    public ProfileCreator(Profile base) {
        this(base.getProfileId(), base.getImageId(), base.getName(), base.getTeamName(),
                base.getGender(), base.getWeight(), base.getBirthday(), base.getProfileSettings());
    }

    private ProfileCreator(String profileId, int imageId, String name, String teamName, int gender,
            float weight, Calendar birthday, ProfileSettings profileSettings) {
        mProfileId = profileId;
        mImageId = imageId;
        mName = name;
        mTeamName = teamName;
        mGender = gender;
        mWeight = weight;
        mBirthday = birthday;
        setProfileSettings(profileSettings);
    }

    public ProfileCreator setImageId(int imageId) {
        mImageId = imageId;
        return this;
    }

    public ProfileCreator setName(String name) {
        mName = name;
        return this;
    }

    public ProfileCreator setTeamName(String teamName) {
        mTeamName = teamName;
        return this;
    }

    public ProfileCreator setGender(int gender) {
        mGender = gender;
        return this;
    }

    public ProfileCreator setWeight(float weight) {
        mWeight = weight;
        return this;
    }

    public ProfileCreator setBirthday(Calendar birthday) {
        mBirthday = birthday;
        return this;
    }

    public ProfileCreator setProfileSettings(ProfileSettings profileSettings) {
        mProfileSettings = profileSettings instanceof ProfileSettingsCreator
                ? (ProfileSettingsCreator) profileSettings
                : new ProfileSettingsCreator(profileSettings);
        return this;
    }

    @Override
    public String getProfileId() {
        return mProfileId;
    }

    @Override
    public int getImageId() {
        return mImageId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getTeamName() {
        return mTeamName;
    }

    @Override
    public int getGender() {
        return mGender;
    }

    @Override
    public float getWeight() {
        return mWeight;
    }

    @Override
    public Calendar getBirthday() {
        return mBirthday;
    }

    @Override
    public int getLifetimeMeters() {
        return 0;
    }

    @Override
    public int getSeasonMeters() {
        return 0;
    }

    @Override
    public ProfileSettings getProfileSettings() {
        return mProfileSettings;
    }

    public ProfileSettingsCreator getProfileSettingsCreator() {
        return mProfileSettings;
    }

    @Override
    public void release() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mProfileId == null ? 0 : 1));
        if (mProfileId != null) {
            dest.writeString(mProfileId);
        }
        dest.writeInt(mImageId);
        dest.writeByte((byte) (mName == null ? 0 : 1));
        if (mName != null) {
            dest.writeString(mName);
        }
        dest.writeByte((byte) (mTeamName == null ? 0 : 1));
        if (mTeamName != null) {
            dest.writeString(mTeamName);
        }
        dest.writeInt(mGender);
        dest.writeFloat(mWeight);
        dest.writeByte((byte) (mBirthday == null ? 0 : 1));
        if (mBirthday != null) {
            dest.writeInt(mBirthday.get(Calendar.DAY_OF_MONTH));
            dest.writeInt(mBirthday.get(Calendar.MONTH));
            dest.writeInt(mBirthday.get(Calendar.YEAR));
        }
        dest.writeByte((byte) (mProfileSettings == null ? 0 : 1));
        if (mProfileSettings != null) {
            dest.writeParcelable(mProfileSettings, flags);
        }

    }

    public static final Creator<ProfileCreator> CREATOR = new Creator<ProfileCreator>() {
        @Override
        public ProfileCreator createFromParcel(Parcel source) {
            String profileId = hasNext(source) ? source.readString() : null;
            int imageId = source.readInt();
            String name = hasNext(source)? source.readString() : null;
            String teamName = hasNext(source) ? source.readString() : null;
            int gender = source.readInt();
            float weight = source.readFloat();
            Calendar birthday = null;
            if (hasNext(source)) {
                int day = source.readInt();
                int month = source.readInt();
                int year = source.readInt();
                birthday = new GregorianCalendar(year, month, day);
            }
            ProfileSettingsCreator profileSettings = (ProfileSettingsCreator) (hasNext(source)
                    ? source.readParcelable(ProfileSettingsCreator.class.getClassLoader()) : null);
            return new ProfileCreator(profileId, imageId, name, teamName, gender, weight, birthday,
                    profileSettings);
        }

        @Override
        public ProfileCreator[] newArray(int size) {
            return new ProfileCreator[size];
        }

        private boolean hasNext(Parcel source) {
            return source.readByte() == 1;
        }
    };
}

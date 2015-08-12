package com.rowbot.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.rowbot.R;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileSettings;

import java.util.Calendar;
import java.util.HashMap;

public final class ProfileRef implements Profile {
    private static HashMap<String, Integer> sImageMap = new HashMap<>();
    static {
        sImageMap.put("image0", R.drawable.stock_avatar_0);
        sImageMap.put("image1", R.drawable.stock_avatar_1);
        sImageMap.put("image2", R.drawable.stock_avatar_2);
    }

    public static Uri ResourceToUri (Context context,int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }

    private final Uri mImageUri;
    private final String mName;
    private final String mTeamName;
    private final int mGender;
    private final float mWeight;
    private final Calendar mBirthday;
    private final int mLifetimeMeters;
    private final int mSeasonMeters;
    private final ProfileSettings mProfileSettings;

    private ProfileRef(Builder builder, Context context) {
        mImageUri = ResourceToUri(context, sImageMap.get(builder.mImageId));
        mName = builder.mName;
        mTeamName = builder.mTeamName;
        mGender = builder.mGender;
        mWeight = builder.mWeight;
        mBirthday = builder.mBirthday;
        mLifetimeMeters = builder.mLifetimeMeters;
        mSeasonMeters = builder.mSeasonMeters;
        mProfileSettings = builder.mProfileSettings;
    }

    public String getProfileId() {
        return null;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public String getName() {
        return mName;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getGender() {
        return mGender;
    }

    public float getWeight() {
        return mWeight;
    }

    public Calendar getBirthday() {
        return mBirthday;
    }

    public int getLifetimeMeters() {
        return mLifetimeMeters;
    }

    public int getSeasonMeters() {
        return mSeasonMeters;
    }

    public ProfileSettings getProfileSettings() {
        return mProfileSettings;
    }

    public static final class Builder {

        private String mImageId;
        private String mName;
        private String mTeamName;
        private int mGender;
        private float mWeight;
        private Calendar mBirthday;
        private int mLifetimeMeters;
        private int mSeasonMeters;
        private ProfileSettings mProfileSettings;

        public Builder setImageId(String imageId) {
            mImageId = imageId;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setTeamName(String teamName) {
            mTeamName = teamName;
            return this;
        }

        public Builder setGender(int gender) {
            mGender = gender;
            return this;
        }

        public Builder setWeight(float weight) {
            mWeight = weight;
            return this;
        }

        public Builder setBirthday(Calendar birthday) {
            mBirthday = birthday;
            return this;
        }

        public Builder setLifetimeMeters(int lifetimeMeters) {
            mLifetimeMeters = lifetimeMeters;
            return this;
        }

        public Builder setSeasonMeters(int seasonMeters) {
            mSeasonMeters = seasonMeters;
            return this;
        }

        public Builder setProfileSettings(ProfileSettings profileSettings) {
            mProfileSettings = profileSettings;
            return this;
        }

        public ProfileRef build(Context context) {
            return new ProfileRef(this, context);
        }
    }
}

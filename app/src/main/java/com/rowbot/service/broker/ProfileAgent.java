package com.rowbot.service.broker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

import com.concept2.api.internal.DataHolder;
import com.rowbot.R;
import com.rowbot.api.RowBot.ProfileImageId;
import com.rowbot.api.RowBotStatusCodes;
import com.rowbot.service.broker.RowBotContract.ProfileColumns;
import com.concept2.api.rowbot.profile.Profile;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ProfileAgent {

    private static final String SHARED_PREFS_FILE = "RowBot.pref";

    private static final HashMap<Integer, Integer> sProfileImageMap = new HashMap<>();

    static {
        sProfileImageMap.put(ProfileImageId.ID0, R.drawable.stock_avatar_0);
        sProfileImageMap.put(ProfileImageId.ID1, R.drawable.stock_avatar_1);
        sProfileImageMap.put(ProfileImageId.ID2, R.drawable.stock_avatar_2);
    }

    private final ReentrantLock mSharedPrefsLock = new ReentrantLock();

    public DataHolder loadProfile(Context context) {
        DataHolder holder = DataHolder.empty(RowBotStatusCodes.INTERNAL_ERROR);
        mSharedPrefsLock.lock();
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE);
        int imageId = prefs.getInt(ProfileColumns.IMAGE_ID, ProfileImageId.ID0);
        String name = prefs.getString(ProfileColumns.NAME, null);
        String teamName = prefs.getString(ProfileColumns.TEAM_NAME, null);
        int gender = prefs.getInt(ProfileColumns.GENDER, Profile.Gender.NONE);
        float weight = prefs.getFloat(ProfileColumns.WEIGHT, 0.0f);
        int birthDay = prefs.getInt(ProfileColumns.BIRTH_DAY, 0);
        int birthMonth = prefs.getInt(ProfileColumns.BIRTH_MONTH, 0);
        int birthYear = prefs.getInt(ProfileColumns.BIRTH_YEAR, 0);

        // Get image URI.
        Uri imageUri = resourceToUri(context, sProfileImageMap.get(imageId));

        ContentValues values = new ContentValues();
        values.put(ProfileColumns.IMAGE_URI, imageUri.toString());
        values.put(ProfileColumns.NAME, name);
        values.put(ProfileColumns.TEAM_NAME, teamName);
        values.put(ProfileColumns.GENDER, gender);
        values.put(ProfileColumns.WEIGHT, weight);
        values.put(ProfileColumns.BIRTH_DAY, birthDay);
        values.put(ProfileColumns.BIRTH_MONTH, birthMonth);
        values.put(ProfileColumns.BIRTH_YEAR, birthYear);
        holder = new DataHolder.Builder()
                .withValues(values)
                .build(RowBotStatusCodes.OK);
        mSharedPrefsLock.unlock();

        return holder;
    }

    private Uri resourceToUri(Context context, int resId) {
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                res.getResourcePackageName(resId) + '/' +
                res.getResourceTypeName(resId) + '/' +
                res.getResourceEntryName(resId));
    }
}

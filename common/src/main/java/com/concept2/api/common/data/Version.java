package com.concept2.api.common.data;

import android.text.TextUtils;

import com.concept2.api.common.utils.Preconditions;

public class Version implements Comparable<Version> {

    private static final String VERSION_NAME_REGEX = "([ABab]\\.)?\\d+\\.\\d+";

    private final String mVersionName;
    private final int mVersionCode;

    private final boolean mIsAlpha;
    private final boolean mIsBeta;

    public Version(String versionName, int versionCode) {
        Preconditions.assertTrue(!TextUtils.isEmpty(versionName));
        String[] versionBits = versionName.split("\\.");
        Preconditions.assertTrue(versionName.matches(VERSION_NAME_REGEX));

        if (versionBits.length == 3) {
            mIsAlpha = versionBits[0].equalsIgnoreCase("a");
            mIsBeta = versionBits[0].equalsIgnoreCase("b");
        } else {
            mIsAlpha = mIsBeta = false;
        }
        mVersionName = versionName;
        mVersionCode = versionCode;
    }

    public boolean isProd() {
        return !(mIsAlpha || mIsBeta);
    }

    public boolean isAlpha() {
        return mIsAlpha;
    }

    public boolean isBeta() {
        return mIsBeta;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version other = (Version) obj;
            return mVersionCode == other.mVersionCode;
        } else if (obj instanceof String) {
            return this.toString().equals(obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return mVersionName;
    }

    @Override
    public int compareTo(Version another) {
        return this.mVersionCode - another.mVersionCode;
    }
}
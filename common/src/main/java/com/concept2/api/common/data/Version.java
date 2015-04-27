package com.concept2.api.common.data;

import android.text.TextUtils;

import com.concept2.api.common.utils.Preconditions;

public class Version implements Comparable<Version> {

    private final boolean mIsBeta;
    private final int mMajorRelease;
    private final int mMinorRelease;

    private final String mStringRepresentation;

    public Version(boolean isBeta, int majorRelease, int minorRelease) {
        mIsBeta = isBeta;
        mMajorRelease = majorRelease;
        mMinorRelease = minorRelease;
        mStringRepresentation = new StringBuilder()
                .append(mIsBeta ? 0 : 1)
                .append(".")
                .append(mMajorRelease)
                .append(".")
                .append(mMinorRelease)
                .toString();
    }

    public Version(String version) {
        Preconditions.assertTrue(!TextUtils.isEmpty(version));
        String[] versionBits = version.split("\\.");
        Preconditions.assertEqual(3, versionBits.length);

        mIsBeta = versionBits[0].equals("0");
        mMajorRelease = Integer.parseInt(versionBits[1]);
        mMinorRelease = Integer.parseInt(versionBits[2]);
        mStringRepresentation = version;
    }

    public boolean isBeta() {
        return mIsBeta;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version other = (Version) obj;
            return mIsBeta == other.mIsBeta
                    && mMajorRelease == other.mMajorRelease
                    && mMinorRelease == other.mMinorRelease;
        } else if (obj instanceof String) {
            return this.toString().equals(obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return mStringRepresentation;
    }

    @Override
    public int compareTo(Version another) {
        if (this.mIsBeta && !another.mIsBeta) {
            // This version is older since its a beta release and the other isn't.
            return -1;
        } else if (!this.mIsBeta && another.mIsBeta) {
            // This version is newer since its not a beta release and the other is.
            return 1;
        } else if (this.mMajorRelease < another.mMajorRelease) {
            // This version is older since its major release is lower.
            return -1;
        } else if (this.mMajorRelease > another.mMajorRelease) {
            // This version is newer since its major release is higher.
            return 1;
        } else if (this.mMinorRelease < another.mMinorRelease) {
            // This version is older since its minor release is lower.
            return -1;
        } else if (this.mMinorRelease > another.mMinorRelease) {
            // This version is newer since its minor release is higher.
            return 1;
        }
        // All version bits are the same.
        return 0;
    }
}
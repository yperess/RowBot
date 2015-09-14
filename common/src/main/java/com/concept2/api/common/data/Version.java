package com.concept2.api.common.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.concept2.api.common.Constants;
import com.concept2.api.common.utils.Preconditions;

public class Version implements Comparable<Version> {

    private String mVersionName;
    private int mVersionCode;

    private boolean mIsBeta;

    public Version() {
        mVersionName = "";
        mVersionCode = 0;
        mIsBeta = false;
    }

    public Version(String versionName, int versionCode) {
        init(versionName, versionCode);
    }

    public void update(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            init(info.versionName, info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {}
    }

    private void init(String versionName, int versionCode) {
        Preconditions.assertTrue(!TextUtils.isEmpty(versionName));
        String[] versionBits = versionName.split("\\.");
        Preconditions.assertEqual(3, versionBits.length);

        mIsBeta = versionBits[0].equals("1");
        mVersionName = versionName;
        mVersionCode = versionCode;
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
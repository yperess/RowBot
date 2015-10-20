package org.uvdev.rowbot;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.concept2.api.common.data.Version;

public class RowBotApplication extends Application {

    private Version mCurrentVersion = null;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            mCurrentVersion = new Version(info.versionName, info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {}
    }

    public Version getCurrentVersion() {
        return mCurrentVersion;
    }
}

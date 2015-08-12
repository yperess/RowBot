package com.rowbot.service.broker;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBroker {

    private static final Object sInstanceLock = new Object();
    private static DataBroker sInstance = null;

    public static DataBroker getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new DataBroker(context);
            }
            return sInstance;
        }
    }

    private final SQLiteOpenHelper mDatabaseHelper;

    private DataBroker(Context context) {
        mDatabaseHelper = new RowBotDatabaseHelper(context);
    }
}

package com.rowbot.service.broker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RowBotDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RowBot.db";
    private static final int VERSION = 1;

    public RowBotDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null /* factory */, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

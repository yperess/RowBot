package com.concept2.api.service.broker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import com.concept2.api.Concept2;
import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.rowbot.internal.RowBotContract;
import com.concept2.api.rowbot.internal.RowBotContract.ProfileColumns;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.internal.ProfileRef;
import com.concept2.api.service.database.RowBotDatabaseHelper;
import com.concept2.api.service.database.RowBotDatabaseHelper.Tables;

import java.util.UUID;

public class RowBotAgent {

    public DataHolder loadProfiles(Context context, String profileId) {
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.isEmpty(profileId)) {
            selection = ProfileColumns.PROFILE_ID + "=?";
            selectionArgs = new String[] { profileId };
        }
        SQLiteQueryBuilder query = new SQLiteQueryBuilder();
        query.setTables(Tables.PROFILES);
        Cursor cursor = query.query(getReadableDatabase(context), ProfileColumns.ALL_COLUMNS,
                selection, selectionArgs, null /* groupBy */, null /* having */,
                null /* sortOrder */);
        return new DataHolder(Concept2StatusCodes.OK, cursor);
    }

    public DataHolder createProfile(Context context, Profile profile) {
        ContentValues values = ProfileRef.toDatabaseValues(profile);
        // Create new random ID if none exists.
        if (!values.containsKey(ProfileColumns.PROFILE_ID)) {
            values.put(ProfileColumns.PROFILE_ID, UUID.randomUUID().toString());
        }
        long id = getWritableDatabase(context).insert(Tables.PROFILES, null /* columnHack */,
                values);
        if (id == -1) {
            return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR);
        }
        SQLiteQueryBuilder query = new SQLiteQueryBuilder();
        query.setTables(Tables.PROFILES);
        Cursor cursor = query.query(getReadableDatabase(context),
                ProfileColumns.ALL_COLUMNS, ProfileColumns._ID + "=?",
                new String[]{String.valueOf(id)}, null /* groupBy */, null /* having */,
                null /* sortOrder */);
        return new DataHolder(Concept2StatusCodes.OK, cursor);
    }

    public DataHolder updateProfile(Context context, Profile profile) {
        ContentValues values = ProfileRef.toDatabaseValues(profile);
        int rowsAffected = getWritableDatabase(context).update(Tables.PROFILES, values,
                ProfileColumns.PROFILE_ID + "=?", new String[]{profile.getProfileId()});
        if (rowsAffected == 0) {
            return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR);
        }
        SQLiteQueryBuilder query = new SQLiteQueryBuilder();
        query.setTables(Tables.PROFILES);
        Cursor cursor = query.query(getReadableDatabase(context),
                ProfileColumns.ALL_COLUMNS, ProfileColumns.PROFILE_ID + "=?",
                new String[] { profile.getProfileId() }, null /* groupBy */, null /* having */,
                null /* sortOrder */);
        return new DataHolder(Concept2StatusCodes.OK, cursor);
    }

    public int deleteProfile(Context context, String profileId) {
        int rowsAffected = getWritableDatabase(context).delete(Tables.PROFILES,
                ProfileColumns.PROFILE_ID + "=?", new String[]{profileId});
        return rowsAffected == 1 ? Concept2StatusCodes.OK : Concept2StatusCodes.INTERNAL_ERROR;
    }

    private final SQLiteDatabase getReadableDatabase(Context context) {
        return RowBotDatabaseHelper.getInstance(context).getReadableDatabase();
    }

    private final SQLiteDatabase getWritableDatabase(Context context) {
        return RowBotDatabaseHelper.getInstance(context).getWritableDatabase();
    }
}

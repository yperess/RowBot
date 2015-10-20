package org.uvdev.rowbot.concept2api.service.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.uvdev.rowbot.concept2api.rowbot.internal.RowBotContract.IntervalColumns;
import org.uvdev.rowbot.concept2api.rowbot.internal.RowBotContract.ProfileColumns;
import org.uvdev.rowbot.concept2api.rowbot.internal.RowBotContract.SnapshotColumns;
import org.uvdev.rowbot.concept2api.rowbot.internal.RowBotContract.WorkoutColumns;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.concept2api.rowbot.profile.ProfileSettings;

public class RowBotDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FILE_NAME = "RowBot.db";
    private static final int VERSION = 1;

    private static RowBotDatabaseHelper sInstance = null;

    public static synchronized RowBotDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RowBotDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public interface Tables {
        String PROFILES = "profiles";
        String SNAPSHOTS = "snapshots";
        String INTERVALS = "intervals";
        String WORKOUTS = "workouts";
    }

    private interface VersionBuilder {
        void onUpgrade(SQLiteDatabase db);
    }

    private RowBotDatabaseHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null /* factory */, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder();
        // Create the PROFILES table.
        query.append("CREATE TABLE ").append(Tables.PROFILES).append(" (")
                .append(ProfileColumns._ID).append(" INTEGER PRIMARY KEY,")
                .append(ProfileColumns.PROFILE_ID).append(" TEXT UNIQUE,")
                .append(ProfileColumns.IMAGE_ID).append(" INTEGER NOT NULL DEFAULT 0,")
                .append(ProfileColumns.NAME).append(" TEXT,")
                .append(ProfileColumns.TEAM_NAME).append(" TEXT,")
                .append(ProfileColumns.GENDER).append(" INTEGER NOT NULL DEFAULT ")
                        .append(Profile.Gender.NONE).append(",")
                .append(ProfileColumns.WEIGHT).append(" REAL,")
                .append(ProfileColumns.BIRTHDAY).append(" TEXT,")
                .append(ProfileColumns.PROVIDE_USE_STATISTICS)
                        .append(" INTEGER NOT NULL DEFAULT 1,")
                .append(ProfileColumns.APPLY_WEIGHT_ADJUSTMENT)
                        .append(" INTEGER NOT NULL DEFAULT 0,")
                .append(ProfileColumns.APPLY_AGE_ADJUSTMENT).append(" INTEGER NOT NULL DEFAULT 0,")
                .append(ProfileColumns.APPLY_BOAT_ADJUSTMENT).append(" INTEGER NOT NULL DEFAULT 0,")
                .append(ProfileColumns.BOAT_TYPE).append(" INTEGER NOT NULL DEFAULT ")
                        .append(ProfileSettings.BoatType.UNSPECIFIED).append(",")
                .append(ProfileColumns.DATA_RESOLUTION).append(" INTEGER NOT NULL DEFAULT ")
                        .append(ProfileSettings.DataResolution.MEDIUM)
                .append(");");
        // Create the WORKOUTS table.
        query.append("CREATE TABLE ").append(Tables.WORKOUTS).append(" (")
                .append(WorkoutColumns._ID).append(" INTEGER PRIMARY KEY,")
                .append(WorkoutColumns.PROFILE_ID).append(" INTEGER NOT NULL,")
                .append(WorkoutColumns.DATE).append(" TEXT NOT NULL")
                .append(");");
        // Create the INTERVALS table.
        query.append("CREATE TABLE ").append(Tables.INTERVALS).append(" (")
                .append(IntervalColumns._ID).append(" INTEGER PRIMARY KEY,")
                .append(IntervalColumns.WORKOUT_ID).append(" INTEGER NOT NULL,")
                .append(IntervalColumns.REST_DISTANCE).append(" INTEGER NOT NULL,")
                .append(IntervalColumns.REST_TIME).append(" INTEGER NOT NULL,")
                .append("FOREIGN KEY(").append(IntervalColumns.WORKOUT_ID).append(") REFERENCES ")
                        .append(Tables.WORKOUTS).append("(").append(WorkoutColumns._ID).append(")")
                .append(");");
        // Create the SNAPSHOTS table.
        query.append("CREATE TABLE ").append(Tables.SNAPSHOTS).append(" (")
                .append(SnapshotColumns._ID).append(" INTEGER PRIMARY KEY,")
                .append(SnapshotColumns.INTERVAL_ID).append(" INTEGER NOT NULL,")
                .append(SnapshotColumns.DISTANCE).append(" INTEGER NOT NULL,")
                .append(SnapshotColumns.TIME).append(" INTEGER NOT NULL,")
                .append(SnapshotColumns.STROKE_RATE).append(" REAL NOT NULL,")
                .append("FOREIGN KEY(").append(SnapshotColumns.INTERVAL_ID).append(") REFERENCES ")
                        .append(Tables.INTERVALS).append("(")
                        .append(IntervalColumns._ID).append(")")
                .append(");");

        // Build the database.
        db.execSQL(query.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

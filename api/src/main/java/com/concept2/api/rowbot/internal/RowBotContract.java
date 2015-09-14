package com.concept2.api.rowbot.internal;

import android.provider.BaseColumns;

public interface RowBotContract {

    interface ProfileColumns extends BaseColumns {
        String PROFILE_ID = "profile_id";
        String IMAGE_ID = "image_id";
        String NAME = "name";
        String TEAM_NAME = "team_name";
        String GENDER = "gender";
        String WEIGHT = "weight";
        String BIRTHDAY = "birthday"; // UTC yyy-MM-dd

        String LIFETIME_METERS = "lifetime_meters"; // Inferred from workouts (not stored in DB).
        String SEASON_METERS = "season_meters"; // Inferred from workouts (not stored in DB).

        // Settings
        String PROVIDE_USE_STATISTICS = "provide_use_statistics";
        String APPLY_WEIGHT_ADJUSTMENT = "apply_weight_adjustment";
        String APPLY_AGE_ADJUSTMENT = "apply_age_adjustment";
        String APPLY_BOAT_ADJUSTMENT = "apply_boat_adjustment";
        String BOAT_TYPE = "boat_type";
        String DATA_RESOLUTION = "data_resolution";

        String[] ALL_COLUMNS = new String[] {
                PROFILE_ID,
                IMAGE_ID,
                NAME,
                TEAM_NAME,
                GENDER,
                WEIGHT,
                BIRTHDAY,
                "'0' AS " + LIFETIME_METERS,
                "'0' AS " + SEASON_METERS,
                PROVIDE_USE_STATISTICS,
                APPLY_WEIGHT_ADJUSTMENT,
                APPLY_AGE_ADJUSTMENT,
                APPLY_BOAT_ADJUSTMENT,
                BOAT_TYPE,
                DATA_RESOLUTION,
        };
    }

    interface SnapshotColumns extends BaseColumns {
        String DISTANCE = "distance";
        String TIME = "time";
        String STROKE_RATE = "stroke_rate";
        String INTERVAL_ID = "interval_id";

        String[] ALL_COLUMNS = new String[] {
                DISTANCE,
                TIME,
                STROKE_RATE,
                INTERVAL_ID,
        };
    }

    interface IntervalColumns extends BaseColumns {
        String REST_TIME = "rest_time";
        String REST_DISTANCE = "rest_distance";
        String WORKOUT_ID = "workout_id";

        String[] ALL_COLUMNS = new String[] {
                REST_TIME,
                REST_DISTANCE,
                WORKOUT_ID,
        };
    }

    interface WorkoutColumns extends BaseColumns {
        String DATE = "date"; // UTC yyy-MM-dd
        String PROFILE_ID = "profile_id";

        String[] ALL_COLUMNS = new String[] {
                DATE,
                PROFILE_ID,
        };
    }
}

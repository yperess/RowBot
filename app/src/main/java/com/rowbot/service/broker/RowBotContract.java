package com.rowbot.service.broker;

import android.provider.BaseColumns;

public interface RowBotContract {

    interface ProfileColumns {
        String IMAGE_ID = "image_id";
        String IMAGE_URI = "image_uri";
        String NAME = "name";
        String TEAM_NAME = "team_name";
        String GENDER = "gender";
        String WEIGHT = "weight";
        String BIRTH_DAY = "birth_day";
        String BIRTH_MONTH = "birth_month";
        String BIRTH_YEAR = "birth_year";

        String[] ALL_COLUMNS = new String[] {
                IMAGE_ID,
                IMAGE_URI,
                NAME,
                TEAM_NAME,
                GENDER,
                WEIGHT,
                BIRTH_DAY,
                BIRTH_MONTH,
                BIRTH_YEAR,
        };
    }

    interface ProfileSettingsColumns {
        String PROVIDE_USE_STATISTICS = "provide_use_statistics";
        String APPLY_WEIGHT_ADJUSTMENT = "apply_weight_adjustment";
        String APPLY_AGE_ADJUSTMENT = "apply_age_adjustment";
        String APPLY_BOAT_ADJUSTMENT = "apply_boat_adjustment";
        String BOAT_TYPE = "boat_type";
        String DATA_RESOLUTION = "data_resolution";
    }

    interface SnapshotColumns extends BaseColumns{
        String DISTANCE = "distance";
        String TIME = "time";
        String STROKE_RATE = "stroke_rate";
        String INTERVAL_ID = "interval_id";
    }

    interface IntervalColumns extends BaseColumns {
        String REST_TIME = "rest_time";
        String REST_DISTANCE = "rest_distance";
        String WORKOUT_ID = "workout_id";
    }

    interface WorkoutColumns extends BaseColumns {
        String DATE = "date";
    }
}

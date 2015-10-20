package org.uvdev.rowbot.model;

import java.util.Calendar;

public interface PersonalRecord {
    int TYPE_FIXED_DISTANCE = 0;
    int TYPE_FIXED_TIME = 1;

    int getRecordType();
    int getIconResId();
    long getValue();
    int getStrokeRate();
    Calendar getDate();
}

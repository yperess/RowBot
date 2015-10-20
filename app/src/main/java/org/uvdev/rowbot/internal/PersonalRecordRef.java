package org.uvdev.rowbot.internal;

import org.uvdev.rowbot.model.PersonalRecord;

import java.util.Calendar;

public class PersonalRecordRef implements PersonalRecord {
    private final int mRecordType;
    private final int mIconResId;
    private final long mValue;
    private final int mStrokeRate;
    private final Calendar mDate;

    public PersonalRecordRef(int recordType, int iconResId, long value, int strokeRate,
            Calendar date) {
        mRecordType = recordType;
        mIconResId = iconResId;
        mValue = value;
        mStrokeRate = strokeRate;
        mDate = date;
    }

    @Override
    public int getRecordType() {
        return mRecordType;
    }

    @Override
    public int getIconResId() {
        return mIconResId;
    }

    @Override
    public long getValue() {
        return mValue;
    }

    @Override
    public int getStrokeRate() {
        return mStrokeRate;
    }

    @Override
    public Calendar getDate() {
        return mDate;
    }
}

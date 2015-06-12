package com.concept2.api.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.concept2.api.Result;
import com.concept2.api.utils.Preconditions;

/**
 * Holder for generic data via either {@link ContentValues} or {@link Cursor}.
 */
public class DataHolder implements Result {

    /**
     * Create an empty data holder for a given status code.
     *
     * @param statusCode The status code to use for the empty data holder.
     * @return {@link DataHolder} with no data, just a status code.
     */
    public static DataHolder empty(int statusCode) {
        return new DataHolder(statusCode, new ContentValues());
    }

    /** The status code of the data. */
    private final int mStatusCode;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mContentValues} or {@link #mCursor} may be set.
     */
    private final ContentValues mContentValues;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mContentValues} or {@link #mCursor} may be set.
     */
    private final Cursor mCursor;

    /**
     * Copy a {@link DataHolder}.
     *
     * @param dataHolder The {@link DataHolder} to copy.
     */
    public DataHolder(DataHolder dataHolder) {
        this(dataHolder.mStatusCode, dataHolder.mContentValues, dataHolder.mCursor);
    }

    /**
     * Create a {@link ContentValues} {@link DataHolder}.
     *
     * @param statusCode The status code of the holder.
     * @param contentValues The values to hold.
     */
    public DataHolder(int statusCode, ContentValues contentValues) {
        this(statusCode, contentValues, null /* cursor */);
    }

    /**
     * Create a {@link Cursor} {@link DataHolder}. Users must call {@link #release()} when done with
     * the data.
     *
     * @param statusCode The status code of the holder.
     * @param cursor The values to hold.
     */
    public DataHolder(int statusCode, Cursor cursor) {
        this(statusCode, null /* contentValues */, cursor);
    }

    /**
     * Main constructor, must only use one of {@link ContentValues} or {@link Cursor}.
     *
     * @param statusCode The status code of the holder.
     * @param contentValues The values to hold or null.
     * @param cursor The values to hold or null.
     */
    private DataHolder(int statusCode, ContentValues contentValues, Cursor cursor) {
        Preconditions.assertTrue(contentValues == null || cursor == null);
        mStatusCode = statusCode;
        mContentValues = contentValues;
        mCursor = cursor;
    }

    @Override
    public int getStatus() {
        return mStatusCode;
    }

    /**
     * Release the data in this holder.
     */
    public void release() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    /**
     * Tests if a column exists in this holder.
     *
     * @param columnName The column name.
     * @return True of the column exists, false otherwise.
     */
    protected boolean hasColumn(String columnName) {
        if (mContentValues != null && mContentValues.containsKey(columnName)) return true;
        if (mCursor != null && mCursor.getColumnIndex(columnName) != -1) return true;
        return false;
    }

    /**
     * Gets an integer value from this holder.
     *
     * @param columnName The name of the column containing the integer value.
     * @param defaultValue The value to return if the column did not exist or did not contain an
     *            integer.
     * @return The integer stored in this holder under the specified column or the default value if
     *         the value wasn't found.
     */
    protected int getInt(String columnName, int defaultValue) {
        try {
            Integer val;
            if (mContentValues != null) {
                val = mContentValues.getAsInteger(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getInt(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

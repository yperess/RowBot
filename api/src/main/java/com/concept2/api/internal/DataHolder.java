package com.concept2.api.internal;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.concept2.api.Result;
import com.concept2.api.utils.Preconditions;

/**
 * Holder for generic data via either {@link Bundle} or {@link Cursor}.
 */
public class DataHolder implements Result {

    /**
     * Create an empty data holder for a given status code.
     *
     * @param statusCode The status code to use for the empty data holder.
     * @return {@link DataHolder} with no data, just a status code.
     */
    public static DataHolder empty(int statusCode) {
        return new DataHolder(statusCode, new Bundle());
    }

    /** The status code of the data. */
    private final int mStatusCode;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mBundle} or {@link #mCursor} may be set.
     */
    private final Bundle mBundle;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mBundle} or {@link #mCursor} may be set.
     */
    private final Cursor mCursor;

    /**
     * Copy a {@link DataHolder}.
     *
     * @param dataHolder The {@link DataHolder} to copy.
     */
    public DataHolder(DataHolder dataHolder) {
        this(dataHolder.mStatusCode, dataHolder.mBundle, dataHolder.mCursor);
    }

    /**
     * Create a {@link Bundle} {@link DataHolder}.
     *
     * @param statusCode The status code of the holder.
     * @param bundle The values to hold.
     */
    public DataHolder(int statusCode, Bundle bundle) {
        this(statusCode, bundle, null /* cursor */);
    }

    /**
     * Create a {@link Cursor} {@link DataHolder}. Users must call {@link #release()} when done with
     * the data.
     *
     * @param statusCode The status code of the holder.
     * @param cursor The values to hold.
     */
    public DataHolder(int statusCode, Cursor cursor) {
        this(statusCode, null /* bundle */, cursor);
    }

    /**
     * Main constructor, must only use one of {@link Bundle} or {@link Cursor}.
     *
     * @param statusCode The status code of the holder.
     * @param bundle The values to hold or null.
     * @param cursor The values to hold or null.
     */
    private DataHolder(int statusCode, Bundle bundle, Cursor cursor) {
        Preconditions.assertTrue(bundle == null || cursor == null);
        mStatusCode = statusCode;
        mBundle = bundle;
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
        if (mBundle != null && mBundle.containsKey(columnName)) return true;
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
    protected final int getInt(String columnName, int defaultValue) {
        try {
            Integer val;
            if (mBundle != null) {
                val = mBundle.containsKey(columnName) ? mBundle.getInt(columnName) : null;
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getInt(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets a long value from this holder.
     *
     * @param columnName The name of the column containing the long value.
     * @param defaultValue The value to return if the column did not exist or did not contain a
     *            long.
     * @return The long sored in this holder under the specified column or the default value if the
     *         value wasn't found.
     */
    protected final long getLong(String columnName, long defaultValue) {
        try {
            Long val;
            if (mBundle != null) {
                val = mBundle.containsKey(columnName) ? mBundle.getLong(columnName) : null;
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getLong(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets a double value from this holder.
     *
     * @param columnName The name of the column containing the double value.
     * @param defaultValue The value to return if the column did not exist or did not contain a
     *            double.
     * @return The double stored in this holder under the specified column or the default value if
     *         the value wasn't found.
     */
    protected final double getDouble(String columnName, double defaultValue) {
        try {
            Double val;
            if (mBundle != null) {
                val = mBundle.containsKey(columnName) ? mBundle.getDouble(columnName) : null;
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getDouble(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets an integer array value from this holder.
     *
     * @param columnName The name of the column containing the int array value.
     * @param defaultValue The value to return if the column did not exist or did not contain an
     *            int array.
     * @return The int array stored in this holder under the specified column or the default value
     *         if the value wasn't found.
     */
    protected final int[] getIntArray(String columnName, int[] defaultValue) {
        try {
            int[] val;
            if (mBundle != null) {
                val = mBundle.containsKey(columnName) ? mBundle.getIntArray(columnName) : null;
            } else {
                int column = mCursor.getColumnIndex(columnName);
                String csv = column == -1 ? null : mCursor.getString(column);
                if (!TextUtils.isEmpty(csv)) {
                    String[] values = csv.split(",");
                    val = new int[values.length];
                    for (int i = 0; i < val.length; ++i) {
                        val[i] = Integer.parseInt(values[i]);
                    }
                } else {
                    val = null;
                }
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

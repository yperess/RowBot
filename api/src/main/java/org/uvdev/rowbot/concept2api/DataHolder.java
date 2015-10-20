package org.uvdev.rowbot.concept2api;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import org.uvdev.rowbot.concept2api.utils.Preconditions;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Holder for generic data via either {@link Bundle} or {@link Cursor}.
 */
public class DataHolder implements Result, Releaseable {

    /**
     * Create an empty data holder for a given status code.
     *
     * @param statusCode The status code to use for the empty data holder.
     * @return {@link DataHolder} with no data, just a status code.
     */
    public static DataHolder empty(int statusCode) {
        return new DataHolder(statusCode, new ArrayList<ContentValues>(), null /* cursor */);
    }

    /** The status code of the data. */
    private final int mStatusCode;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mValues} or {@link #mCursor} may be set.
     */
    private final ArrayList<ContentValues> mValues;

    /**
     * One of two possible ways to store data in the {@link DataHolder}. Only one of
     * {@link #mValues} or {@link #mCursor} may be set.
     */
    private final Cursor mCursor;

    /**
     * Copy a {@link DataHolder}.
     *
     * @param dataHolder The {@link DataHolder} to copy.
     */
    public DataHolder(DataHolder dataHolder) {
        this(dataHolder.mStatusCode, dataHolder.mValues, dataHolder.mCursor);
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
     * @param values The values to hold or null.
     * @param cursor The values to hold or null.
     */
    private DataHolder(int statusCode, ArrayList<ContentValues> values, Cursor cursor) {
        Preconditions.assertTrue(values == null || cursor == null);
        mStatusCode = statusCode;
        mValues = values;
        mCursor = cursor;
    }

    @Override
    public int getStatus() {
        return mStatusCode;
    }

    /**
     * Release the data in this holder.
     */
    @Override
    public void release() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    /**
     * @return The number of rows contained by this holder.
     */
    public int getCount() {
        if (mValues != null) {
            return mValues.size();
        } else if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    private final boolean containsRow(int row) {
        if (mValues != null) {
            return row >= 0 && row < mValues.size();
        } else {
            return mCursor.moveToPosition(row);
        }
    }

    protected final boolean getBoolean(int row, String columnName, boolean defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            Boolean val;
            if (mValues != null) {
                val = mValues.get(row).getAsBoolean(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getInt(column) == 1;
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets an integer value from this holder.
     *
     * @param row The row in the data holder to look at.
     * @param columnName The name of the column containing the integer value.
     * @param defaultValue The value to return if the column did not exist or did not contain an
     *            integer.
     * @return The integer stored in this holder under the specified column or the default value if
     *         the value wasn't found.
     */
    protected final int getInt(int row, String columnName, int defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            Integer val;
            if (mValues != null) {
                val = mValues.get(row).getAsInteger(columnName);
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
     * @param row The row in the data holder to look at.
     * @param columnName The name of the column containing the long value.
     * @param defaultValue The value to return if the column did not exist or did not contain a
     *            long.
     * @return The long sored in this holder under the specified column or the default value if the
     *         value wasn't found.
     */
    protected final long getLong(int row, String columnName, long defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            Long val;
            if (mValues != null) {
                val = mValues.get(row).getAsLong(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getLong(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected final float getFloat(int row, String columnName, float defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            Float val;
            if (mValues != null) {
                val = mValues.get(row).getAsFloat(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getFloat(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets a double value from this holder.
     *
     * @param row The row in the data holder to look at.
     * @param columnName The name of the column containing the double value.
     * @param defaultValue The value to return if the column did not exist or did not contain a
     *            double.
     * @return The double stored in this holder under the specified column or the default value if
     *         the value wasn't found.
     */
    protected final double getDouble(int row, String columnName, double defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            Double val;
            if (mValues != null) {
                val = mValues.get(row).getAsDouble(columnName);
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
     * @param row The row in the data holder to look at.
     * @param columnName The name of the column containing the int array value.
     * @param defaultValue The value to return if the column did not exist or did not contain an
     *            int array.
     * @return The int array stored in this holder under the specified column or the default value
     *         if the value wasn't found.
     */
    protected final int[] getIntArray(int row, String columnName, int[] defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            byte[] bytes;
            if (mValues != null) {
                bytes = mValues.get(row).getAsByteArray(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                bytes = column == -1 ? null : mCursor.getBlob(column);
            }
            if (bytes == null) {
                return defaultValue;
            }
            if (bytes.length == 0) {
                return new int[0];
            }
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            int[] intArray = new int[bytes.length / 4];
            buffer.asIntBuffer().get(intArray);
            return intArray;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected final String getString(int row, String columnName, String defaultValue) {
        if (!containsRow(row)) return defaultValue;
        try {
            String val;
            if (mValues != null) {
                val = mValues.get(row).getAsString(columnName);
            } else {
                int column = mCursor.getColumnIndex(columnName);
                val = column == -1 ? null : mCursor.getString(column);
            }
            return val == null ? defaultValue : val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DataHolder[");
        for (int i = 0, size = getCount(); i < size; ++i) {
            if (i != 0) sb.append(",");
            sb.append("(");
            if (mValues != null) {
                boolean firstValue = true;
                ContentValues values = mValues.get(i);
                for (String key : values.keySet()) {
                    if (!firstValue) sb.append(",");
                    sb.append(key).append(":").append(values.get(key));
                    firstValue = false;
                }
            } else if (mCursor != null) {
                mCursor.moveToPosition(i);
                for (int j = 0, numCols = mCursor.getColumnCount(); j < numCols; ++j) {
                    if (j != 0) sb.append(",");
                    sb.append(mCursor.getColumnName(j)).append(":");
                    switch (mCursor.getType(j)) {
                        case Cursor.FIELD_TYPE_BLOB:
                            sb.append(mCursor.getBlob(j));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            sb.append(mCursor.getFloat(j));
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            sb.append(mCursor.getInt(j));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            sb.append(mCursor.getString(j));
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            sb.append("null");
                            break;
                    }
                }
            }
            sb.append(")");
        }
        return sb.append("]").toString();
    }

    /**
     * Builder used to create a {@link DataHolder} using one or more {@link ContentValues} rows.
     * Each row must have all of the columns passed in the constructor.
     */
    public static final class Builder {

        private final ArrayList<ContentValues> mValues;

        public Builder() {
            mValues = new ArrayList<>();
        }

        public Builder withValues(ContentValues values) {
            mValues.add(Preconditions.assertNotNull(values));
            return this;
        }

        public DataHolder build(int statusCode) {
            return new DataHolder(statusCode, mValues, null /* cursor */);
        }
    }
}

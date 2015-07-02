package com.concept2.api.utils;

import java.util.Arrays;

/**
 * Objects utilities, supplements java.util.Objects since it was only added in API 19.
 */
public class Objects {

    /**
     * Check that two objects are equal.
     *
     * @param a The first object.
     * @param b The second object.
     * @return True if the two objects are equal.
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Compute the hash of several objects.
     *
     * @param objects The objects to hash.
     * @return The hash code of the objects.
     */
    public static int hash(Object... objects) {
        return Arrays.hashCode(objects);
    }

    /**
     * Build a string to represent an object using the following format:
     * (var_name0 = value, var_name1 = value, ...)
     *
     * @return A new instance of {@link ObjectsStringBuilder}.
     */
    public static ObjectsStringBuilder stringBuilder() {
        return new ObjectsStringBuilder();
    }

    /**
     * Build a string to represent an object using the following format:
     * object_tag (var_name0 = value, var_name1 = value, ...)
     */
    public static final class ObjectsStringBuilder {

        /** Whether the object has any values stored yet or not. */
        private boolean mHasValues;

        /** Whether the builder is still editable, can only call {@link #toString()} once. */
        private boolean mEditable;

        /** The string builder used to store all the data. */
        private final StringBuilder mBuilder;

        /**
         * Create a string builder for an object.
         */
        private ObjectsStringBuilder() {
            mHasValues = false;
            mEditable = true;
            mBuilder = new StringBuilder();
        }

        /**
         * Add a value to the object's string representation. Once {@link #toString()} is called,
         * this object is no longer editable and will ignore calls to this method.
         *
         * @param memberName The name of the member object.
         * @param value The value stored in the member object.
         * @return This builder for chaining.
         */
        public ObjectsStringBuilder addVal(String memberName, Object value) {
            if (!mEditable) {
                return this;
            }
            mBuilder.append(mHasValues ? ", " : "(")
                    .append(memberName)
                    .append(" = ")
                    .append(value);
            mHasValues = true;
            return this;
        }

        /**
         * Builds the string representation. Note that once this is called the builder can no loger
         * be edited via {@link #addVal(String, Object)}.
         *
         * @return The string representation.
         */
        @Override
        public String toString() {
            if (mEditable) {
                if (mHasValues) {
                    mBuilder.append(")");
                }
                mEditable = false;
            }
            return mBuilder.toString();
        }
    }
}

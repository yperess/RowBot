package org.uvdev.rowbot.concept2api.utils;

public class Preconditions {

    /**
     * Assert that a give object is not null.
     *
     * @param obj The object to test.
     * @return The tested non-null object.
     * @throws IllegalStateException If the object was null.
     */
    public static <T> T assertNotNull(T obj) throws IllegalStateException {
        return assertNotNull(obj, "Null object found");
    }

    /**
     * Assert that a given object is not null.
     *
     * @param obj The object to test.
     * @param errorMessage The error message to use if the object was null.
     * @return The tested non-null object.
     * @throws IllegalStateException If the object was null.
     */
    public static <T> T assertNotNull(T obj, String errorMessage) throws IllegalStateException {
        if (obj == null) {
            throw new IllegalStateException(errorMessage);
        }
        return obj;
    }

    /**
     * Asserts that a condition is true.
     *
     * @param condition The condition to assert.
     * @throws IllegalStateException If the condition was false.
     */
    public static void assertTrue(boolean condition) throws IllegalStateException {
        assertTrue(condition, "Condition assert failed");
    }

    /**
     * Asserts that a condition is true.
     *
     * @param condition The condition to assert.
     * @param errorMessage The error message to use if the condition was false.
     * @throws IllegalStateException If the condition was false.
     */
    public static void assertTrue(boolean condition, String errorMessage) throws
            IllegalStateException {
        if (!condition) {
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @throws IllegalStateException If the objects were not equal.
     */
    public static void assertEqual(Object obj1, Object obj2) throws IllegalStateException {
        assertEqual(obj1, obj2, "Objects not equal");
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @param errorMessage The error message to use if the two objects are not equal.
     * @throws IllegalStateException If the objects were not equal.
     */
    public static void assertEqual(Object obj1, Object obj2, String errorMessage) throws
            IllegalStateException {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalStateException(errorMessage);
        }
    }
}

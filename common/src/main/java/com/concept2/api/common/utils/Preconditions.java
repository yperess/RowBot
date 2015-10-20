package com.concept2.api.common.utils;

public class Preconditions {

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalStateException("Null object found");
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("Condition assert failed");
        }
    }

    public static void assertEqual(Object obj1, Object obj2) {
        if ((obj1 == null && obj2 != null) || (obj1 != null && obj2 == null)
                || !obj1.equals(obj2)) {
            throw new IllegalStateException("Objects not equal");
        }
    }
}

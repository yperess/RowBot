package com.concept2.api.utils;

public class Preconditions {

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalStateException("Null object found");
        }
    }

    public static void assertValue(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("Condition assert failed");
        }
    }
}

package com.concept2.api;

/**
 * Callback object to report a result to.
 * @param <T> Class extending {@link Result} representing the result to be reported.
 */
public interface ResultCallback<T extends Result> {

    /**
     * Called when a result becomes available.
     *
     * @param result The result reported.
     */
    void onResult(T result);
}

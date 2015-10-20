package org.uvdev.rowbot.concept2api;

import java.util.concurrent.TimeUnit;

/**
 * Object used to represent a pending result that will be available in the future.
 * @param <T> A class extending {@link Result} that will be returned by this object when available.
 */
public interface PendingResult<T extends Result> {

    /**
     * Pause the current thread and wait for the result. The result may be null if canceled.
     *
     * @return The result or null if canceled.
     */
    T await();

    /**
     * Pause the current thread and wait for the result or timeout. The result may be null if
     * canceled or timed out.
     *
     * @param time The maximum amount of time to wait for the result.
     * @param timeUnit The time units used to wait.
     * @return The result or null if canceled or timed out.
     */
    T await(long time, TimeUnit timeUnit);

    /**
     * Cancel the request. This will notify any waiting threads or registered callbacks.
     */
    void cancel();

    /**
     * @return True if {@link #cancel()} was called on this object.
     */
    boolean isCanceled();

    /**
     * Set the callback object to be called when the result becomes available or canceled.
     *
     * @param callback The callback object to receive the result.
     */
    void setResultCallback(ResultCallback<T> callback);

    /**
     * Set the callback object to be called when the result becomes available, canceled, or times
     * out.
     *
     * @param callback The callback object to receive the result.
     * @param time The maximum amount of time to wait for the result.
     * @param timeUnit The time units used to wait.
     */
    void setResultCallback(ResultCallback<T> callback, long time, TimeUnit timeUnit);
}

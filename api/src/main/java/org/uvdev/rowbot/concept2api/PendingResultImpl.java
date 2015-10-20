package org.uvdev.rowbot.concept2api;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import org.uvdev.rowbot.concept2api.utils.Preconditions;

import java.util.concurrent.TimeUnit;

/**
 * Implementation of the {@link PendingResult}. This implementation provides default callback
 * handling making sure that the callback is called on the same thread the pending result was
 * created in.
 * @param <T> A result class extending {@link Result}.
 */
public abstract class PendingResultImpl<T extends Result> implements PendingResult<T> {

    private static final String TAG = "PendingResult";

    /** The handler to call when the result is ready. */
    private Handler mHandler;

    /** Whether the call was canceled. */
    private boolean mCanceled = false;

    /** The result. */
    private T mResult;

    /** The callback object to call when the result is ready. */
    private ResultCallback<T> mResultCallback;

    /** Timeout of the result callback, 0L mean no timeout. */
    private long mResultCallbackTimeout = 0L;

    /**
     * Create a new pending result. This method will set the callback thread to the thread creating
     * the pending result.
     */
    public PendingResultImpl() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Ignore timeout here since we were OK in setResult.
                if (mResultCallback != null && !mCanceled) {
                    mResultCallback.onResult(mResult);
                }
            }
        };
    }

    /**
     * Sets the result and notifies anyone listening for it.
     *
     * @param result The result, must not be null.
     */
    public synchronized void setResult(T result) {
        mResult = Preconditions.assertNotNull(result);
        if (mCanceled) {
            // Result was canceled, replace it.
            Log.i(TAG, "Result ignored due to cancel.");
            mResult = getFailedResult(Concept2StatusCodes.CANCELED);
            return;
        }
        // If we have a callback object and it hasn't timed out, call it on the right thread.
        if (mResultCallback != null) {
            long currentTime = getCurrentTime();
            if (mResultCallbackTimeout != 0L && mResultCallbackTimeout <= currentTime) {
                // Result timed out, replace it.
                mResult = getFailedResult(Concept2StatusCodes.TIMEOUT);
            }
            mHandler.sendMessage(mHandler.obtainMessage());
        }
        // Notify any waits.
        this.notify();
    }

    /**
     * Create a failed result object for the given status code.
     *
     * @param statusCode The failed status code.
     * @return A result object representing a failed state with the given status code.
     */
    abstract protected T getFailedResult(int statusCode);

    @Override
    public synchronized T await() {
        while (mResult == null && !mCanceled) {
            try {
                this.wait();
            } catch (InterruptedException e) {}
        }
        return getResult();
    }

    @Override
    public synchronized T await(long time, TimeUnit timeUnit) {
        Preconditions.assertTrue(time > 0, "Timeout must be greater than 0");
        if (timeUnit == null) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        long timeoutTime = getCurrentTime() + timeUnit.toMillis(time);
        while (mResult == null && !mCanceled && getCurrentTime() < timeoutTime) {
            long timeToWait = timeoutTime - getCurrentTime();
            try {
                this.wait(timeToWait);
            } catch (InterruptedException e) {}
        }
        return timeoutTime <= getCurrentTime()
                ? getFailedResult(Concept2StatusCodes.TIMEOUT) : getResult();
    }

    @Override
    public synchronized void cancel() {
        mCanceled = true;
        setResult(null);
    }

    @Override
    public synchronized boolean isCanceled() {
        return mCanceled;
    }

    @Override
    public synchronized void setResultCallback(ResultCallback<T> callback) {
        mResultCallback = Preconditions.assertNotNull(callback);
        T result = getResult();
        if (result != null) {
            mResultCallback.onResult(result);
        }
    }

    @Override
    public synchronized void setResultCallback(ResultCallback<T> callback, long time,
            TimeUnit timeUnit) {
        Preconditions.assertTrue(time > 0, "Timeout must be greater than 0");
        if (timeUnit == null) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        mResultCallbackTimeout = getCurrentTime() + timeUnit.toMillis(time);
        setResultCallback(callback);
    }

    /**
     * Get the current time from {@link SystemClock#elapsedRealtime()}.
     *
     * @return The elapsed realtime in milliseconds.
     */
    private long getCurrentTime() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * Get the currently stored result or a failed result with status code
     * {@link Concept2StatusCodes#CANCELED} if the operation was canceled.
     *
     * @return The current result.
     */
    private synchronized T getResult() {
            return mCanceled ? getFailedResult(Concept2StatusCodes.CANCELED) : mResult;
    }
}

package com.concept2.api.service.broker;

import android.content.Context;

import com.concept2.api.internal.DataHolder;

/**
 * Broker for all the data related to Concept2 operations.
 */
public class DataBroker {

    /** Lock used to guard the singleton instance. */
    private final static Object sInstanceLock = new Object();

    /** Singleton instance of the broker. */
    private static DataBroker sInstance;

    /**
     * Get or create and get the singleton instance of the data broker.
     *
     * @param context The context used to create the broker.
     * @return The singleton instance of the data broker.
     */
    public static DataBroker getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new DataBroker(context);
            }
            return sInstance;
        }
    }

    /** The pace monitor agent. */
    private final PaceMonitorAgent mPaceMonitorAgent;

    /**
     * @param context The context used to create the broker and associated databases.
     */
    private DataBroker(Context context) {
        mPaceMonitorAgent = new PaceMonitorAgent();
    }

    /**
     * Get the connected Pace Monitor status.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorStatus(Context context) {
        return mPaceMonitorAgent.getPaceMonitorStatus(context);
    }
}

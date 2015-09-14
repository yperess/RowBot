package com.concept2.api.service.broker;

import android.content.Context;

import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.internal.CommandImpl;
import com.concept2.api.rowbot.profile.Profile;

import java.util.List;

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

    /** The RowBot agent. */
    private final RowBotAgent mRowBotAgent;

    /**
     * @param context The context used to create the broker and associated databases.
     */
    private DataBroker(Context context) {
        mPaceMonitorAgent = new PaceMonitorAgent();
        mRowBotAgent = new RowBotAgent();
    }

    public DataHolder executePaceMonitorCommand(Context context, CommandImpl command) {
        return mPaceMonitorAgent.executeCommand(context, command);
    }

    public DataHolder createPaceMonitorCommandBatch(Context context,
            List<CommandBuilder.Command> commandList) {
        return mPaceMonitorAgent.createCommandBatch(context, commandList);
    }

    public DataHolder executePaceMonitorCommandBatch(Context context, int id) {
        return mPaceMonitorAgent.executeCommandBatch(context, id);
    }

    public DataHolder loadProfiles(Context context, String profileId) {
        return mRowBotAgent.loadProfiles(context, profileId);
    }

    public DataHolder createProfile(Context context, Profile profile) {
        return mRowBotAgent.createProfile(context, profile);
    }

    public DataHolder updateProfile(Context context, Profile profile) {
        return mRowBotAgent.updateProfile(context, profile);
    }
}

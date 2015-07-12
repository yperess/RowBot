package com.concept2.api.service.broker;

import android.content.Context;

import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitor;

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

    /**
     * Sets the state of the pace monitor. State must be one of
     * {@link PaceMonitor.PaceMonitorState}.
     *
     * @param context The calling context.
     * @param state The desired state of the pace monitor.
     * @return The status of the command.
     */
    public DataHolder setPaceMonitorState(Context context, int state) {
        return mPaceMonitorAgent.setPaceMonitorState(context, state);
    }

    /**
     * Get the current odometer reading from the connected pace monitor.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorOdometer(Context context) {
        return mPaceMonitorAgent.getOdometer(context);
    }

    /**
     * Get the current work time reading from the connected pace monitor.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorWorkTime(Context context) {
        return mPaceMonitorAgent.getWorkTime(context);
    }

    /**
     * Get the current work distance reading from the connected pace monitor.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorWorkDistance(Context context) {
        return mPaceMonitorAgent.getWorkDistance(context);
    }

    /**
     * Get the current work calories reading from the connected pace monitor.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorWorkCalories(Context context) {
        return mPaceMonitorAgent.getWorkCalories(context);
    }

    /**
     * Get the currently running programmed/pre-stored workout number.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorStoredWorkoutNumber(Context context) {
        return mPaceMonitorAgent.getStoredWorkoutNumber(context);
    }

    /**
     * Get the current pace in seconds / km.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorPace(Context context) {
        return mPaceMonitorAgent.getPace(context);
    }

    /**
     * Get the current stroke rate in strokes / minute.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorStrokeRate(Context context) {
        return mPaceMonitorAgent.getStrokeRate(context);
    }

    /**
     * Get the current pace monitor user's info.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorUserInfo(Context context) {
        return mPaceMonitorAgent.getUserInfo(context);
    }

    /**
     * Get the current heart rate in beats per minute.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorHeartRate(Context context) {
        return mPaceMonitorAgent.getHeartRate(context);
    }

    /**
     * Get the current power in watts.
     *
     * @param context The calling context.
     * @return {@link DataHolder} with the requested data or error code.
     */
    public DataHolder getPaceMonitorPower(Context context) {
        return mPaceMonitorAgent.getPower(context);
    }

    /**
     * Set the current time HH:MM:SS using a 24 hour clock.
     *
     * @param context The calling context.
     * @param hours The hours to set [0-23].
     * @param minutes The minutes to set [0-59].
     * @param seconds The seconds to set [0-59].
     * @return The status of the set operation.
     */
    public DataHolder setPaceMonitorTime(Context context, int hours, int minutes, int seconds) {
        return mPaceMonitorAgent.setTime(context, hours, minutes, seconds);
    }

    /**
     * Set the current date.
     *
     * @param context The calling context.
     * @param year The year to set [1900-2155].
     * @param month The month to set [1-12].
     * @param day The day of the month to set (range varies by month and year).
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorDate(Context context, int year, int month, int day) {
        return mPaceMonitorAgent.setDate(context, year, month, day);
    }

    /**
     * Set the timeout for the current pace monitor state.
     *
     * @param context The calling context.
     * @param seconds The number of seconds to timeout.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorTimeout(Context context, int seconds) {
        return mPaceMonitorAgent.setTimeout(context, seconds);
    }

    /**
     * Sets the goal time for the workout. Value must be at least 20 seconds and under 10 hours.
     *
     * @param context The calling context.
     * @param seconds The number of seconds the user should row.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorGoalTime(Context context, int seconds) {
        return mPaceMonitorAgent.setGoalTime(context, seconds);
    }

    /**
     * Sets the goal distance for the workout in meters. Value must be between [100, 50000] meters.
     *
     * @param context The calling context.
     * @param meters The number of meters the user should row.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorGoalDistance(Context context, int meters) {
        return mPaceMonitorAgent.setGoalDistance(context, meters);
    }

    /**
     * Sets the goal calories for the workout, Value must be between [0, 65535] calories.
     *
     * @param context The calling context.
     * @param calories The number of calories the user should burn.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorGoalCalories(Context context, int calories) {
        return mPaceMonitorAgent.setGoalCalories(context, calories);
    }

    /**
     * Sets the goal power for the workout. Value must be between [0, 65535] watts.
     *
     * @param context The calling context.
     * @param watts The power the user should produce.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorGoalPower(Context context, int watts) {
        return mPaceMonitorAgent.setGoalPower(context, watts);
    }

    /**
     * Sets the pace monitor to a programmed or pre-stored workout [0-15].
     *
     * @param context The calling context.
     * @param workoutNumber The workout number.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorStoredWorkoutNumber(Context context, int workoutNumber) {
        return mPaceMonitorAgent.setStoredWorkoutNumber(context, workoutNumber);
    }

    /**
     * Gets the current workout type.
     *
     * @param context The calling context.
     * @return The workout type as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorWorkoutType(Context context) {
        return mPaceMonitorAgent.getWorkoutType(context);
    }

    /**
     * Gets the current drag factor.
     *
     * @param context The calling context.
     * @return The drag factor as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorDragFactor(Context context) {
        return mPaceMonitorAgent.getDragFactor(context);
    }

    /**
     * Gets the current stroke state.
     *
     * @param context The calling context.
     * @return the stroke state as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorStrokeState(Context context) {
        return mPaceMonitorAgent.getStrokeState(context);
    }

    /**
     * Gets the high resolution work time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @return The high resolution work time as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorHighResWorkTime(Context context) {
        return mPaceMonitorAgent.getHighResWorkTime(context);
    }

    /**
     * Gets the high resolution work distance accurate to the 1/10th of a meter.
     *
     * @param context The calling context.
     * @return The high resolution work distance as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorHighResWorkDistance(Context context) {
        return mPaceMonitorAgent.getHighResWorkDistance(context);
    }

    /**
     * Gets and clears the latched error value.
     *
     * @param context The calling context.
     * @return The error value as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorErrorValue(Context context) {
        return mPaceMonitorAgent.getErrorValue(context);
    }

    /**
     * Gets the workout state.
     *
     * @param context The calling context.
     * @return The workout state as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorWorkoutState(Context context) {
        return mPaceMonitorAgent.getWorkoutState(context);
    }

    /**
     * Gets the current workout interval count.
     *
     * @param context The calling context.
     * @return The current workout interval count as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorWorkoutIntervalCount(Context context) {
        return mPaceMonitorAgent.getWorkoutIntervalCount(context);
    }

    /**
     * Gets the current interval type.
     *
     * @param context The calling context.
     * @return The current interval type as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorIntervalType(Context context) {
        return mPaceMonitorAgent.getIntervalType(context);
    }

    /**
     * Gets the current rest time.
     *
     * @param context The calling context.
     * @return The current rest time as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorRestTime(Context context) {
        return mPaceMonitorAgent.getRestTime(context);
    }

    /**
     * Sets the split time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @param seconds The number of split seconds.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorSplitTime(Context context, double seconds) {
        return mPaceMonitorAgent.setSplitTime(context, seconds);
    }

    /**
     * Sets the split distance accurate to the meter.
     *
     * @param context The calling context.
     * @param meters The number of split meters.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorSplitDistance(Context context, int meters) {
        return mPaceMonitorAgent.setSplitDistance(context, meters);
    }

    /**
     * Gets the force curve plot.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return The force curve as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorForcePlot(Context context, int numSamples) {
        return mPaceMonitorAgent.getForcePlot(context, numSamples);
    }

    /**
     * Gets the heart rate plot.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return The heart rate plot as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorHeartRatePlot(Context context, int numSamples) {
        return mPaceMonitorAgent.getHeartRatePlot(context, numSamples);
    }

    /**
     * Enable/disable the screen error mode.
     *
     * @param context The calling context.
     * @param enable True if the screen error mode should be enabled.
     * @return The status of the operation.
     */
    public DataHolder setPaceMonitorScreenErrorMode(Context context, boolean enable) {
        return mPaceMonitorAgent.setScreenErrorMode(context, enable);
    }

    public DataHolder createPaceMonitorCommandBatch(Context context,
            List<CommandBuilder.Command> commandList) {
        return mPaceMonitorAgent.createCommandBatch(context, commandList);
    }

    public DataHolder executePaceMonitorCommandBatch(Context context, long id) {
        return mPaceMonitorAgent.executeCommandBatch(context, id);
    }
}

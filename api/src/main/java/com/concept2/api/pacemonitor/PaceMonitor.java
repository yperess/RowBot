package com.concept2.api.pacemonitor;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;

import java.util.List;

/**
 * Pace Monitor APIs.
 */
public interface PaceMonitor {

    /**
     * States of the pace monitor.
     */
    interface PaceMonitorState {
        int RESET = 0;
        int IDLE = 1;
        int HAVE_ID = 2;
        int IN_USE = 3;
        int FINISHED = 4;
        int READY = 5;
        int BAD_ID = 6;
    }

    /**
     * Gender values reported by {@link GetUserInfoResult}.
     * @see #getUserInfo(Context)
     */
    interface Gender {
        /** No gender specified. */
        int NONE = 0;
        /** User specified male. */
        int MALE = 1;
        /** User specified female. */
        int FEMALE = 2;
    }

    interface WorkoutType {
        int INVALID = Integer.MIN_VALUE;
        int JUST_ROW_NO_SPLITS = 0;
        int JUST_ROW = 1;
        int DISTANCE_NO_SPLITS = 2;
        int DISTANCE = 3;
        int TIME_NO_SPLITS = 4;
        int TIME = 5;
        int TIME_INTERVAL = 6;
        int DISTANCE_INTERVAL = 7;
        int VARIABLE_INTERVAL = 8;
    }

    interface WorkoutState {
        int INVALID = Integer.MIN_VALUE;
        int WAITING = 0;
        int WORKOUT_ROW = 1;
        int COUNTDOWN_PAUSE = 2;
        int INTERVAL_REST = 3;
        int WORK_TIME_INTERVAL = 4;
        int WORK_DISTANCE_INTERVAL = 5;
        int REST_INTERVAL_END_TO_WORK_TIME_INTERVAL_BEGIN = 6;
        int REST_INTERVAL_END_TO_WORK_DISTANCE_INTERVAL_BEGIN = 7;
        int WORK_TIME_INTERVAL_END_TO_REST_INTERVAL_BEGIN = 8;
        int WORK_DISTANCE_INTERVAL_END_TO_REST_INTERVAL_BEGIN = 9;
        int WORKOUT_END = 10;
        int WORKOUT_TERMINATE = 11;
        int WORKOUT_LOGGED = 12;
        int WORKOUT_REARM = 13;
    }

    interface IntervalType {
        int INVALID = Integer.MIN_VALUE;
        int TIME = 0;
        int DISTANCE = 1;
        int REST = 2;
    }

    interface StrokeState {
        int INVALID = Integer.MIN_VALUE;
        int WAITING_FOR_MIN_SPEED = 0;
        int WAITING_FOR_ACCELERATION = 1;
        int DRIVING = 2;
        int DWELLING = 3;
        int RECOVERY = 4;
    }

    /** Result reporting distance. */
    interface GetDistanceResult extends PaceMonitorResult {
        int getDistance();
    }

    /** Result reporting time. */
    interface GetTimeResult extends PaceMonitorResult {
        long getSeconds();
    }

    /** Result reporting calories. */
    interface GetCaloriesResult extends PaceMonitorResult {
        int getCalories();
    }

    /** Result reporting the programmed/pre-stored workout number. */
    interface GetWorkoutNumberResult extends PaceMonitorResult {
        int getWorkoutNumber();
    }

    /** Result reporting the current pace in seconds/km. */
    interface GetPaceResult extends PaceMonitorResult {
        int getPace();
    }

    /** Result reporting the current stroke rate in strokes/minute. */
    interface GetStrokeRateResult extends PaceMonitorResult {
        int getStrokeRate();
    }

    /** Result reporting the current user info. */
    interface GetUserInfoResult extends PaceMonitorResult {
        int getWeight();
        int getAge();
        int getGender();
    }

    /** Result reporting the current heart rate in beats/minute. */
    interface GetHeartRateResult extends PaceMonitorResult {
        int getHeartRate();
    }

    /** Result reporting the current power in Watts. */
    interface GetPowerResult extends PaceMonitorResult {
        int getPower();
    }

    /** Result reporting the current workout type as one of {@link WorkoutType}. */
    interface GetWorkoutTypeResult extends PaceMonitorResult {
        int getWorkoutType();
    }

    /** Result reporting the current drag factor. */
    interface GetDragFactorResult extends PaceMonitorResult {
        int getDragFactor();
    }

    /** Result reporting the current stroke state as one of {@link StrokeState}. */
    interface GetStrokeStateResult extends PaceMonitorResult {
        int getStrokeState();
    }

    /** Result reporting a high resolution workout time including 1/100th of a second. */
    interface GetHighResWorkTimeResult extends PaceMonitorResult {
        double getSeconds();
    }

    /** Result reporting a high resolution workout distance including 1/10th of a meter. */
    interface GetHighResWorkDistanceResult extends PaceMonitorResult {
        double getDistance();
    }

    /** Result reporting the pace monitor latched error value. */
    interface GetErrorValueResult extends PaceMonitorResult {
        int getErrorValue();
    }

    /** Result reporting the current workout state as one of {@link WorkoutState}. */
    interface GetWorkoutStateResult extends PaceMonitorResult {
        int getWorkoutState();
    }

    /** Result reporting the current workout interval count. */
    interface GetWorkoutIntervalCountResult extends PaceMonitorResult {
        int getWorkoutIntervalCount();
    }

    /** Result reporting the current interval's type as one of {@link IntervalType}. */
    interface GetIntervalTypeResult extends PaceMonitorResult {
        int getIntervalType();
    }

    /** Result reporting the rest time in seconds. */
    interface GetRestTimeResult extends PaceMonitorResult {
        int getRestTime();
    }

    /** Result reporting the force plot. */
    interface GetForcePlotResult extends PaceMonitorResult {
        int[] getForcePlot();
    }

    /** Result reporting the heart beat plot. */
    interface GetHeartRatePlotResult extends PaceMonitorResult {
        int[] getHeartRatePlot();
    }

    /** Result reporting results for a batch of commands. */
    interface BatchResult extends PaceMonitorResult {
        List<Result> getResults();
    }
    //////////////////////////////
    // Beginning of API
    //////////////////////////////

    /**
     * Gets the status of the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> getStatus(Context context);

    /**
     * Sets the state of the pace monitor. State must be one of {@link PaceMonitorState}.
     *
     * @param context The calling context.
     * @param state The new state of the pace monitor.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setState(Context context, int state);

    /**
     * Gets the odometer of the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetDistanceResult> getOdometer(Context context);

    /**
     * Gets the currently displayed work time from the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetTimeResult> getWorkTime(Context context);

    /**
     * Gets the currently displayed work distance from the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetDistanceResult> getWorkDistance(Context context);

    /**
     * Gets the currently displayed work calories from the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetCaloriesResult> getWorkCalories(Context context);

    /**
     * Gets the currently running programmed/pre-stored workout number from the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetWorkoutNumberResult> getStoredWorkoutNumber(Context context);

    /**
     * Gets the current pace from the pace monitor. Pace is reported in seconds / km.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetPaceResult> getPace(Context context);

    /**
     * Gets the current stroke rate from the pace monitor. Stroke rate is reported in
     * strokes / minute.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetStrokeRateResult> getStrokeRate(Context context);

    /**
     * Gets the user info saved to the pace monitor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetUserInfoResult> getUserInfo(Context context);

    /**
     * Gets the current heart rate from the pace monitor. Heart rate is reported in beats / minute.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetHeartRateResult> getHeartRate(Context context);

    /**
     * Gets the current power from the pace monitor. Power is reported in Watts.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetPowerResult> getPower(Context context);

    /**
     * Set the current time HH:MM:SS using a 24 hour clock.
     *
     * @param context The calling context.
     * @param hours The hours to set [0-23].
     * @param minutes The minutes to set [0-59].
     * @param seconds The seconds to set [0-59].
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setTime(Context context, int hours, int minutes, int seconds);

    /**
     * Set the current date MM/DD/YYYY. This method only accepts years between [1900, 2155].
     *
     * @param context The calling context.
     * @param year The year to set [1900-2155].
     * @param month The month to set [1-12].
     * @param day The day of the month (range depends on the year and month).
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setDate(Context context, int year, int month, int day);

    /**
     * Set the timeout for the current pace monitor state.
     *
     * @param context The calling context.
     * @param seconds The number of seconds to timeout.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setTimeout(Context context, int seconds);

    /**
     * Sets the goal time for the workout in seconds. Value must be at least 20 seconds and under 10
     * hours.
     *
     * @param context The calling context.
     * @param seconds The number of seconds the user should row.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setGoalTime(Context context, int seconds);

    /**
     * Sets the goal distance for the workout in meters. Value must be in the range of
     * [100, 50000] meters.
     *
     * @param context The calling context.
     * @param meters The number of meters the user should row.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setGoalDistance(Context context, int meters);

    /**
     * Sets the goal calories for the workout. Value must be in the range of [0, 65535] calories.
     *
     * @param context The calling context.
     * @param calories The number of calories the user should burn.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setGoalCalories(Context context, int calories);

    /**
     * Sets the pace monitor to a programmed or pre-stored workout using the following definitions:
     * <ul>
     *     <li>0: programmed workout.</li>
     *     <li>1-5: Standard list.</li>
     *     <li>6-10: Custom list.</li>
     *     <li>11-15: Favorites list (only available if logcard is present).</li>
     * </ul>
     *
     * @param context The calling context.
     * @param workoutNumber The workout number.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setStoredWorkoutNumber(Context context, int workoutNumber);

    /**
     * Sets the goal power for the workout. Value must be in the range of [0, 65535] watts.
     *
     * @param context The calling context.
     * @param watts The power the user should produce.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setGoalPower(Context context, int watts);

    /**
     * Gets the current workout type as one of {@link WorkoutType}.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetWorkoutTypeResult> getWorkoutType(Context context);

    /**
     * Gets the current drag factor.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetDragFactorResult> getDragFactor(Context context);

    /**
     * Gets the current stroke state as one of {@link StrokeState}.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetStrokeStateResult> getStrokeState(Context context);

    /**
     * Gets the high resolution work time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetHighResWorkTimeResult> getHighResWorkTime(Context context);

    /**
     * Gets the high resolution work distance accurate to the 1/10th of a meter.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetHighResWorkDistanceResult> getHighResWorkDistance(Context context);

    /**
     * Gets and clears the latched error value in the pace monitor when the screen error display
     * mode is disabled.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetErrorValueResult> getErrorValue(Context context);
    // TODO enumerate error values.

    /**
     * Gets the workout state as one of {@link WorkoutState}.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetWorkoutStateResult> getWorkoutState(Context context);

    /**
     * Gets the current workout interval count.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetWorkoutIntervalCountResult> getWorkoutIntervalCount(Context context);

    /**
     * Gets the current interval type as one of {@link IntervalType}.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetIntervalTypeResult> getIntervalType(Context context);

    /**
     * Gets the current rest time in seconds.
     *
     * @param context The calling context.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetRestTimeResult> getRestTime(Context context);

    /**
     * Sets the split to use time units with a given seconds value accurate to 1/100th of a second
     * and must be at least 20 seconds.
     *
     * @param context The calling context.
     * @param seconds The split duration in seconds.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setSplitTime(Context context, double seconds);

    /**
     * Sets the split to use distance units with a given meter value accurate to the meter
     * and must be at least 100m.
     *
     * @param context The calling context.
     * @param meters The split duration in meters.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setSplitDistance(Context context, int meters);

    /**
     * Gets the force curve plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetForcePlotResult> getForcePlot(Context context, int numSamples);

    /**
     * Enable/disable the screen error mode.
     *
     * @param context The calling context.
     * @param enable True if the screen error mode should be enabled.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<PaceMonitorResult> setScreenErrorMode(Context context, boolean enable);

    /**
     * Gets the heart rate plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<GetHeartRatePlotResult> getHeartRatePlot(Context context, int numSamples);

    /**
     * Execute one or more commands in order as a batch. Commands should be created via the static
     * methods provided in {@link CommandBatch}.
     *
     * @param context The calling context.
     * @param commandList The list of commands to execute.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<BatchResult> executeCommandBatch(Context context,
            List<CommandBatch.Command> commandList);
}

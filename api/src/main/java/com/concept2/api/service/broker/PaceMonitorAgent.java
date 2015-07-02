package com.concept2.api.service.broker;

import android.content.Context;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.common.Constants;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.CommandBatch;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.GetDragFactorResultRef;
import com.concept2.api.pacemonitor.internal.GetErrorValueResultRef;
import com.concept2.api.pacemonitor.internal.GetForcePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRatePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetIntervalTypeResultRef;
import com.concept2.api.pacemonitor.internal.GetRestTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeRateResultRef;
import com.concept2.api.pacemonitor.internal.GetCaloriesResultRef;
import com.concept2.api.pacemonitor.internal.GetDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRateResultRef;
import com.concept2.api.pacemonitor.internal.GetPaceResultRef;
import com.concept2.api.pacemonitor.internal.GetPowerResultRef;
import com.concept2.api.pacemonitor.internal.PaceMonitorResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeStateResultRef;
import com.concept2.api.pacemonitor.internal.GetTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetUserInfoResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutIntervalCountResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutNumberResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutStateResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutTypeResultRef;
import com.concept2.api.service.broker.pacemonitor.Engine;
import com.concept2.api.service.broker.pacemonitor.GetPMData;
import com.concept2.api.service.broker.pacemonitor.USBEngine;
import com.concept2.api.service.broker.pacemonitor.util.CsafeUnitUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Agent used to access the Pace Monitor.
 */
public class PaceMonitorAgent {

    private static final String TAG = "PaceMonitorAgent";
    private static final boolean DBG = false | Constants.DBG;

    /**
     * Basic commands to communicate with the Pace Monitor.
     */
    private interface Commands {
        // Short commands.
        byte GET_STATUS = (byte) 0x80;
        byte GO_RESET = (byte) 0x81;
        byte GO_IDLE = (byte) 0x82;
        byte GO_HAVE_ID = (byte) 0x83;
        byte GO_IN_USE = (byte) 0x85;
        byte GO_FINISHED = (byte) 0x86;
        byte GO_READY = (byte) 0x87;
        byte GO_BAD_ID = (byte) 0x88;
        byte GET_ODOMETER = (byte) 0x9B;
        byte GET_WORK_TIME = (byte) 0xA0;
        byte GET_WORK_DISTANCE = (byte) 0xA1;
        byte GET_WORK_CALORIES = (byte) 0xA3;
        byte GET_WORKOUT_NUMBER = (byte) 0xA4;
        byte GET_PACE = (byte) 0xA6;
        byte GET_CADENCE = (byte) 0xA7;
        byte GET_USER_INFO = (byte) 0xAB;
        byte GET_HEART_RATE = (byte) 0xB0;
        byte GET_POWER = (byte) 0xB4;

        // Long commands.
        byte SET_TIME = (byte) 0x11;
        byte SET_DATE = (byte) 0x12;
        byte SET_TIMEOUT = (byte) 0x13;
        byte SET_GOAL_TIME = (byte) 0x20;
        byte SET_GOAL_DISTANCE = (byte) 0x21;
        byte SET_GOAL_CALORIES = (byte) 0x23;
        byte SET_PROGRAM = (byte) 0x24;
        byte SET_GOAL_POWER = (byte) 0x34;

        // Custom commands.
        byte USR_CONFIG1 = (byte) 0x1A;
        byte SET_PM_CGF = (byte) 0x76;
        byte SET_PM_DATA = (byte) 0x77;
        byte GET_PM_CFG = (byte) 0x7E;
        byte GET_PM_DATA = (byte) 0x7F;

        byte PM_GET_WORKOUT_TYPE = (byte) 0x89;
        byte PM_GET_DRAG_FACTOR = (byte) 0xC1;
        byte PM_GET_STROKE_STATE = (byte) 0xBF;
        byte PM_GET_WORK_TIME = (byte) 0xA0;
        byte PM_GET_WORK_DISTANCE = (byte) 0xA3;
        byte PM_GET_ERROR_VALUE = (byte) 0xC9;
        byte PM_GET_WORKOUT_STATE = (byte) 0x8D;
        byte PM_GET_WORKOUT_INTERVAL_COUNT = (byte) 0x9F;
        byte PM_GET_INTERVAL_TYPE = (byte) 0x8E;
        byte PM_GET_REST_TIME = (byte) 0xCF;
        byte PM_SET_SPLIT_DURATION = (byte) 0x05;
        byte PM_GET_FORCE_PLOT_DATA = (byte) 0x6B;
        byte PM_GET_HEART_RATE_DATA = (byte) 0x6C;
        byte PM_SET_SCREEN_ERROR_MODE = (byte) 0x27;
    }

    /** {@link Engine} instance for a USB connection. */
    private final Engine mUsbEngine = new USBEngine();

    // TODO - Add a BT engine.

    /**
     * Get the status of the connected monitor. If no monitor is connected, this method will return
     * an empty {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status.
     * If an error occurred during communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The status of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getPaceMonitorStatus(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_STATUS)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        return PaceMonitorResultRef.createDataHolder(result.getStatus());
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
        byte stateCommand;
        switch (state) {
            case PaceMonitor.PaceMonitorState.RESET:
                stateCommand = Commands.GO_RESET;
                break;
            case PaceMonitor.PaceMonitorState.IDLE:
                stateCommand = Commands.GO_IDLE;
                break;
            case PaceMonitor.PaceMonitorState.HAVE_ID:
                stateCommand = Commands.GO_HAVE_ID;
                break;
            case PaceMonitor.PaceMonitorState.IN_USE:
                stateCommand = Commands.GO_IN_USE;
                break;
            case PaceMonitor.PaceMonitorState.FINISHED:
                stateCommand = Commands.GO_FINISHED;
                break;
            case PaceMonitor.PaceMonitorState.READY:
                stateCommand = Commands.GO_READY;
                break;
            case PaceMonitor.PaceMonitorState.BAD_ID:
                stateCommand = Commands.GO_BAD_ID;
                break;
            default:
                return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR) ;
        }
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, stateCommand)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Get the odometer reading of the connected monitor. If no monitor is connected, this method
     * will return an empty {@link DataHolder} with a
     * {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an error occurred during
     * communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The odometer reading of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getOdometer(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_ODOMETER)
                .setValidation((byte) 5)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int distance = ((bytes[3] & 0xFF) << 24)
                | ((bytes[2] & 0xFF) << 16)
                | ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[4] & 0xFF;
        distance = CsafeUnitUtil.normalizeInt(distance, units);
        return GetDistanceResultRef.createDataHolder(distance);
    }

    /**
     * Get the work time reading of the connected monitor. If no monitor is connected, this method
     * will return an empty {@link DataHolder} with a
     * {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an error occurred during
     * communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The work time reading of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getWorkTime(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_WORK_TIME)
                .setValidation((byte) 3)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        long seconds = TimeUnit.HOURS.toSeconds(bytes[0] & 0xFF)
                + TimeUnit.MINUTES.toSeconds(bytes[1] & 0xFF)
                + (bytes[2] & 0xFF);
        return GetTimeResultRef.createDataHolder(seconds);
    }

    /**
     * Get the work distance reading of the connected monitor. If no monitor is connected, this
     * method will return an empty {@link DataHolder} with a
     * {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an error occurred during
     * communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The work distance reading of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getWorkDistance(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_WORK_DISTANCE)
                .setValidation((byte) 3)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int distance = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[2] & 0xFF;
        distance = CsafeUnitUtil.normalizeInt(distance, units);
        return GetDistanceResultRef.createDataHolder(distance);
    }

    /**
     * Get the work calories reading of the connected monitor. If no monitor is connected, this
     * method will return an empty {@link DataHolder} with a
     * {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an error occurred during
     * communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The work calories reading of the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getWorkCalories(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_WORK_CALORIES)
                .setValidation((byte) 2)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int calories = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        return GetCaloriesResultRef.createDataHolder(calories);
    }

    /**
     * Get the currently running programmed/pre-stored workout number. If no monitor is connected,
     * this method will return an empty {@link DataHolder} with a
     * {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an error occurred during
     * communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The currently running programmed/pre-stored workout number as a {@link DataHolder}.
     */
    public DataHolder getStoredWorkoutNumber(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_WORKOUT_NUMBER)
                .setValidation((byte) 1)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int workoutNumber = result.getData()[0] & 0xFF;
        return GetWorkoutNumberResultRef.createDataHolder(workoutNumber);
    }

    /**
     * Get the current pace. If no monitor is connected, this method will return an empty
     * {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an
     * error occurred during communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The current pace in sec/km as a {@link DataHolder}.
     */
    public DataHolder getPace(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_PACE)
                .setValidation((byte) 3)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int strokePace = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[2] & 0xFF;
        strokePace = CsafeUnitUtil.normalizeInt(strokePace, units);
        return GetPaceResultRef.createDataHolder(strokePace);
    }

    /**
     * Get the current stroke rate. If no monitor is connected, this method will return an empty
     * {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an
     * error occurred during communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The current stroke rate in strokes/min as a {@link DataHolder}.
     */
    public DataHolder getStrokeRate(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_CADENCE)
                .setValidation((byte) 3)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int strokeCadence = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[2] & 0xFF;
        strokeCadence = CsafeUnitUtil.normalizeInt(strokeCadence, units);
        return GetStrokeRateResultRef.createDataHolder(strokeCadence);
    }

    /**
     * Get the user info saved to the pace monitor. If no monitor is connected, this method will
     * return an empty {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND}
     * status. If an error occurred during communication, an empty {@link DataHolder} is returned
     * with a {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The user info from the pace monitor as a {@link DataHolder}.
     */
    public DataHolder getUserInfo(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_USER_INFO)
                .setValidation((byte) 5)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int weight = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[2] & 0xFF;
        weight = CsafeUnitUtil.normalizeInt(weight, units);
        int age = bytes[3] & 0xFF;
        int gender = bytes[4] & 0xFF;
        return GetUserInfoResultRef.createDataHolder(weight, age, gender);
    }

    /**
     * Get the current heart rate. If no monitor is connected, this method will return an empty
     * {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an
     * error occurred during communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The current heart rate in beats/min as a {@link DataHolder}.
     */
    public DataHolder getHeartRate(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_HEART_RATE)
                .setValidation((byte) 1)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int heartRate = result.getData()[0] & 0xFF;
        return GetHeartRateResultRef.createDataHolder(heartRate);
    }

    /**
     * Get the current power. If no monitor is connected, this method will return an empty
     * {@link DataHolder} with a {@link Concept2StatusCodes#PACE_MONITOR_NOT_FOUND} status. If an
     * error occurred during communication, an empty {@link DataHolder} is returned with a
     * {@link Concept2StatusCodes#PACE_MONITOR_COMMUNICATION_ERROR} status.
     *
     * @param context The calling context.
     * @return The current power in Watts as a {@link DataHolder}.
     */
    public DataHolder getPower(Context context) {
        GetPMData get = new GetPMData.Builder(context, mUsbEngine, Commands.GET_POWER)
                .setValidation((byte) 3)
                .build();
        GetPMData.DataResult result = get.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        byte[] bytes = result.getData();
        int power = ((bytes[1] & 0xFF) << 8)
                | (bytes[0] & 0xFF);
        int units = bytes[2] & 0xFF;
        power = CsafeUnitUtil.normalizeInt(power, units);
        return GetPowerResultRef.createDataHolder(power);
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
    public DataHolder setTime(Context context, int hours, int minutes, int seconds) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_TIME)
                .setData(new byte[] { (byte) 0x03, toByte(hours), toByte(minutes),
                        toByte(seconds) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
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
    public DataHolder setDate(Context context, int year, int month, int day) {
        year -= 1900;
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_DATE)
                .setData(new byte[] { (byte) 0x03, toByte(year), toByte(month), toByte(day) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Set the timeout for the current pace monitor state.
     *
     * @param context The calling context.
     * @param seconds The number of seconds to timeout.
     * @return The status of the operation.
     */
    public DataHolder setTimeout(Context context, int seconds) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_TIMEOUT)
                .setData(new byte[] { (byte) 0x01, toByte(seconds) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the goal time for the workout. Value must be at least 20 seconds and under 10 hours.
     *
     * @param context The calling context.
     * @param seconds The number of seconds the user should row.
     * @return The status of the operation.
     */
    public DataHolder setGoalTime(Context context, int seconds) {
        byte hoursByte = toByte(TimeUnit.SECONDS.toHours(seconds));
        seconds -= TimeUnit.HOURS.toSeconds(hoursByte);
        byte minutesByte = toByte(TimeUnit.SECONDS.toMinutes(seconds));
        seconds -= TimeUnit.MINUTES.toSeconds(minutesByte);
        byte secondsByte = toByte(seconds);
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_GOAL_TIME)
                .setData(new byte[] { (byte) 0x03, hoursByte, minutesByte, secondsByte })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the goal distance for the workout in meters. Value must be between [100, 50000] meters.
     *
     * @param context The calling context.
     * @param meters The number of meters the user should row.
     * @return The status of the operation.
     */
    public DataHolder setGoalDistance(Context context, int meters) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_GOAL_DISTANCE)
                .setData(new byte[] { (byte) 0x03, getByte(meters, 0), getByte(meters, 1),
                        CsafeUnitUtil.METER })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the goal calories for the workout, Value must be between [0, 65535] calories.
     *
     * @param context The calling context.
     * @param calories The number of calories the user should burn.
     * @return The status of the operation.
     */
    public DataHolder setGoalCalories(Context context, int calories) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_GOAL_CALORIES)
                .setData(new byte[] { (byte) 0x02, getByte(calories, 0), getByte(calories, 1) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the goal power for the workout. Value must be between [0, 65535] watts.
     *
     * @param context The calling context.
     * @param watts The power the user should produce.
     * @return The status of the operation.
     */
    public DataHolder setGoalPower(Context context, int watts) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_GOAL_POWER)
                .setData(new byte[] { (byte) 0x03, getByte(watts, 0), getByte(watts, 1),
                        CsafeUnitUtil.WATTS })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the pace monitor to a programmed or pre-stored workout [0-15].
     *
     * @param context The calling context.
     * @param workoutNumber The workout number.
     * @return The status of the operation.
     */
    public DataHolder setStoredWorkoutNumber(Context context, int workoutNumber) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.SET_PROGRAM)
                .setData(new byte[] { 0x02, toByte(workoutNumber), 0x00 })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Gets the current workout type.
     *
     * @param context The calling context.
     * @return The workout type as a {@link DataHolder}.
     */
    public DataHolder getWorkoutType(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_WORKOUT_TYPE })
                .setValidation((byte) 0x01)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int workoutType = result.getData()[0];
        return GetWorkoutTypeResultRef.createDataHolder(workoutType);
    }

    /**
     * Gets the current drag factor.
     *
     * @param context The calling context.
     * @return The drag factor as a {@link DataHolder}.
     */
    public DataHolder getDragFactor(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_DRAG_FACTOR })
                .setValidation((byte) 0x01)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int dragFactor = result.getData()[0];
        return GetDragFactorResultRef.createDataHolder(dragFactor);
    }

    /**
     * Gets the current stroke state.
     *
     * @param context The calling context.
     * @return the stroke state as a {@link DataHolder}.
     */
    public DataHolder getStrokeState(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_STROKE_STATE })
                .setValidation((byte) 0x01)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int strokeState = result.getData()[0];
        return GetStrokeStateResultRef.createDataHolder(strokeState);
    }

    /**
     * Gets the high resolution work time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @return The high resolution work time as a {@link DataHolder}.
     */
    public DataHolder getHighResWorkTime(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_WORK_TIME })
                .setValidation((byte) 0x05)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        double time = read4ByteInt(result.getData(), 0)
                + (0.01 * toByte(result.getData()[4]));
        return GetHighResWorkTimeResultRef.createDataHolder(time);
    }

    /**
     * Gets the high resolution work distance accurate to the 1/10th of a meter.
     *
     * @param context The calling context.
     * @return The high resolution work distance as a {@link DataHolder}.
     */
    public DataHolder getHighResWorkDistance(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_WORK_DISTANCE })
                .setValidation((byte) 0x05)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        double meters = read4ByteInt(result.getData(), 0)
                + (0.1 * toByte(result.getData()[4]));
        return GetHighResWorkDistanceResultRef.createDataHolder(meters);
    }

    /**
     * Gets and clears the latched error value.
     *
     * @param context The calling context.
     * @return The error value as a {@link DataHolder}.
     */
    public DataHolder getErrorValue(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_ERROR_VALUE })
                .setValidation((byte) 0x02)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int errorValue = read2ByteInt(result.getData(), 0);
        return GetErrorValueResultRef.createDataHolder(errorValue);
    }

    /**
     * Gets the workout state.
     *
     * @param context The calling context.
     * @return The workout state as a {@link DataHolder}.
     */
    public DataHolder getWorkoutState(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_WORKOUT_STATE })
                .setValidation((byte) 0x01)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int workoutState = result.getData()[0];
        return GetWorkoutStateResultRef.createDataHolder(workoutState);
    }

    /**
     * Gets the current workout interval count.
     *
     * @param context The calling context.
     * @return The current workout interval count as a {@link DataHolder}.
     */
    public DataHolder getWorkoutIntervalCount(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_WORKOUT_INTERVAL_COUNT })
                .setValidation((byte) 0x01)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int intervalCount = result.getData()[0];
        return GetWorkoutIntervalCountResultRef.createDataHolder(intervalCount);
    }

    /**
     * Gets the current interval type.
     *
     * @param context The calling context.
     * @return The current interval type as a {@link DataHolder}.
     */
    public DataHolder getIntervalType(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_INTERVAL_TYPE })
                .setValidation((byte) 0x02)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int intervalType = result.getData()[0];
        return GetIntervalTypeResultRef.createDataHolder(intervalType);
    }

    /**
     * Gets the current rest time.
     *
     * @param context The calling context.
     * @return The current rest time as a {@link DataHolder}.
     */
    public DataHolder getRestTime(Context context) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x01, Commands.PM_GET_REST_TIME })
                .setValidation((byte) 0x02)
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int restTime = read2ByteInt(result.getData(), 0);
        return GetRestTimeResultRef.createDataHolder(restTime);
    }

    /**
     * Sets the split time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @param seconds The number of split seconds.
     * @return The status of the operation.
     */
    public DataHolder setSplitTime(Context context, double seconds) {
        int duration = (int) (seconds * 100);
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[]{0x07, Commands.PM_SET_SPLIT_DURATION, 0x05, 0x00 /* time */,
                        getByte(duration, 0), getByte(duration, 1), getByte(duration, 2),
                        getByte(duration, 3)})
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Sets the split distance accurate to the 1/10th of a meter.
     *
     * @param context The calling context.
     * @param meters The number of split meters.
     * @return The status of the operation.
     */
    public DataHolder setSplitDistance(Context context, int meters) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x07, Commands.PM_SET_SPLIT_DURATION, 0x05,
                        (byte) 0x80 /* distance */,
                        getByte(meters, 0), getByte(meters, 1), getByte(meters, 2),
                        getByte(meters, 3) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Gets the force curve plot.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return The force curve as a {@link DataHolder}.
     */
    public DataHolder getForcePlot(Context context, int numSamples) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[]{0x03, Commands.PM_GET_FORCE_PLOT_DATA, 0x01,
                        (byte) (numSamples * 2)})
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int bytesRead = result.getData()[0];
        int[] forcePlot = new int[bytesRead / 2];
        for (int i = 0; i < forcePlot.length; ++i) {
            forcePlot[i] = read2ByteInt(result.getData(), 1 + (i * 2));
        }
        return GetForcePlotResultRef.createDataHolder(forcePlot);
    }

    /**
     * Gets the heart rate plot.
     *
     * @param context The calling context.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return The heart rate plot as a {@link DataHolder}.
     */
    public DataHolder getHeartRatePlot(Context context, int numSamples) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[]{0x03, Commands.PM_GET_HEART_RATE_DATA, 0x01,
                        (byte) (numSamples * 2)})
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }

        int bytesRead = result.getData()[0];
        int[] heartRatePlot = new int[bytesRead / 2];
        for (int i = 0; i < heartRatePlot.length; ++i) {
            heartRatePlot[i] = read2ByteInt(result.getData(), 1 + (i * 2));
        }
        return GetHeartRatePlotResultRef.createDataHolder(heartRatePlot);
    }

    /**
     * Enable/disable the screen error mode.
     *
     * @param context The calling context.
     * @param enable True if the screen error mode should be enabled.
     * @return The status of the operation.
     */
    public DataHolder setScreenErrorMode(Context context, boolean enable) {
        GetPMData set = new GetPMData.Builder(context, mUsbEngine, Commands.USR_CONFIG1)
                .setData(new byte[] { 0x03, Commands.PM_SET_SCREEN_ERROR_MODE, 0x01,
                        (byte) (enable ? 0x01 : 0x00) })
                .build();
        GetPMData.DataResult result = set.getData();
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        return PaceMonitorResultRef.createDataHolder(result.getStatus());
    }

    /**
     * Execute one or more commands in order as a batch. Commands should be created via the static
     * methods provided in {@link CommandBatch}.
     *
     * @param context The calling context.
     * @param commandList The list of commands to execute.
     * @return The command results as a {@link DataHolder}.
     */
    public DataHolder executeCommandBatch(Context context, List<CommandBatch.Command> commandList) {
        return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR);
    }

    private int read2ByteInt(byte[] bytes, int pos) {
        return ((bytes[pos + 1] & 0xFF) << 8)
                | (bytes[pos] & 0xFF);
    }

    private int read4ByteInt(byte[] bytes, int pos) {
        return ((bytes[pos + 3] & 0xFF) << 24)
                | ((bytes[pos + 2] & 0xFF) << 16)
                | ((bytes[pos + 1] & 0xFF) << 8)
                | (bytes[pos] & 0xFF);
    }

    private byte toByte(long val) {
        return (byte) (val & 0xFF);
    }

    private byte toByte(int val) {
        return (byte) (val & 0xFF);
    }

    private byte getByte(int val, int idx) {
        return (byte) ((val >> (idx * 8)) & 0xFF);
    }
}

package com.concept2.api.pacemonitor;

import android.os.Bundle;

import com.concept2.api.ResultCallback;
import com.concept2.api.pacemonitor.PaceMonitor.GetCaloriesResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetDistanceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetDragFactorResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetErrorValueResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetForcePlotResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRatePlotResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkDistanceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetIntervalTypeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetPaceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetPowerResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetRestTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetStrokeStateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetUserInfoResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutIntervalCountResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutNumberResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutStateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutTypeResult;
import com.concept2.api.pacemonitor.internal.CommandImpl;
import com.concept2.api.pacemonitor.internal.PaceMonitorImpl;
import com.concept2.api.service.broker.pacemonitor.util.CsafeUnitUtil;

import java.util.concurrent.TimeUnit;

public class CommandBuilder {

    private static final String ARG_NUM_SAMPLES = "numSamples";
    private static final String ARG_HOURS = "hours";
    private static final String ARG_MINUTES = "minutes";
    private static final String ARG_SECONDS = "seconds";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    private static final String ARG_METERS = "meters";
    private static final String ARG_CALORIES = "calories";
    private static final String ARG_WATTS = "watts";
    private static final String ARG_STORED_WORKOUT_NUMBER = "workoutNumber";
    private static final String ARG_SCREEN_ERROR_MODE = "screenErrorMode";

    private static final byte CMD_GET_STATUS = (byte) 0x80;
    private static final byte CMD_GO_RESET = (byte) 0x81;
    private static final byte CMD_GO_IDLE = (byte) 0x82;
    private static final byte CMD_GO_HAVE_ID = (byte) 0x83;
    private static final byte CMD_GO_IN_USE = (byte) 0x85;
    private static final byte CMD_GO_FINISHED = (byte) 0x86;
    private static final byte CMD_GO_READY = (byte) 0x87;
    private static final byte CMD_GO_BAD_ID = (byte) 0x88;
    private static final byte CMD_GET_ODOMETER = (byte) 0x9B;
    private static final byte CMD_GET_WORK_TIME = (byte) 0xA0;
    private static final byte CMD_GET_WORK_DISTANCE = (byte) 0xA1;
    private static final byte CMD_GET_WORK_CALORIES = (byte) 0xA3;
    private static final byte CMD_GET_STORED_WORKOUT_NUMBER = (byte) 0xA4;
    private static final byte CMD_GET_PACE = (byte) 0xA6;
    private static final byte CMD_GET_STROKE_RATE = (byte) 0xA7;
    private static final byte CMD_GET_USER_INFO = (byte) 0xAB;
    private static final byte CMD_GET_HEART_RATE = (byte) 0xB0;
    private static final byte CMD_GET_POWER = (byte) 0xB4;

    // Long commands.
    private static final byte CMD_SET_TIME = (byte) 0x11;
    private static final byte CMD_SET_DATE = (byte) 0x12;
    private static final byte CMD_SET_TIMEOUT = (byte) 0x13;
    private static final byte CMD_SET_GOAL_TIME = (byte) 0x20;
    private static final byte CMD_SET_GOAL_DISTANCE = (byte) 0x21;
    private static final byte CMD_SET_GOAL_CALORIES = (byte) 0x23;
    private static final byte CMD_SET_STORED_WORKOUT_NUMBER = (byte) 0x24;
    private static final byte CMD_SET_GOAL_POWER = (byte) 0x34;

    // Custom commands.
    private static final byte USR_CONFIG1 = (byte) 0x1A;

    private static final byte CMD_GET_WORKOUT_TYPE = (byte) 0x89;
    private static final byte CMD_GET_DRAG_FACTOR = (byte) 0xC1;
    private static final byte CMD_GET_STROKE_STATE = (byte) 0xBF;
    private static final byte CMD_GET_HIGH_RES_WORK_TIME = (byte) 0xA0;
    private static final byte CMD_GET_HIGH_RES_WORK_DISTANCE = (byte) 0xA3;
    private static final byte CMD_GET_ERROR_VALUE = (byte) 0xC9;
    private static final byte CMD_GET_WORKOUT_STATE = (byte) 0x8D;
    private static final byte CMD_GET_WORKOUT_INTERVAL_COUNT = (byte) 0x9F;
    private static final byte CMD_GET_INTERVAL_TYPE = (byte) 0x8E;
    private static final byte CMD_GET_REST_TIME = (byte) 0xCF;
    private static final byte CMD_SET_SPLIT_DURATION = (byte) 0x05;
    private static final byte CMD_GET_FORCE_PLOT = (byte) 0x6B;
    private static final byte CMD_GET_HEART_RATE_PLOT = (byte) 0x6C;
    private static final byte CMD_SET_SCREEN_ERROR_MODE = (byte) 0x27;

    public interface Command<R extends PaceMonitorResult> {

        ResultCallback<R> getResultCallback();
    }

    public static Command<PaceMonitorResult> getStatusCmd(
            ResultCallback<PaceMonitorResult> callback) {
        return new CommandImpl(CMD_GET_STATUS, callback);
    }

    public static Command<GetDistanceResult> getOdometerCmd(
            ResultCallback<GetDistanceResult> callback) {
        return new CommandImpl(CMD_GET_ODOMETER, callback);
    }

    public static Command<GetTimeResult> getWorkTimeCmd(
            ResultCallback<GetTimeResult> callback) {
        return new CommandImpl(CMD_GET_WORK_TIME, callback);
    }

    public static Command<GetDistanceResult> getWorkDistanceCmd(
            ResultCallback<GetDistanceResult> callback) {
        return new CommandImpl(CMD_GET_WORK_DISTANCE, callback);
    }

    public static Command<GetCaloriesResult> getWorkCaloriesCmd(
            ResultCallback<GetCaloriesResult> callback) {
        return new CommandImpl(CMD_GET_WORK_CALORIES, callback);
    }

    public static Command<GetWorkoutNumberResult> getStoredWorkoutNumberCmd(
            ResultCallback<GetWorkoutNumberResult> callback) {
        return new CommandImpl(CMD_GET_STORED_WORKOUT_NUMBER, callback);
    }

    public static Command<GetPaceResult> getPaceCmd(
            ResultCallback<GetPaceResult> callback) {
        return new CommandImpl(CMD_GET_PACE, callback);
    }

    public static Command<GetStrokeStateResult> getStrokeRateCmd(
            ResultCallback<GetStrokeStateResult> callback) {
        return new CommandImpl(CMD_GET_STROKE_RATE, callback);
    }

    public static Command<GetUserInfoResult> getUserInfoCmd(
            ResultCallback<GetUserInfoResult> callback) {
        return new CommandImpl(CMD_GET_USER_INFO, callback);
    }

    public static Command<GetHeartRateResult> getHeartRateCmd(
            ResultCallback<GetHeartRateResult> callback) {
        return new CommandImpl(CMD_GET_HEART_RATE, callback);
    }

    public static Command<GetPowerResult> getPowerCmd(
            ResultCallback<GetPowerResult> callback) {
        return new CommandImpl(CMD_GET_POWER, callback);
    }

    public static Command<GetWorkoutTypeResult> getWorkoutTypeCmd(
            ResultCallback<GetWorkoutTypeResult> callback) {
        return new CommandImpl(CMD_GET_WORKOUT_TYPE, true /* isCustomCommand */, callback);
    }

    public static Command<GetDragFactorResult> getDragFactorCmd(
            ResultCallback<GetDragFactorResult> callback) {
        return new CommandImpl(CMD_GET_DRAG_FACTOR, true /* isCustomCommand */, callback);
    }

    public static Command<GetStrokeStateResult> getStrokeStateCmd(
            ResultCallback<GetStrokeStateResult> callback) {
        return new CommandImpl(CMD_GET_STROKE_STATE, true /* isCustomCommand */, callback);
    }

    public static Command<GetHighResWorkTimeResult> getHighResolutionWorkTimeCmd(
            ResultCallback<GetHighResWorkTimeResult> callback) {
        return new CommandImpl(CMD_GET_HIGH_RES_WORK_TIME, true /* isCustomCommand */, callback);
    }

    public static Command<GetHighResWorkDistanceResult> getHighResolutionWorkDistanceCmd(
            ResultCallback<GetHighResWorkDistanceResult> callback) {
        return new CommandImpl(CMD_GET_HIGH_RES_WORK_DISTANCE, true /* isCustomCommand */,
                callback);
    }

    public static Command<GetErrorValueResult> getErrorValueCmd(
            ResultCallback<GetErrorValueResult> callback) {
        return new CommandImpl(CMD_GET_ERROR_VALUE, true /* isCustomCommand */, callback);
    }

    public static Command<GetWorkoutStateResult> getWorkoutStateCmd(
            ResultCallback<GetWorkoutStateResult> callback) {
        return new CommandImpl(CMD_GET_WORKOUT_STATE, true /* isCustomCommand */, callback);
    }

    public static Command<GetWorkoutIntervalCountResult> getWorkoutIntervalCountCmd(
            ResultCallback<GetWorkoutIntervalCountResult> callback) {
        return new CommandImpl(CMD_GET_WORKOUT_INTERVAL_COUNT, true /* isCustomCommand */,
                callback);
    }

    public static Command<GetIntervalTypeResult> getIntervalTypeCmd(
            ResultCallback<GetIntervalTypeResult> callback) {
        return new CommandImpl(CMD_GET_INTERVAL_TYPE, true /* isCustomCommand */, callback);
    }

    public static Command<GetRestTimeResult> getRestTimeCmd(
            ResultCallback<GetRestTimeResult> callback) {
        return new CommandImpl(CMD_GET_REST_TIME, true /* isCustomCommand */, callback);
    }

    public static Command<GetForcePlotResult> getForcePlotCmd(
            ResultCallback<GetForcePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        byte[] data = new byte[] { 0x01, (byte) (numSamples * 2) };
        return new CommandImpl(CMD_GET_FORCE_PLOT, true /* isCustomCommand */, data, callback);
    }

    public static Command<GetHeartRatePlotResult> getHeartRatePlotCmd(
            ResultCallback<GetHeartRatePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        byte[] data = new byte[] { 0x01, (byte) (numSamples * 2) };
        return new CommandImpl(CMD_GET_HEART_RATE_PLOT, true /* isCustomCommand */, data, callback);
    }

    public static Command<PaceMonitorResult> setTimeCmd(ResultCallback<PaceMonitorResult> callback,
            int hours, int minutes, int seconds) {
        PaceMonitorImpl.validateTime(hours, minutes, seconds);
        byte[] data = new byte[] { 0x03, toByte(hours), toByte(minutes), toByte(seconds) };
        return new CommandImpl(CMD_SET_TIME, data, callback);
    }

    public static Command<PaceMonitorResult> setDateCmd(ResultCallback<PaceMonitorResult> callback,
            int year, int month, int day) {
        PaceMonitorImpl.validateDate(year, month, day);
        year -= 1900;
        byte[] data = new byte[] { 0x03, toByte(year), toByte(month), toByte(day) };
        return new CommandImpl(CMD_SET_DATE, data, callback);
    }

    public static Command<PaceMonitorResult> setTimeoutCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateTimeout(seconds);
        byte[] data = new byte[] { 0x01, toByte(seconds) };
        return new CommandImpl(CMD_SET_TIMEOUT, data, callback);
    }

    public static Command<PaceMonitorResult> setGoalTimeCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateGoalTime(seconds);
        byte hoursByte = toByte(TimeUnit.SECONDS.toHours(seconds));
        seconds -= TimeUnit.HOURS.toSeconds(hoursByte);
        byte minutesByte = toByte(TimeUnit.SECONDS.toMinutes(seconds));
        seconds -= TimeUnit.MINUTES.toSeconds(minutesByte);
        byte secondsByte = toByte(seconds);
        byte[] data = new byte[] { 0x03, hoursByte, minutesByte, secondsByte };
        return new CommandImpl(CMD_SET_GOAL_TIME, data, callback);
    }

    public static Command<PaceMonitorResult> setGoalDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, int meters) {
        PaceMonitorImpl.validateGoalDistance(meters);
        byte[] data = new byte[] { getByte(meters, 0), getByte(meters, 1), CsafeUnitUtil.METER };
        return new CommandImpl(CMD_SET_GOAL_DISTANCE, data, callback);
    }

    public static Command<PaceMonitorResult> setGoalCaloriesCmd(
            ResultCallback<PaceMonitorResult> callback, int calories) {
        PaceMonitorImpl.validateGoalCalories(calories);
        byte[] data = new byte[] { (byte) 0x02, getByte(calories, 0), getByte(calories, 1) };
        return new CommandImpl(CMD_SET_GOAL_CALORIES, data, callback);
    }

    public static Command<PaceMonitorResult> setGoalPowerCmd(
            ResultCallback<PaceMonitorResult> callback, int watts) {
        PaceMonitorImpl.validateGoalPower(watts);
        byte[] data = new byte[] { getByte(watts, 0), getByte(watts, 1), CsafeUnitUtil.WATTS };
        return new CommandImpl(CMD_SET_GOAL_POWER, data, callback);
    }

    public static Command<PaceMonitorResult> setStoredWorkoutNumberCmd(
            ResultCallback<PaceMonitorResult> callback, int workoutNumber) {
        PaceMonitorImpl.validateWorkoutNumber(workoutNumber);
        byte[] data = new byte[] { toByte(workoutNumber), 0x00 };
        return new CommandImpl(CMD_SET_STORED_WORKOUT_NUMBER, data, callback);
    }

    public static Command<PaceMonitorResult> setSplitTimeCmd(
            ResultCallback<PaceMonitorResult> callback, double seconds) {
        PaceMonitorImpl.validateSplitTime(seconds);
        int duration = (int) (seconds * 100);
        byte[] data = new byte[] { 0x05, 0x00 /* time */, getByte(duration, 0),
                getByte(duration, 1), getByte(duration, 2), getByte(duration, 3) };
        return new CommandImpl(CMD_SET_SPLIT_DURATION, true /* isCustomCommand */, data, callback);
    }

    public static Command<PaceMonitorResult> setSplitDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, int meters) {
        PaceMonitorImpl.validateSplitDistance(meters);
        byte[] data = new byte[] { (byte) 0x80 /* distance */, getByte(meters, 0),
                getByte(meters, 1), getByte(meters, 2), getByte(meters, 3) };
        return new CommandImpl(CMD_SET_SPLIT_DURATION, true /* isCustomCommand */, data, callback);
    }

    public static Command<PaceMonitorResult> setScreenErrorModeCmd(
            ResultCallback<PaceMonitorResult> callback, boolean enabled) {
        byte[] data = new byte[] { 0x01, (byte) (enabled ? 0x01 : 0x00) };
        return new CommandImpl(CMD_SET_SCREEN_ERROR_MODE, true /* isCustomCommand */, data,
                callback);
    }

    public static Command<PaceMonitorResult> setPaceMonitorState(
            ResultCallback<PaceMonitorResult> callback, int state) {
        PaceMonitorImpl.validatePaceMonitorState(state);
        byte command;
        switch (state) {
            case PaceMonitor.PaceMonitorState.RESET:
                command = CMD_GO_RESET;
                break;
            case PaceMonitor.PaceMonitorState.IDLE:
                command = CMD_GO_IDLE;
                break;
            case PaceMonitor.PaceMonitorState.HAVE_ID:
                command = CMD_GO_HAVE_ID;
                break;
            case PaceMonitor.PaceMonitorState.IN_USE:
                command = CMD_GO_IN_USE;
                break;
            case PaceMonitor.PaceMonitorState.FINISHED:
                command = CMD_GO_FINISHED;
                break;
            case PaceMonitor.PaceMonitorState.READY:
                command = CMD_GO_READY;
                break;
            case PaceMonitor.PaceMonitorState.BAD_ID:
                command = CMD_GO_BAD_ID;
                break;
            default:
                throw new IllegalArgumentException("Invalid pace monitor state " + state);
        }
        return new CommandImpl(command, callback);
    }

    private static byte toByte(int val) {
        return (byte) (val & 0xFF);
    }

    private static byte toByte(long val) {
        return (byte) (val & 0xFF);
    }

    private static byte getByte(int val, int idx) {
        return (byte) ((val >> (idx * 8)) & 0xFF);
    }
}

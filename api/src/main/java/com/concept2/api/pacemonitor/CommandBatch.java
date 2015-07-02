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

public class CommandBatch {

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
    private static final String ARG_STATE = "state";

    private static final int CMD_GET_STATUS = 0;
    private static final int CMD_GET_ODOMETER = 1;
    private static final int CMD_GET_WORK_TIME = 2;
    private static final int CMD_GET_WORK_DISTANCE = 3;
    private static final int CMD_GET_WORK_CALORIES = 4;
    private static final int CMD_GET_STORED_WORKOUT_NUMBER = 5;
    private static final int CMD_GET_PACE = 6;
    private static final int CMD_GET_STROKE_RATE = 7;
    private static final int CMD_GET_USER_INFO = 8;
    private static final int CMD_GET_HEART_RATE = 9;
    private static final int CMD_GET_POWER = 10;
    private static final int CMD_GET_WORKOUT_TYPE = 11;
    private static final int CMD_GET_DRAG_FACTOR = 12;
    private static final int CMD_GET_STROKE_STATE = 13;
    private static final int CMD_GET_HIGH_RES_WORK_TIME = 14;
    private static final int CMD_GET_HIGH_RES_WORK_DISTANCE = 15;
    private static final int CMD_GET_ERROR_VALUE = 16;
    private static final int CMD_GET_WORKOUT_STATE = 17;
    private static final int CMD_GET_WORKOUT_INTERVAL_COUNT = 18;
    private static final int CMD_GET_INTERVAL_TYPE = 19;
    private static final int CMD_GET_REST_TIME = 20;
    private static final int CMD_GET_FORCE_PLOT = 21;
    private static final int CMD_GET_HEART_RATE_PLOT = 22;

    private static final int CMD_SET_TIME = 23;
    private static final int CMD_SET_DATE = 24;
    private static final int CMD_SET_TIMEOUT = 25;
    private static final int CMD_SET_GOAL_TIME = 26;
    private static final int CMD_SET_GOAL_DISTANCE = 27;
    private static final int CMD_SET_GOAL_CALORIES = 28;
    private static final int CMD_SET_GOAL_POWER = 29;
    private static final int CMD_SET_STORED_WORKOUT_NUMBER = 30;
    private static final int CMD_SET_SPLIT_TIME = 31;
    private static final int CMD_SET_SPLIT_DISTANCE = 32;
    private static final int CMD_SET_SCREEN_ERROR_MODE = 33;
    private static final int CMD_SET_PACE_MONITOR_STATE = 34;

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
        return new CommandImpl(CMD_GET_WORKOUT_TYPE, callback);
    }

    public static Command<GetDragFactorResult> getDragFactorCmd(
            ResultCallback<GetDragFactorResult> callback) {
        return new CommandImpl(CMD_GET_DRAG_FACTOR, callback);
    }

    public static Command<GetStrokeStateResult> getStrokeStateCmd(
            ResultCallback<GetStrokeStateResult> callback) {
        return new CommandImpl(CMD_GET_STROKE_STATE, callback);
    }

    public static Command<GetHighResWorkTimeResult> getHighResolutionWorkTimeCmd(
            ResultCallback<GetHighResWorkTimeResult> callback) {
        return new CommandImpl(CMD_GET_HIGH_RES_WORK_TIME, callback);
    }

    public static Command<GetHighResWorkDistanceResult> getHighResolutionWorkDistanceCmd(
            ResultCallback<GetHighResWorkDistanceResult> callback) {
        return new CommandImpl(CMD_GET_HIGH_RES_WORK_DISTANCE, callback);
    }

    public static Command<GetErrorValueResult> getErrorValueCmd(
            ResultCallback<GetErrorValueResult> callback) {
        return new CommandImpl(CMD_GET_ERROR_VALUE, callback);
    }

    public static Command<GetWorkoutStateResult> getWorkoutStateCmd(
            ResultCallback<GetWorkoutStateResult> callback) {
        return new CommandImpl(CMD_GET_WORKOUT_STATE, callback);
    }

    public static Command<GetWorkoutIntervalCountResult> getWorkoutIntervalCountCmd(
            ResultCallback<GetWorkoutIntervalCountResult> callback) {
        return new CommandImpl(CMD_GET_WORKOUT_INTERVAL_COUNT, callback);
    }

    public static Command<GetIntervalTypeResult> getIntervalTypeCmd(
            ResultCallback<GetIntervalTypeResult> callback) {
        return new CommandImpl(CMD_GET_INTERVAL_TYPE, callback);
    }

    public static Command<GetRestTimeResult> getRestTimeCmd(
            ResultCallback<GetRestTimeResult> callback) {
        return new CommandImpl(CMD_GET_REST_TIME, callback);
    }

    public static Command<GetForcePlotResult> getForcePlotCmd(
            ResultCallback<GetForcePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_SAMPLES, numSamples);
        return new CommandImpl(CMD_GET_FORCE_PLOT, args, callback);
    }

    public static Command<GetHeartRatePlotResult> getHeartRatePlotCmd(
            ResultCallback<GetHeartRatePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_SAMPLES, numSamples);
        return new CommandImpl(CMD_GET_HEART_RATE_PLOT, args, callback);
    }

    public static Command<PaceMonitorResult> setTimeCmd(ResultCallback<PaceMonitorResult> callback,
            int hours, int minutes, int seconds) {
        PaceMonitorImpl.validateTime(hours, minutes, seconds);
        Bundle args = new Bundle();
        args.putInt(ARG_HOURS, hours);
        args.putInt(ARG_MINUTES, minutes);
        args.putInt(ARG_SECONDS, seconds);
        return new CommandImpl(CMD_SET_TIME, args, callback);
    }

    public static Command<PaceMonitorResult> setDateCmd(ResultCallback<PaceMonitorResult> callback,
            int year, int month, int day) {
        PaceMonitorImpl.validateDate(year, month, day);
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        return new CommandImpl(CMD_SET_DATE, args, callback);
    }

    public static Command<PaceMonitorResult> setTimeoutCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateTimeout(seconds);
        Bundle args = new Bundle();
        args.putInt(ARG_SECONDS, seconds);
        return new CommandImpl(CMD_SET_TIMEOUT, args, callback);
    }

    public static Command<PaceMonitorResult> setGoalTimeCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateGoalTime(seconds);
        Bundle args = new Bundle();
        args.putInt(ARG_SECONDS, seconds);
        return new CommandImpl(CMD_SET_GOAL_TIME, args, callback);
    }

    public static Command<PaceMonitorResult> setGoalDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, int meters) {
        PaceMonitorImpl.validateGoalDistance(meters);
        Bundle args = new Bundle();
        args.putInt(ARG_METERS, meters);
        return new CommandImpl(CMD_SET_GOAL_DISTANCE, args, callback);
    }

    public static Command<PaceMonitorResult> setGoalCaloriesCmd(
            ResultCallback<PaceMonitorResult> callback, int calories) {
        PaceMonitorImpl.validateGoalCalories(calories);
        Bundle args = new Bundle();
        args.putInt(ARG_CALORIES, calories);
        return new CommandImpl(CMD_SET_GOAL_CALORIES, args, callback);
    }

    public static Command<PaceMonitorResult> setGoalPowerCmd(
            ResultCallback<PaceMonitorResult> callback, int watts) {
        PaceMonitorImpl.validateGoalPower(watts);
        Bundle args = new Bundle();
        args.putInt(ARG_WATTS, watts);
        return new CommandImpl(CMD_SET_GOAL_POWER, args, callback);
    }

    public static Command<PaceMonitorResult> setStoredWorkoutNumberCmd(
            ResultCallback<PaceMonitorResult> callback, int workoutNumber) {
        PaceMonitorImpl.validateWorkoutNumber(workoutNumber);
        Bundle args = new Bundle();
        args.putInt(ARG_STORED_WORKOUT_NUMBER, workoutNumber);
        return new CommandImpl(CMD_SET_STORED_WORKOUT_NUMBER, args, callback);
    }

    public static Command<PaceMonitorResult> setSplitTimeCmd(
            ResultCallback<PaceMonitorResult> callback, double seconds) {
        PaceMonitorImpl.validateSplitTime(seconds);
        Bundle args = new Bundle();
        args.putDouble(ARG_SECONDS, seconds);
        return new CommandImpl(CMD_SET_SPLIT_TIME, args, callback);
    }

    public static Command<PaceMonitorResult> setSplitDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, double meters) {
        PaceMonitorImpl.validateSplitDistance(meters);
        Bundle args = new Bundle();
        args.putDouble(ARG_METERS, meters);
        return new CommandImpl(CMD_SET_SPLIT_DISTANCE, args, callback);
    }

    public static Command<PaceMonitorResult> setScreenErrorModeCmd(
            ResultCallback<PaceMonitorResult> callback, boolean enabled) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_SCREEN_ERROR_MODE, enabled);
        return new CommandImpl(CMD_SET_SCREEN_ERROR_MODE, args, callback);
    }

    public static Command<PaceMonitorResult> setPaceMonitorState(
            ResultCallback<PaceMonitorResult> callback, int state) {
        PaceMonitorImpl.validatePaceMonitorState(state);
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, state);
        return new CommandImpl(CMD_SET_PACE_MONITOR_STATE, args, callback);
    }
}

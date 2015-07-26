package com.concept2.api.service.broker;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.common.Constants;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitorResult;
import com.concept2.api.pacemonitor.internal.CommandImpl;
import com.concept2.api.pacemonitor.internal.CreateCommandBatchResultRef;
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
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.CommandBatch;
import com.concept2.api.service.broker.pacemonitor.CommandBatchCache;
import com.concept2.api.service.broker.pacemonitor.Csafe;
import com.concept2.api.service.broker.pacemonitor.Engine;
import com.concept2.api.service.broker.pacemonitor.GetPMData;
import com.concept2.api.service.broker.pacemonitor.ReportId;
import com.concept2.api.service.broker.pacemonitor.USBEngine;
import com.concept2.api.service.broker.pacemonitor.util.CsafeUnitUtil;
import com.concept2.api.utils.Objects;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
//        byte SET_PM_CGF = (byte) 0x76;
//        byte SET_PM_DATA = (byte) 0x77;
//        byte GET_PM_CFG = (byte) 0x7E;
//        byte GET_PM_DATA = (byte) 0x7F;

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

    /** Cache of command batches. */
    private final CommandBatchCache mCommandBatchCache = CommandBatchCache.getInstance();

    // TODO - Add a BT engine.

    public DataHolder executeCommand(Context context, CommandImpl command) {
        GetPMData.ValueResult result = GetPMData.executeCommandRaw(context, mUsbEngine,
                command.getCommandBytes());
        if (!result.isSuccess()) {
            return DataHolder.empty(result.getStatusCode());
        }
        ContentValues values = command.handleResult(result.getPaceMonitorStatus(),
                result.getData());
        if (values == null) {
            return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_DATA_ERROR);
        }
        return new DataHolder.Builder()
                .withValues(values)
                .build(result.getStatusCode());
    }

    /**
     * Execute one or more commands in order as a batch. Commands should be created via the static
     * methods provided in {@link CommandBuilder}.
     *
     * @param context The calling context.
     * @param commandList The list of commands to execute.
     * @return The command results as a {@link DataHolder}.
     */
    public DataHolder createCommandBatch(Context context, List<CommandBuilder.Command> commandList) {
        CommandBatch batch = CommandBatch.build(commandList);
        int id = mCommandBatchCache.saveCommandBatch(batch);
        if (id < 0) {
            return DataHolder.empty(Concept2StatusCodes.INTERNAL_ERROR);
        }
        ContentValues values = new ContentValues();
        values.put(PaceMonitorColumnContract.COMMAND_BATCH_ID, id);
        return new DataHolder.Builder()
                .withValues(values)
                .build(Concept2StatusCodes.OK);
    }

    public DataHolder executeCommandBatch(Context context, int id) {
        CommandBatch batch = mCommandBatchCache.findCommandBatch(id);
        if (batch == null) {
            return DataHolder.empty(Concept2StatusCodes.COMMAND_NOT_FOUND);
        }
        DataHolder.Builder resultBuilder = new DataHolder.Builder();
        for (int i = 0, size = batch.size(); i < size; ++i) {
            byte[] command = batch.getCommand(i);
            Log.d(TAG, "Executing batch " + id + "." + i + ": " + Objects.toString(command));
            GetPMData.ValueResult result = GetPMData.executeCommandRaw(context, mUsbEngine,
                    command);
            Log.d(TAG, "    result: " + result);
            if (!result.isSuccess()) {
                // Failed to execute batch. Stop here.
                return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR);
            }
            ArrayList<CommandImpl> commandArray = batch.getCommandArray(i);
            for (int j = 0, commandSize = commandArray.size(); j < commandSize; ++j) {
                Log.d(TAG, "    Parsing result " + j + " / " + commandSize);
                ContentValues values = commandArray.get(j)
                        .handleResult(result.getPaceMonitorStatus(), result.getData());
                Log.d(TAG, "        values: " + Objects.toString(values));
                if (values == null) {
                    // Error handling the result, bail.
                    return DataHolder.empty(Concept2StatusCodes.PACE_MONITOR_DATA_ERROR);
                }
                resultBuilder.withValues(values);
            }
        }
        return resultBuilder.build(Concept2StatusCodes.OK);
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

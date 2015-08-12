package com.concept2.api.pacemonitor.internal;

import android.content.Context;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitorResult;
import com.concept2.api.pacemonitor.PaceMonitorStatus;
import com.concept2.api.service.Concept2AsyncTaskService;
import com.concept2.api.utils.Preconditions;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Implementation of the {@link PaceMonitor} APIs.
 */
public class PaceMonitorImpl implements PaceMonitor {

    private static class FailedPaceMonitorResult implements PaceMonitorResult {
        private final int mStatusCode;
        public FailedPaceMonitorResult(int statusCode) {
            mStatusCode = statusCode;
        }
        @Override
        public PaceMonitorStatus getPaceMonitorStatus() {
            return null;
        }

        @Override
        public int getStatus() {
            return mStatusCode;
        }
    }

    private static final class BatchCreatePendingResult extends
            PendingResultImpl<CreateBatchCommandResult> {
        @Override
        protected CreateBatchCommandResult getFailedResult(final int statusCode) {
            return new CreateBatchCommandResult() {
                @Override
                public int getBatchId() {
                    return -1;
                }

                @Override
                public int getStatus() {
                    return statusCode;
                }
            };
        }
    }

    private static final class BatchPendingResult extends PendingResultImpl<BatchResult> {
        @Override
        protected BatchResult getFailedResult(final int statusCode) {
            return new BatchResult() {

                @Override
                public List<PaceMonitorResult> getResults() {
                    return null;
                }

                @Override
                public int getStatus() {
                    return statusCode;
                }
            };
        }
    }

    @Override
    public <R extends PaceMonitorResult> PendingResult<R> executeCommand(Context context,
            final CommandBuilder.Command<R> command) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertNotNull(command, "Command cannot be null");
        Preconditions.assertTrue(command instanceof CommandImpl);
        final CommandImpl<R> cmd = (CommandImpl) command;
        PendingResultImpl<R>  pendingResult = new PendingResultImpl<R>() {
            @Override
            protected R getFailedResult(int statusCode) {
                return cmd.getResult(DataHolder.empty(statusCode), 0 /* row */);
            }
        };
        if (command.getResultCallback() != null) {
            pendingResult.setResultCallback(command.getResultCallback());
        }
        Concept2AsyncTaskService.executePaceMonitorCommand(context, pendingResult, cmd);
        return pendingResult;
    }

    @Override
    public PendingResult<CreateBatchCommandResult> createCommandBatch(Context context,
            List<CommandBuilder.Command> commandList) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertNotNull(commandList, "Command list cannot be null");
        Preconditions.assertTrue(!commandList.isEmpty(), "Command list cannot be empty");
        for (CommandBuilder.Command command : commandList) {
            Preconditions.assertTrue(command instanceof CommandImpl,
                    "Command must be created by CommandBatch static methods");
        }
        BatchCreatePendingResult pendingResult = new BatchCreatePendingResult();
        Concept2AsyncTaskService.createCommandBatch(context, pendingResult, commandList);
        return pendingResult;
    }

    @Override
    public PendingResult<BatchResult> executeCommandBatch(Context context, int id) {
        Preconditions.assertNotNull(context, "Context cannot be null");
        Preconditions.assertTrue(id >= 0, "Id must be >= 0");
        BatchPendingResult pendingResult = new BatchPendingResult();
        Concept2AsyncTaskService.executeCommandBatch(context, pendingResult, id);
        return pendingResult;
    }

    public static void validatePaceMonitorState(int state) {
        switch (state) {
            case PaceMonitorState.RESET:
            case PaceMonitorState.IDLE:
            case PaceMonitorState.HAVE_ID:
            case PaceMonitorState.IN_USE:
            case PaceMonitorState.FINISHED:
            case PaceMonitorState.READY:
            case PaceMonitorState.BAD_ID:
                return;
            default:
                Preconditions.assertTrue(false, "State " + state + " not recognized");
        }
    }

    public static void validateTime(int hours, int minutes, int seconds) {
        Preconditions.assertTrue(hours >= 0 && hours < 24, "Hours must be between 0 and 23");
        Preconditions.assertTrue(minutes >= 0 && minutes < 60,
                "Minutes must be between 0 and 59");
        Preconditions.assertTrue(seconds >= 0 && seconds < 60,
                "Seconds must be between 0 and 59");
    }

    public static void validateDate(int year, int month, int day) {
        Preconditions.assertTrue(year >= 1900 && year <= 2155,
                "Year must be in range [1900, 2155]");
        Preconditions.assertTrue(month >= 1 && month <= 12,
                "Month must be in range [1, 12]");
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Preconditions.assertTrue(day >= 1 && day <= daysInMonth,
                "Day must be in range [1, " + daysInMonth + "] for " + month + "/" + year);
    }

    public static void validateTimeout(int seconds) {
        Preconditions.assertTrue(seconds >= 0 && seconds <= 0xFF,
                "Timeout must be in range [0, 255] seconds");
    }

    public static void validateGoalTime(int seconds) {
        Preconditions.assertTrue(seconds >= 20 && seconds < 36000,
                "Goal time must be in range [0, 36000) seconds");
    }

    public static void validateGoalDistance(int meters) {
        Preconditions.assertTrue(meters >= 100 && meters <= 50000,
                "Goal distance must be in range [100, 50000] meters");
    }

    public static void validateGoalCalories(int calories) {
        Preconditions.assertTrue(calories >= 0 && calories <= 0xFFFF,
                "Goal calories must be in the range [0, 65535] calories");
    }

    public static void validateWorkoutNumber(int workoutNumber) {
        Preconditions.assertTrue(workoutNumber >= 0 && workoutNumber < 16,
                "Workout number must be in the range [0, 15]");
    }

    public static void validateGoalPower(int watts) {
        Preconditions.assertTrue(watts >= 0 && watts <= 0xFFFF,
                "Goal power must be in the range [0, 65535] watts");
    }

    public static void validateSplitTime(double seconds) {
        Preconditions.assertTrue(seconds >= 20.0, "Split time must be at least 20 seconds");
    }

    public static void validateSplitDistance(double meters) {
        Preconditions.assertTrue(meters >= 100.0, "Split distance must be at least 100 meters");
    }

    public static void validateNumSamples(int numSamples) {
        Preconditions.assertTrue(numSamples >= 0 && numSamples <= 16,
                "Number of samples must be in range [0-16]");
    }
}

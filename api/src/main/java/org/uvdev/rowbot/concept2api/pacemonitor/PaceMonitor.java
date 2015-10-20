package org.uvdev.rowbot.concept2api.pacemonitor;

import android.content.Context;

import org.uvdev.rowbot.concept2api.PendingResult;
import org.uvdev.rowbot.concept2api.Result;
import org.uvdev.rowbot.concept2api.ResultCallback;

import java.util.List;

/**
 * Pace Monitor APIs.
 */
public interface PaceMonitor {

    /**
     * States of the pace monitor reported by any {@link PaceMonitorResult}. Or set via
     * {@link CommandBuilder#setPaceMonitorState(ResultCallback, int)}
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
     * @see CommandBuilder#getUserInfoCmd(ResultCallback)
     */
    interface Gender {
        /** No gender specified. */
        int NONE = 0;
        /** User specified male. */
        int MALE = 1;
        /** User specified female. */
        int FEMALE = 2;
    }

    /**
     * The workout types reported by {@link GetWorkoutTypeResult}.
     * @see CommandBuilder#getWorkoutTypeCmd(ResultCallback)
     */
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

    /**
     * The current workout type reported by {@link GetWorkoutStateResult}.
     * @see CommandBuilder#getWorkoutStateCmd(ResultCallback)
     */
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

    /**
     * The current interval type reported by {@link GetIntervalTypeResult}.
     * @see CommandBuilder#getIntervalTypeCmd(ResultCallback)
     */
    interface IntervalType {
        int INVALID = Integer.MIN_VALUE;
        int TIME = 0;
        int DISTANCE = 1;
        int REST = 2;
    }

    /**
     * The current stroke state reported by {@link GetStrokeStateResult}.
     * @see CommandBuilder#getStrokeStateCmd(ResultCallback)
     */
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

    /** Result reporting the creation of a command batch. */
    interface CreateBatchCommandResult extends Result {
        int getBatchId();
    }

    /** Result reporting results for a batch of commands. */
    interface BatchResult extends Result {
        List<PaceMonitorResult> getResults();
    }

    //////////////////////////////
    // Beginning of API
    //////////////////////////////

    /**
     * Execute a single command created by {@link CommandBuilder}. If the command's callback object
     * was set it will be used in the returned {@link PendingResult} by default.
     *
     * @param context The calling context.
     * @param command The command to execute.
     * @param <R> The return result type matching the executing command.
     * @return {@link PendingResult} that will report the result when ready.
     */
    <R extends PaceMonitorResult> PendingResult<R> executeCommand(Context context,
            CommandBuilder.Command<R> command);
    /**
     * Creates and caches the communication bytes required to execute a batch of commands. When
     * successful, this method will return an identifier for the batch that can be executed via
     * {@link #executeCommandBatch(Context, int}.
     *
     * @param context The calling context.
     * @param commandList The list of commands to execute.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<CreateBatchCommandResult> createCommandBatch(Context context,
            List<CommandBuilder.Command> commandList);

    /**
     * Executes a command batch matching the provided id created by
     * {@link #createCommandBatch(Context, List)}.
     *
     * @param context The calling context.
     * @param id The id of the command batch.
     * @return {@link PendingResult} that will report the result when ready.
     */
    PendingResult<BatchResult> executeCommandBatch(Context context, int id);
}

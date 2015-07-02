package com.concept2.api.service;

import android.content.Context;
import android.util.SparseArray;

import com.concept2.api.PendingResult;
import com.concept2.api.Result;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.pacemonitor.CommandBatch;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitorResult;
import com.concept2.api.pacemonitor.PaceMonitorStatus;
import com.concept2.api.pacemonitor.internal.GetCaloriesResultRef;
import com.concept2.api.pacemonitor.internal.GetDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetDragFactorResultRef;
import com.concept2.api.pacemonitor.internal.GetErrorValueResultRef;
import com.concept2.api.pacemonitor.internal.GetForcePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRatePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRateResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetIntervalTypeResultRef;
import com.concept2.api.pacemonitor.internal.GetPaceResultRef;
import com.concept2.api.pacemonitor.internal.GetPowerResultRef;
import com.concept2.api.pacemonitor.internal.GetRestTimeResultRef;
import com.concept2.api.pacemonitor.internal.PaceMonitorResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeRateResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeStateResultRef;
import com.concept2.api.pacemonitor.internal.GetTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetUserInfoResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutIntervalCountResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutNumberResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutStateResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutTypeResultRef;
import com.concept2.api.service.broker.DataBroker;
import com.concept2.api.service.operations.BaseDataOperation;
import com.concept2.api.service.operations.BaseOperation;
import com.concept2.api.utils.Objects;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Async task executor class. This class is responsible for executing the API methods in parallel
 * when possible.
 */
public class Concept2AsyncTaskService {

    /** Array of single thread executors, one per affinity. */
    private static final SparseArray<ExecutorService> sExecutors = new SparseArray<>();

    /** The different affinities to execute commands. */
    private interface Affinity {
        int DEFAULT = 0;
        int PACE_MONITOR = 1;
        int LOGBOOK = 2;
    }

    /**
     * Get or create an {@link ExecutorService} associated with the given affinity.
     *
     * @param affinity The affinity to get the executor for,
     * @return The associate executor.
     */
    private static ExecutorService getExecutor(int affinity) {
        synchronized (sExecutors) {
            ExecutorService service = sExecutors.get(affinity);
            if (service == null) {
                service = Executors.newSingleThreadExecutor();
                sExecutors.put(affinity, service);
            }
            return service;
        }
    }

    /**
     * Execute an operation.
     *
     * @param context The {@link Context} executing the operation.
     * @param affinity The affinity of the operation.
     * @param operation The {@link BaseOperation} to execute.
     */
    private static void execute(Context context, int affinity, BaseOperation operation) {
        getExecutor(affinity).execute(new OperationRunnable(context, operation));
    }

    /**
     * Runnable to execute in the executer task.
     */
    private static final class OperationRunnable implements Runnable {

        /** The context that is requesting the operation. */
        private final Context mContext;

        /** The operation to execute. */
        private final BaseOperation mBaseOperation;

        /**
         * @param context The context that is requesting the operation.
         * @param baseOperation The operation to execute.
         */
        public OperationRunnable(Context context, BaseOperation baseOperation) {
            mContext = context;
            mBaseOperation = baseOperation;
        }

        @Override
        public void run() {
            mBaseOperation.run(DataBroker.getInstance(mContext), mContext);
        }
    }

    /**
     * Simple status reporting operation to send a default {@link Result} to a
     * {@link PendingResult}.
     */
    private static abstract class BaseStatusReportingOperation extends BaseDataOperation {
        private final PendingResultImpl<PaceMonitorResult> mPendingResult;
        public BaseStatusReportingOperation(PendingResultImpl<PaceMonitorResult> pendingResult) {
            mPendingResult = pendingResult;
        }

        @Override
        protected void onResult(DataHolder data) {
            mPendingResult.setResult(new PaceMonitorResultRef(data));
        }
    }

    /**
     * Get the status of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorStatus(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorStatus(context);
            }
        });
    }

    /**
     * Sets the state of the pace monitor. State must be one of
     * {@link PaceMonitor.PaceMonitorState}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param state The desired state of the pace monitor.
     */
    public static void setPaceMonitorState(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int state) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorState(context, state);
            }
        });
    }

    /**
     * Get the odometer reading of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorOdometer(Context context,
            final PendingResultImpl<PaceMonitor.GetDistanceResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorOdometer(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetDistanceResultRef(data));
            }
        });
    }

    /**
     * Get the work time reading of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkTime(Context context,
            final PendingResultImpl<PaceMonitor.GetTimeResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkTime(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetTimeResultRef(data));
            }
        });
    }

    /**
     * Get the work distance reading of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkDistance(Context context,
            final PendingResultImpl<PaceMonitor.GetDistanceResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkDistance(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetDistanceResultRef(data));
            }
        });
    }

    /**
     * Get the work calories reading of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkCalories(Context context,
            final PendingResultImpl<PaceMonitor.GetCaloriesResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkCalories(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetCaloriesResultRef(data));
            }
        });
    }

    /**
     * Get the currently running programmed/pre-stored workout number.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorStoredWorkoutNumber(Context context,
            final PendingResultImpl<PaceMonitor.GetWorkoutNumberResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorStoredWorkoutNumber(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetWorkoutNumberResultRef(data));
            }
        });
    }

    /**
     * Get the current pace.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorPace(Context context,
            final PendingResultImpl<PaceMonitor.GetPaceResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorPace(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetPaceResultRef(data));
            }
        });
    }

    /**
     * Get the current stroke rate.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorStrokeRate(Context context,
            final PendingResultImpl<PaceMonitor.GetStrokeRateResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorStrokeRate(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetStrokeRateResultRef(data));
            }
        });
    }

    /**
     * Get the pace monitor user's info.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorUserInfo(Context context,
            final PendingResultImpl<PaceMonitor.GetUserInfoResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorUserInfo(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetUserInfoResultRef(data));
            }
        });
    }

    /**
     * Get the current heart rate.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorHeartRate(Context context,
            final PendingResultImpl<PaceMonitor.GetHeartRateResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorHeartRate(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetHeartRateResultRef(data));
            }
        });
    }

    /**
     * Get the current power.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorPower(Context context,
            final PendingResultImpl<PaceMonitor.GetPowerResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorPower(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetPowerResultRef(data));
            }
        });
    }

    /**
     * Set the current time HH:MM:SS using a 24 hour clock.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param hours The hours to set [0-23].
     * @param minutes The minutes to set [0-59].
     * @param seconds The seconds to set [0-59].
     */
    public static void setPaceMonitorTime(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int hours,
            final int minutes, final int seconds) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorTime(context, hours, minutes, seconds);
            }
        });
    }

    /**
     * Set the current date.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param year The year to set [1900-2155].
     * @param month The month to set [1-12].
     * @param day The day of the month to set (range varies by month and year).
     */
    public static void setPaceMonitorDate(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int year, final int month,
            final int day) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorDate(context, year, month, day);
            }
        });
    }

    /**
     * Set the timeout for the current pace monitor state.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param seconds The number of seconds to timeout.
     */
    public static void setPaceMonitorTimeout(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int seconds) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorTimeout(context, seconds);
            }
        });
    }

    /**
     * Sets the goal time for the workout. Value must be at least 20 seconds and under 10 hours.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param seconds The number of seconds the user should row.
     */
    public static void setPaceMonitorGoalTime(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int seconds) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorGoalTime(context, seconds);
            }
        });
    }

    /**
     * Sets the goal distance for the workout in meters. Value must be between [100, 50000] meters.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param meters The number of meters the user should row.
     */
    public static void setPaceMonitorGoalDistance(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int meters) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorGoalDistance(context, meters);
            }
        });
    }

    /**
     * Sets the goal calories for the workout, Value must be between [0, 65535] calories.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param calories The number of calories the user should burn.
     */
    public static void setPaceMonitorGoalCalories(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int calories) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorGoalCalories(context, calories);
            }
        });
    }

    /**
     * Sets the goal power for the workout. Value must be between [0, 65535] watts.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param watts The power the user should produce.
     */
    public static void setPaceMonitorGoalPower(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int watts) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorGoalPower(context, watts);
            }
        });
    }

    /**
     * Sets the pace monitor to a programmed or pre-stored workout [0-15].
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param workoutNumber The workout number.
     */
    public static void setPaceMonitorStoredWorkoutNumber(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int workoutNumber) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorStoredWorkoutNumber(context, workoutNumber);
            }
        });
    }

    /**
     * Gets the current workout type.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkoutType(Context context,
            final PendingResultImpl<PaceMonitor.GetWorkoutTypeResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkoutType(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetWorkoutTypeResultRef(data));
            }
        });
    }

    /**
     * Gets the current drag factor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorDragFactor(Context context,
            final PendingResultImpl<PaceMonitor.GetDragFactorResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorDragFactor(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetDragFactorResultRef(data));
            }
        });
    }

    /**
     * Gets the current stroke state as one of {@link PaceMonitor.StrokeState}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorStrokeState(Context context,
            final PendingResultImpl<PaceMonitor.GetStrokeStateResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorStrokeState(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetStrokeStateResultRef(data));
            }
        });
    }

    /**
     * Gets the high resolution work time accurate to the 1/100th of a second.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorHighResWorkTime(Context context,
            final PendingResultImpl<PaceMonitor.GetHighResWorkTimeResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorHighResWorkTime(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetHighResWorkTimeResultRef(data));
            }
        });
    }


    /**
     * Gets the high resolution work distance accurate to the 1/10th of a meter.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorHighResWorkDistance(Context context,
            final PendingResultImpl<PaceMonitor.GetHighResWorkDistanceResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorHighResWorkDistance(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetHighResWorkDistanceResultRef(data));
            }
        });
    }

    /**
     * Gets and clears the latched error value in the pace monitor when the screen error display
     * mode is disabled.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorErrorValue(Context context,
            final PendingResultImpl<PaceMonitor.GetErrorValueResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorErrorValue(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetErrorValueResultRef(data));
            }
        });
    }

    /**
     * Gets the workout state as one of {@link PaceMonitor.WorkoutState}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkoutState(Context context,
            final PendingResultImpl<PaceMonitor.GetWorkoutStateResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkoutState(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetWorkoutStateResultRef(data));
            }
        });
    }

    /**
     * Gets the current workout interval count.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorWorkoutIntervalCount(Context context,
            final PendingResultImpl<PaceMonitor.GetWorkoutIntervalCountResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorWorkoutIntervalCount(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetWorkoutIntervalCountResultRef(data));
            }
        });
    }

    /**
     * Gets the current interval type as one of {@link PaceMonitor.IntervalType}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorIntervalType(Context context,
            final PendingResultImpl<PaceMonitor.GetIntervalTypeResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorIntervalType(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetIntervalTypeResultRef(data));
            }
        });
    }

    /**
     * Gets the current rest time in seconds.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorRestTime(Context context,
            final PendingResultImpl<PaceMonitor.GetRestTimeResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorRestTime(context);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetRestTimeResultRef(data));
            }
        });
    }

    /**
     * Sets the split to use time units with a given seconds value accurate to 1/100th of a second
     * and must be at least 20 seconds.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param seconds The split duration in seconds.
     */
    public static void setPaceMonitorSplitTime(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final double seconds) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorSplitTime(context, seconds);
            }
        });
    }

    /**
     * Sets the split to use distance units with a given meter value accurate to 1 meter
     * and must be at least 100m.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param meters The split duration in meters.
     */
    public static void setPaceMonitorSplitDistance(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final int meters) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorSplitDistance(context, meters);
            }
        });
    }

    /**
     * Gets the force curve plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     */
    public static void getPaceMonitorForcePlot(Context context,
            final PendingResultImpl<PaceMonitor.GetForcePlotResult> pendingResult,
            final int numSamples) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorForcePlot(context, numSamples);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetForcePlotResultRef(data));
            }
        });
    }

    /**
     * Gets the heart rate plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     */
    public static void getPaceMonitorHeartRatePlot(Context context,
            final PendingResultImpl<PaceMonitor.GetHeartRatePlotResult> pendingResult,
            final int numSamples) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.getPaceMonitorHeartRatePlot(context, numSamples);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new GetHeartRatePlotResultRef(data));
            }
        });
    }

    /**
     * Enable/disable the screen error mode.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param enable True if the screen error mode should be enabled.
     */
    public static void setPaceMonitorScreenErrorMode(Context context,
            final PendingResultImpl<PaceMonitorResult> pendingResult, final boolean enable) {
        execute(context, Affinity.PACE_MONITOR, new BaseStatusReportingOperation(pendingResult) {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.setPaceMonitorScreenErrorMode(context, enable);
            }
        });
    }

    /**
     * Execute one or more commands in order as a batch. Commands should be created via the static
     * methods provided in {@link CommandBatch}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param commandList The list of commands to execute.
     */
    public static void executeCommandBatch(Context context,
            final PendingResultImpl<PaceMonitor.BatchResult> pendingResult,
            final List<CommandBatch.Command> commandList) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.executeCommandBatch(context, commandList);
            }

            @Override
            protected void onResult(DataHolder data) {
                // TODO Implement BatchResultRef
                pendingResult.setResult(null);
            }
        });
    }
}

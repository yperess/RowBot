package com.concept2.api.service;

import android.content.Context;
import android.util.SparseArray;

import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.service.broker.DataBroker;
import com.concept2.api.service.operations.BaseOperation;
import com.concept2.api.service.operations.GetPaceMonitorStatusOperation;

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
     * Get the status of the pace monitor.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     */
    public static void getPaceMonitorStatus(Context context,
            PendingResultImpl<PaceMonitor.GetStatusResult> pendingResult) {
        execute(context, Affinity.PACE_MONITOR, new GetPaceMonitorStatusOperation(pendingResult));
    }
}

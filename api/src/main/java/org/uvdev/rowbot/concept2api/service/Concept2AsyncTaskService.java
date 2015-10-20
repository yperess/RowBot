package org.uvdev.rowbot.concept2api.service;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.concept2api.Result;
import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.PendingResultImpl;
import org.uvdev.rowbot.concept2api.ResultImpl;
import org.uvdev.rowbot.concept2api.pacemonitor.CommandBuilder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitorResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.BatchResultRef;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.CommandImpl;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.CreateCommandBatchResultRef;
import org.uvdev.rowbot.concept2api.rowbot.RowBot;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.concept2api.rowbot.profile.internal.LoadProfilesResultRef;
import org.uvdev.rowbot.concept2api.service.broker.DataBroker;
import org.uvdev.rowbot.concept2api.service.broker.pacemonitor.CommandBatch;
import org.uvdev.rowbot.concept2api.service.broker.pacemonitor.CommandBatchCache;
import org.uvdev.rowbot.concept2api.service.operations.BaseDataOperation;
import org.uvdev.rowbot.concept2api.service.operations.BaseOperation;

import java.util.ArrayList;
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
        int ROW_BOT = 3;
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

    public static<R extends PaceMonitorResult> void executePaceMonitorCommand(Context context,
            final PendingResultImpl<R> pendingResult, final CommandImpl<R> command) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.executePaceMonitorCommand(context, command);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(command.getResult(data, 0 /* row */));
            }
        });
    }

    /**
     * Execute one or more commands in order as a batch. Commands should be created via the static
     * methods provided in {@link CommandBuilder}.
     *
     * @param context The calling context.
     * @param pendingResult The callback object to report the result to.
     * @param commandList The list of commands to execute.
     */
    public static void createCommandBatch(Context context,
            final PendingResultImpl<PaceMonitor.CreateBatchCommandResult> pendingResult,
            final List<CommandBuilder.Command> commandList) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.createPaceMonitorCommandBatch(context, commandList);
            }

            @Override
            protected void onResult(final DataHolder data) {
                pendingResult.setResult(new CreateCommandBatchResultRef(data));
            }
        });
    }

    /**
     *
     * @param context
     * @param pendingResult
     * @param id
     */
    public static void executeCommandBatch(Context context,
            final PendingResultImpl<PaceMonitor.BatchResult> pendingResult, final int id) {
        execute(context, Affinity.PACE_MONITOR, new BaseDataOperation() {
            private static final String TAG = "ExecCmdBatchOp";
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.executePaceMonitorCommandBatch(context, id);
            }

            @Override
            protected void onResult(DataHolder data) {
                if (data.getStatus() != Concept2StatusCodes.OK) {
                    pendingResult.setResult(new BatchResultRef(data.getStatus()));
                }
                CommandBatch batch = CommandBatchCache.getInstance().findCommandBatch(id);
                if (batch == null) {
                    Log.e(TAG, "Failed to find command batch");
                    pendingResult.setResult(
                            new BatchResultRef(Concept2StatusCodes.COMMAND_NOT_FOUND));
                }
                int row = 0;
                ArrayList<PaceMonitorResult> results = new ArrayList<>();
                for (int i = 0; i < batch.size(); ++i) {
                    Log.d(TAG, "Creating results for batch " + i);
                    ArrayList<CommandImpl> commandArray = batch.getCommandArray(i);
                    for (int j = 0, size = commandArray.size(); j < size; ++j) {
                        Log.d(TAG, "  creating result for command " + i + "." + j);
                        results.add(commandArray.get(j).getResult(data, row++));
                    }
                }
                pendingResult.setResult(new BatchResultRef(results));
            }
        });
    }

    public static void loadProfiles(Context context,
            final PendingResultImpl<RowBot.LoadProfilesResult> pendingResult,
            final String profileId) {
        execute(context, Affinity.ROW_BOT, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.loadProfiles(context, profileId);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new LoadProfilesResultRef(data));
            }
        });
    }

    public static void createProfile(Context context,
            final PendingResultImpl<RowBot.LoadProfilesResult> pendingResult,
            final Profile newProfile) {
        execute(context, Affinity.ROW_BOT, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.createProfile(context, newProfile);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new LoadProfilesResultRef(data));
            }
        });
    }
    public static void updateProfile(Context context,
            final PendingResultImpl<RowBot.LoadProfilesResult> pendingResult,
            final Profile newProfile) {
        execute(context, Affinity.ROW_BOT, new BaseDataOperation() {
            @Override
            protected DataHolder getData(DataBroker dataBroker, Context context) {
                return dataBroker.updateProfile(context, newProfile);
            }

            @Override
            protected void onResult(DataHolder data) {
                pendingResult.setResult(new LoadProfilesResultRef(data));
            }
        });
    }

    public static void deleteProfile(Context context, final PendingResultImpl<Result> pendingResult,
            final String profileId) {
        execute(context, Affinity.ROW_BOT, new BaseOperation() {
            @Override
            protected int execute(DataBroker dataBroker, Context context) {
                return dataBroker.deleteProfile(context, profileId);
            }

            @Override
            protected void onResult(int status) {
                pendingResult.setResult(new ResultImpl(status));
            }
        });
    }
}

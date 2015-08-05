package com.concept2.api.rowbot.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.concept2.api.Concept2;
import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.Result;
import com.concept2.api.ResultCallback;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.rowbot.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DebugFragment extends BaseFragment implements View.OnClickListener, ResultCallback {

    public static final String TAG = "DebugFragment";

    private static final String STATE_RESULTS = "results";

    /** Screen error mode enabled/disabled. */
    private static boolean sErrorMode = false;

    private Button mExecute;
    private Spinner mCommands;
    private ArrayAdapter<String> mResultAdapter;

    int mLastCommandBatchId = -1;
    boolean mCollectingData = false;


    private int mDataCollectBatchId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResultAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        if (savedInstanceState != null) {
            String[] results = savedInstanceState.getStringArray(STATE_RESULTS);
            Log.d(TAG, "Recovering " + results.length + " results");
            for (int i = 0; i < results.length; ++i) {
                mResultAdapter.add(results[i]);
            }
        }


        ArrayList<CommandBuilder.Command> cmdList = new ArrayList<>();
        cmdList.add(CommandBuilder.getPaceCmd(null));
        cmdList.add(CommandBuilder.getStrokeRateCmd(null));
        cmdList.add(CommandBuilder.getHighResolutionWorkTimeCmd(null));
        cmdList.add(CommandBuilder.getHighResolutionWorkDistanceCmd(null));
        cmdList.add(CommandBuilder.getStrokeStateCmd(null));
        Concept2.PaceMonitor.createCommandBatch(getActivity(), cmdList).setResultCallback(
                new ResultCallback<PaceMonitor.CreateBatchCommandResult>() {
                    @Override
                    public void onResult(PaceMonitor.CreateBatchCommandResult result) {
                        if (result.getStatus() == Concept2StatusCodes.OK) {
                            mDataCollectBatchId = result.getBatchId();
                        } else {
                            Toast.makeText(getActivity(), "Failed to create command batch!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String[] results = new String[mResultAdapter.getCount()];
        for (int i = 0; i < results.length; ++i) {
            results[i] = mResultAdapter.getItem(i);
        }
        Log.d(TAG, "Saving " + results.length + " results");
        outState.putStringArray(STATE_RESULTS, results);
    }

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_debug);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.debug_fragment, container, false);
        ((ListView) rootView.findViewById(R.id.result)).setAdapter(mResultAdapter);

        mExecute = (Button) rootView.findViewById(R.id.execute);
        mExecute.setOnClickListener(this);

        mCommands = (Spinner) rootView.findViewById(R.id.commands);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.debug_command_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCommands.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.execute:
                executeCommand();
                break;
        }
    }

    private void executeCommand() {
        switch (mCommands.getSelectedItemPosition()) {
            case 0:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getStatusCmd(this));
                break;
            case 1:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getOdometerCmd(this));
                break;
            case 2:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkTimeCmd(this));
                break;
            case 3:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkDistanceCmd(this));
                break;
            case 4:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkCaloriesCmd(this));
                break;
            case 5:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getStoredWorkoutNumberCmd(this));
                break;
            case 6:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getPaceCmd(this));
                break;
            case 7:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getStrokeRateCmd(this));
                break;
            case 8:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getUserInfoCmd(this));
                break;
            case 9:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getHeartRateCmd(this));
                break;
            case 10:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getPowerCmd(this));
                break;
            case 11: {
                Calendar cal = Calendar.getInstance();
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setTimeCmd(this, cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
                break;
            }
            case 12: {
                Calendar cal = Calendar.getInstance();
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setDateCmd(this, cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
                break;
            }
            case 13:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setTimeoutCmd(this, 15 /* seconds */));
                break;
            case 14:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setGoalTimeCmd(this, 20 /* seconds */));
                break;
            case 15:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setGoalDistanceCmd(this, 100 /* meters */));
                break;
            case 16:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setGoalCaloriesCmd(this, 5 /* calories */));
                break;
            case 17:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setGoalPowerCmd(this, 100 /* watts */));
                break;
            case 18:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setStoredWorkoutNumberCmd(this, 0 /* workoutNumber */));
                break;
            case 19:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setStoredWorkoutNumberCmd(this, 1 /* workoutNumber */));
                break;
            case 20:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkoutTypeCmd(this));
                break;
            case 21:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getDragFactorCmd(this));
                break;
            case 22:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getStrokeStateCmd(this));
                break;
            case 23:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getHighResolutionWorkTimeCmd(this));
                break;
            case 24:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getHighResolutionWorkDistanceCmd(this));
                break;
            case 25:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getErrorValueCmd(this));
                break;
            case 26:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkoutStateCmd(this));
                break;
            case 27:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getWorkoutIntervalCountCmd(this));
                break;
            case 28:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getIntervalTypeCmd(this));
                break;
            case 29:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getRestTimeCmd(this));
                break;
            case 30:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setSplitTimeCmd(this, 60.0 /* seconds */));
                break;
            case 31:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setSplitDistanceCmd(this, 100 /* meters */));
                break;
            case 32:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getForcePlotCmd(this, 0 /* numSamples */));
                break;
            case 33:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.getHeartRatePlotCmd(this, 0 /* numSamples */));
                break;
            case 34:
                sErrorMode = !sErrorMode;
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setScreenErrorModeCmd(this, sErrorMode));
                break;
            case 35:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.RESET));
                break;
            case 36:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.IDLE));
                break;
            case 37:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.HAVE_ID));
                break;
            case 38:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.IN_USE));
                break;
            case 39:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.FINISHED));
                break;
            case 40:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.READY));
                break;
            case 41:
                Concept2.PaceMonitor.executeCommand(getActivity(),
                        CommandBuilder.setPaceMonitorState(this,
                                PaceMonitor.PaceMonitorState.BAD_ID));
                break;
            case 42:
                if (mLastCommandBatchId != -1) {
                    Concept2.PaceMonitor.executeCommandBatch(getActivity(), mLastCommandBatchId)
                            .setResultCallback(this);
                } else {
                    mResultAdapter.add("No batch found");
                }
                break;
            case 43: {
                ArrayList<CommandBuilder.Command> cmdList = new ArrayList<>();
                cmdList.add(CommandBuilder.setGoalDistanceCmd(this, 100));
                cmdList.add(CommandBuilder.setSplitDistanceCmd(this, 100));
                cmdList.add(CommandBuilder.setGoalPowerCmd(this, 200));
                cmdList.add(CommandBuilder.setStoredWorkoutNumberCmd(this, 0));
                cmdList.add(CommandBuilder.setPaceMonitorState(this,
                        PaceMonitor.PaceMonitorState.IN_USE));
                ResultCallback<PaceMonitor.CreateBatchCommandResult> callback =
                        new ResultCallback<PaceMonitor.CreateBatchCommandResult>() {
                            @Override
                            public void onResult(PaceMonitor.CreateBatchCommandResult result) {
                                DebugFragment.this.onResult(result);
                                if (result.getStatus() == Concept2StatusCodes.OK) {
                                    mLastCommandBatchId = result.getBatchId();
                                }
                            }
                        };
                Concept2.PaceMonitor.createCommandBatch(getActivity(), cmdList)
                        .setResultCallback(callback);
                break;
            }
            case 44: {
                if (mCollectingData || mDataCollectBatchId < 0) {
                    break;
                }
                Concept2.PaceMonitor.executeCommandBatch(getActivity(), mDataCollectBatchId)
                        .setResultCallback(this);
            }
        }
    }

    @Override
    public void onResult(Result result) {
        mResultAdapter.add(result.toString());
        if (result.getStatus() == Concept2StatusCodes.OK
                && result instanceof PaceMonitor.BatchResult && mCollectingData) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Concept2.PaceMonitor.executeCommandBatch(getActivity(), mDataCollectBatchId)
                            .setResultCallback(DebugFragment.this);
                }
            }, 100);
        }
    }
}

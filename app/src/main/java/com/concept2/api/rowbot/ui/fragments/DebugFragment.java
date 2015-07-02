package com.concept2.api.rowbot.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.concept2.api.Concept2;
import com.concept2.api.Result;
import com.concept2.api.ResultCallback;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.rowbot.R;

import java.util.Calendar;

public class DebugFragment extends BaseFragment implements View.OnClickListener, ResultCallback {

    public static final String TAG = "DebugFragment";

    private static final String STATE_RESULTS = "results";

    /** Screen error mode enabled/disabled. */
    private static boolean sErrorMode = false;

    private Button mExecute;
    private Spinner mCommands;
    private ArrayAdapter<String> mResultAdapter;

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
                Concept2.PaceMonitor.getStatus(getActivity()).setResultCallback(this);
                break;
            case 1:
                Concept2.PaceMonitor.getOdometer(getActivity()).setResultCallback(this);
                break;
            case 2:
                Concept2.PaceMonitor.getWorkTime(getActivity()).setResultCallback(this);
                break;
            case 3:
                Concept2.PaceMonitor.getWorkDistance(getActivity()).setResultCallback(this);
                break;
            case 4:
                Concept2.PaceMonitor.getWorkCalories(getActivity()).setResultCallback(this);
                break;
            case 5:
                Concept2.PaceMonitor.getStoredWorkoutNumber(getActivity()).setResultCallback(this);
                break;
            case 6:
                Concept2.PaceMonitor.getPace(getActivity()).setResultCallback(this);
                break;
            case 7:
                Concept2.PaceMonitor.getStrokeRate(getActivity()).setResultCallback(this);
                break;
            case 8:
                Concept2.PaceMonitor.getUserInfo(getActivity()).setResultCallback(this);
                break;
            case 9:
                Concept2.PaceMonitor.getHeartRate(getActivity()).setResultCallback(this);
                break;
            case 10:
                Concept2.PaceMonitor.getPower(getActivity()).setResultCallback(this);
                break;
            case 11: {
                Calendar cal = Calendar.getInstance();
                Concept2.PaceMonitor.setTime(getActivity(), cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))
                        .setResultCallback(this);
                break;
            }
            case 12: {
                Calendar cal = Calendar.getInstance();
                Concept2.PaceMonitor.setDate(getActivity(), cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                        .setResultCallback(this);
                break;
            }
            case 13:
                Concept2.PaceMonitor.setTimeout(getActivity(), 15 /* seconds */)
                        .setResultCallback(this);
                break;
            case 14:
                Concept2.PaceMonitor.setGoalTime(getActivity(), 20 /* seconds */)
                        .setResultCallback(this);
                break;
            case 15:
                Concept2.PaceMonitor.setGoalDistance(getActivity(), 100 /* meters */)
                        .setResultCallback(this);
                break;
            case 16:
                Concept2.PaceMonitor.setGoalCalories(getActivity(), 5 /* calories */)
                        .setResultCallback(this);
                break;
            case 17:
                Concept2.PaceMonitor.setGoalPower(getActivity(), 100 /* watts */)
                        .setResultCallback(this);
                break;
            case 18:
                Concept2.PaceMonitor.setStoredWorkoutNumber(getActivity(), 0 /* workoutNumber */)
                        .setResultCallback(this);
                break;
            case 19:
                Concept2.PaceMonitor.setStoredWorkoutNumber(getActivity(), 0 /* workoutNumber */)
                        .setResultCallback(this);
                break;
            case 20:
                Concept2.PaceMonitor.getWorkoutType(getActivity()).setResultCallback(this);
                break;
            case 21:
                Concept2.PaceMonitor.getDragFactor(getActivity()).setResultCallback(this);
                break;
            case 22:
                Concept2.PaceMonitor.getStrokeState(getActivity()).setResultCallback(this);
                break;
            case 23:
                Concept2.PaceMonitor.getHighResWorkTime(getActivity()).setResultCallback(this);
                break;
            case 24:
                Concept2.PaceMonitor.getHighResWorkDistance(getActivity()).setResultCallback(this);
                break;
            case 25:
                Concept2.PaceMonitor.getErrorValue(getActivity()).setResultCallback(this);
                break;
            case 26:
                Concept2.PaceMonitor.getWorkoutState(getActivity()).setResultCallback(this);
                break;
            case 27:
                Concept2.PaceMonitor.getWorkoutIntervalCount(getActivity()).setResultCallback(this);
                break;
            case 28:
                Concept2.PaceMonitor.getIntervalType(getActivity()).setResultCallback(this);
                break;
            case 29:
                Concept2.PaceMonitor.getRestTime(getActivity()).setResultCallback(this);
                break;
            case 30:
                Concept2.PaceMonitor.setSplitTime(getActivity(), 60.0 /* seconds */)
                        .setResultCallback(this);
                break;
            case 31:
                Concept2.PaceMonitor.setSplitDistance(getActivity(), 100 /* meters */)
                        .setResultCallback(this);
                break;
            case 32:
                Concept2.PaceMonitor.getForcePlot(getActivity(), 0 /* numSamples */)
                        .setResultCallback(this);
                break;
            case 33:
                Concept2.PaceMonitor.getHeartRatePlot(getActivity(), 0 /* numSamples */)
                        .setResultCallback(this);
                break;
            case 34:
                sErrorMode = !sErrorMode;
                Concept2.PaceMonitor.setScreenErrorMode(getActivity(), sErrorMode)
                        .setResultCallback(this);
                break;
            case 35:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.RESET)
                        .setResultCallback(this);
                break;
            case 36:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.IDLE)
                        .setResultCallback(this);
                break;
            case 37:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.HAVE_ID)
                        .setResultCallback(this);
                break;
            case 38:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.IN_USE)
                        .setResultCallback(this);
                break;
            case 39:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.FINISHED)
                        .setResultCallback(this);
                break;
            case 40:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.READY)
                        .setResultCallback(this);
                break;
            case 41:
                Concept2.PaceMonitor.setState(getActivity(), PaceMonitor.PaceMonitorState.BAD_ID)
                        .setResultCallback(this);
                break;
        }
    }

    @Override
    public void onResult(Result result) {
        mResultAdapter.add(result.toString());

    }
}

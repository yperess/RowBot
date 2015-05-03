package com.concept2.api.rowbot.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.concept2.api.Concept2;
import com.concept2.api.constants.ReportId;
import com.concept2.api.model.VirtualPaceMonitorApi;
import com.concept2.api.rowbot.R;

import java.util.HashMap;

public class DebugFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "DebugFragment";

    private ArrayAdapter mResultAdapter;
    private Button mExecute;

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_debug);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.debug_fragment, container, false);
        mResultAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);
        ((ListView) rootView.findViewById(R.id.result)).setAdapter(mResultAdapter);
        mExecute = (Button) rootView.findViewById(R.id.execute);
        mExecute.setEnabled(Concept2.PaceMonitor.isConnected());
        mExecute.setOnClickListener(this);
        rootView.findViewById(R.id.refresh).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.execute:
                executeCommand();
                break;
            case R.id.refresh:
                refreshUsbConnection();
                break;
        }
    }

    private void refreshUsbConnection() {
        if (Concept2.PaceMonitor.isConnected()) {
            // TODO - Test connection.
            return;
        }
        Concept2.PaceMonitor.start(getActivity());
        if (Concept2.PaceMonitor.isConnected()) {
            mExecute.setEnabled(true);
        } else {
            showToast("Failed to connect to Pace Monitor");
        }
    }

    private void executeCommand() {
        if (!Concept2.PaceMonitor.isConnected()) {
            showToast("PM not connected");
        }
        // Begin executing commands here...
        try {
            byte[] bytes = Concept2.PaceMonitor.executeCommandBytes(ReportId.SMALL,
                    new byte[] {(byte) 0x80});
            mResultAdapter.add("GET_STATUS (0x80) - " + bytesToString(bytes));
        } catch (VirtualPaceMonitorApi.ConnectionException e) {
            showToast(e.getMessage());
            return;
        }
    }

    private String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("0x");
        for (int i = 0; i < bytes.length; ++i) {
            sb.append(String.format("%02x", bytes[i]));
        }
        return sb.toString();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}

package com.concept2.api.rowbot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.concept2.api.Concept2;
import com.concept2.api.constants.ReportId;
import com.concept2.api.model.VirtualPaceMonitorApi;
import com.concept2.api.rowbot.R;

public class DebugFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DebugFragment";

    private EditText mCommand;
    private Spinner mReportId;
    private TextView mResult;
    private Button mExecute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.debug_fragment, container, false);
        mCommand = (EditText) rootView.findViewById(R.id.command_bytes);
        mReportId = (Spinner) rootView.findViewById(R.id.report_id);
        mResult = (TextView) rootView.findViewById(R.id.result);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.report_ids, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReportId.setAdapter(adapter);
        mExecute = (Button) rootView.findViewById(R.id.execute);
        mExecute.setEnabled(false);
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
        if (Concept2.PaceMonitor.isConnected()) {
            ReportId reportId;
            switch (mReportId.getSelectedItemPosition()) {
                case 0:
                    reportId = ReportId.SMALL;
                    break;
                case 1:
                    reportId = ReportId.MEDIUM;
                    break;
                case 2:
                    reportId = ReportId.LARGE;
                    break;
                default:
                    showToast("Selected report id position unknown: "
                            + mReportId.getSelectedItemPosition());
                    return;
            }
            String commandString = mCommand.getText().toString().toUpperCase();
            byte[] bytes = stringToBytes(commandString);
            if (bytes == null) {
                return;
            }
            try {
                bytes = Concept2.PaceMonitor.executeCommandBytes(reportId, bytes);
            } catch (VirtualPaceMonitorApi.ConnectionException e) {
                showToast(e.getMessage());
                return;
            }
            mResult.setText(bytesToString(bytes));
        }
    }

    private byte[] stringToBytes(String str) {
        if (TextUtils.isEmpty(str)) {
            showToast("Empty input string");
            return null;
        }
        byte[] bytes = new byte[str.length()];
        for (int i = 0; i < bytes.length; ++i) {
            switch (str.charAt(i)) {
                case '0': bytes[i] = (byte) 0x0; break;
                case '1': bytes[i] = (byte) 0x1; break;
                case '2': bytes[i] = (byte) 0x2; break;
                case '3': bytes[i] = (byte) 0x3; break;
                case '4': bytes[i] = (byte) 0x4; break;
                case '5': bytes[i] = (byte) 0x5; break;
                case '6': bytes[i] = (byte) 0x6; break;
                case '7': bytes[i] = (byte) 0x7; break;
                case '8': bytes[i] = (byte) 0x8; break;
                case '9': bytes[i] = (byte) 0x9; break;
                case 'A': bytes[i] = (byte) 0xA; break;
                case 'B': bytes[i] = (byte) 0xB; break;
                case 'C': bytes[i] = (byte) 0xC; break;
                case 'D': bytes[i] = (byte) 0xD; break;
                case 'E': bytes[i] = (byte) 0xE; break;
                case 'F': bytes[i] = (byte) 0xF; break;
                default:
                    showToast("Command string invalid hex: " + str);
                    return null;
            }
        }
        return bytes;
    }

    private String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("0x");
        for (int i = 0; i < bytes.length; ++i) {
            switch (bytes[i]) {
                case 0x0: sb.append("0"); break;
                case 0x1: sb.append("1"); break;
                case 0x2: sb.append("2"); break;
                case 0x3: sb.append("3"); break;
                case 0x4: sb.append("4"); break;
                case 0x5: sb.append("5"); break;
                case 0x6: sb.append("6"); break;
                case 0x7: sb.append("7"); break;
                case 0x8: sb.append("8"); break;
                case 0x9: sb.append("9"); break;
                case 0xA: sb.append("A"); break;
                case 0xB: sb.append("B"); break;
                case 0xC: sb.append("C"); break;
                case 0xD: sb.append("D"); break;
                case 0xE: sb.append("E"); break;
                case 0xF: sb.append("F"); break;
            }
        }
        return sb.toString();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}

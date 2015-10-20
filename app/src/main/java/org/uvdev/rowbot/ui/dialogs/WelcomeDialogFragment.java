package org.uvdev.rowbot.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.uvdev.rowbot.R;

public class WelcomeDialogFragment extends DialogFragment {

    public interface WelcomeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    WelcomeDialogListener mDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof WelcomeDialogListener) {
            mDialogListener = (WelcomeDialogListener) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);
        ListView detailList = (ListView) dialogView.findViewById(R.id.dialog_welcome_detail_list);
        ArrayAdapter<String> detailListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.welcome_dialog_list));
        detailList.setAdapter(detailListAdapter);

        builder.setView(dialogView)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogListener != null) {
                            mDialogListener.onDialogPositiveClick(WelcomeDialogFragment.this);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogListener != null) {
                            mDialogListener.onDialogNegativeClick(WelcomeDialogFragment.this);
                        }
                    }
                });
        return builder.create();
    }
}

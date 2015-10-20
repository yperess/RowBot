package org.uvdev.rowbot.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.uvdev.rowbot.R;

public class ProgressDialog extends DialogFragment {

    private static final String ARG_TITLE_RES_ID = "title_res_id";
    private static final String ARG_MESSAGE_RES_ID = "message_res_id";

    public static ProgressDialog createInstance(int titleResId, int messageResId) {
        ProgressDialog progressDialog = new ProgressDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE_RES_ID, titleResId);
        args.putInt(ARG_MESSAGE_RES_ID, messageResId);
        progressDialog.setArguments(args);
        return progressDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int spacing = getResources().getDimensionPixelSize(R.dimen.dialog_spacing);
        View content = LayoutInflater.from(getContext()).inflate(R.layout.progress_dialog, null);
        int titleResId = getArguments().getInt(ARG_TITLE_RES_ID);
        int messageResId = getArguments().getInt(ARG_MESSAGE_RES_ID);
        if (messageResId > 0) {
            ((TextView) content.findViewById(R.id.message)).setText(messageResId);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (titleResId > 0) {
            builder.setTitle(getArguments().getInt(ARG_TITLE_RES_ID));
        }
        return builder.setView(content, spacing, spacing, spacing, spacing)
                .create();
    }
}

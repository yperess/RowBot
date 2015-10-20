package org.uvdev.rowbot.ui.dialogs;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import org.uvdev.rowbot.MainActivity;

public class BaseDialogFragment extends DialogFragment {

    protected MainActivity mParent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = (MainActivity) activity;
    }
}

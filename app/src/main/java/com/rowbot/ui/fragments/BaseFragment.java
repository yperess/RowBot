package com.rowbot.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.concept2.api.common.data.Version;
import com.rowbot.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected MainActivity mParent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = (MainActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public Version getCurrentVersion() {
        return mParent == null ? null : mParent.getRowBotApplication().getCurrentVersion();
    }
}

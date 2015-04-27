package com.concept2.api.rowbot.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.concept2.api.rowbot.model.RowBotActivity;

public class BaseFragment extends Fragment {

    protected RowBotActivity mParent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent = (RowBotActivity) activity;
    }
}

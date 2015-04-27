package com.concept2.api.rowbot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.concept2.api.rowbot.R;

public class HelpAndFeedbackFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent.setActionBarTitle(R.string.rowbot_frag_help);
    }
}

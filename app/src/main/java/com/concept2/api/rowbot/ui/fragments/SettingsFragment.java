package com.concept2.api.rowbot.ui.fragments;

import com.concept2.api.rowbot.R;

public class SettingsFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_settings);
    }
}

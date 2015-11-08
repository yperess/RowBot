package org.uvdev.rowbot.ui.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.concept2.api.common.data.Version;

import org.uvdev.rowbot.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected MainActivity mParent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (MainActivity) getActivity();
    }
    @Override
    public void onStart() {
        super.onStart();

        Boolean hasNavDrawer = hasNavDrawer();
        if (hasNavDrawer != null) {
            mParent.setHasNavDrawer(hasNavDrawer);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public Version getCurrentVersion() {
        return mParent == null ? null : mParent.getRowBotApplication().getCurrentVersion();
    }

    /**
     * This method gets called from {@link #onStart()} and is used to set the nav drawer
     * action. By default base fragments are assumed to be leaf nodes with no nav drawer.
     *
     * @return Whether or not this fragment should have a navigation drawer (null if no affect).
     */
    protected Boolean hasNavDrawer() {
        return null;
    }
}

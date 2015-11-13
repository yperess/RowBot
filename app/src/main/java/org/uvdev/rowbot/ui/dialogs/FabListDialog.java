package org.uvdev.rowbot.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.uvdev.rowbot.R;
import org.uvdev.rowbot.concept2api.utils.Preconditions;
import org.uvdev.rowbot.ui.widgets.FabActionView;
import org.uvdev.rowbot.ui.widgets.utils.AbstractAnimationListener;

import java.util.ArrayList;

public class FabListDialog extends DialogFragment implements View.OnClickListener {

    public static final String KEY_FAB_INDEX = "fabIndex";
    private static final String STATE_FAB_DATA_ITEMS = "fabDataItems";

    public static FabListDialog newInstance(Fragment targetFragment, int requestCode) {
        FabListDialog dialog = new FabListDialog();
        dialog.setTargetFragment(targetFragment, requestCode);
        return dialog;
    }

    private final FabActionView[] mFabs = new FabActionView[6];
    private ArrayList<FabActionView.FabDataItem> mFabDataItems;
    private boolean mInflated = false;

    private boolean mInitialDataSet = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(), R.style.RowBot_FabListDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflated = true;
        View root = inflater.inflate(R.layout.fab_list_dialog, container, false);
        root.setOnClickListener(this);
        mFabs[0] = (FabActionView) root.findViewById(R.id.fab0);
        mFabs[1] = (FabActionView) root.findViewById(R.id.fab1);
        mFabs[2] = (FabActionView) root.findViewById(R.id.fab2);
        mFabs[3] = (FabActionView) root.findViewById(R.id.fab3);
        mFabs[4] = (FabActionView) root.findViewById(R.id.fab4);
        mFabs[5] = (FabActionView) root.findViewById(R.id.fab5);

        for (int i = 0; i < mFabs.length; ++i) {
            mFabs[i].setOnClickListener(this);
        }
        if (savedInstanceState != null) {
            mFabDataItems = savedInstanceState.getParcelableArrayList(STATE_FAB_DATA_ITEMS);
        }
        if (mFabDataItems != null) {
            setDataItems(mFabDataItems);
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_FAB_DATA_ITEMS, mFabDataItems);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab0:
                onFabClicked(0);
                break;
            case R.id.fab1:
                onFabClicked(1);
                break;
            case R.id.fab2:
                onFabClicked(2);
                break;
            case R.id.fab3:
                onFabClicked(3);
                break;
            case R.id.fab4:
                onFabClicked(4);
                break;
            case R.id.fab5:
                onFabClicked(5);
                break;
            default:
                dismiss();
        }
    }

    public void setDataItems(ArrayList<FabActionView.FabDataItem> dataItems) {
        Preconditions.assertTrue(dataItems.size() <= mFabs.length);
        mFabDataItems = dataItems;
        if (!mInflated) {
            // Views aren't ready yet, cache this data for now.
            return;
        }
        int animationDelay = 0;
        final int animationDelayDelta = getContext().getResources()
                .getInteger(R.integer.fab_list_animation_delay_delta);
        for (int i = 0; i < mFabs.length; ++i) {
            if (i >= dataItems.size()) {
                // This FAB isn't in use, hide it.
                if (mFabs[i].getVisibility() != View.VISIBLE) {
                    // View is already hidden, skip.
                    continue;
                }
                Animation animation = AnimationUtils.loadAnimation(getContext(),
                        android.R.anim.slide_out_right);
                animation.setAnimationListener(new AbstractAnimationListener<View>(mFabs[i]) {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mData.setVisibility(View.INVISIBLE);
                    }
                });
                animation.setStartOffset(animationDelay);
                animationDelay += animationDelayDelta;
                mFabs[i].startAnimation(animation);
                continue;
            }
            FabActionView.FabDataItem dataItem = dataItems.get(i);

            if (mInitialDataSet) {
                mFabs[i].setVisibility(View.VISIBLE);
                mFabs[i].setDataItem(dataItem, false /* animate */, 0);
                continue;
            }

            if (mFabs[i].getVisibility() == View.VISIBLE) {
                // View is already visible, animate data transition.
                mFabs[i].setDataItem(dataItem, true /* animate */, animationDelay);
                animationDelay += animationDelayDelta;
                continue;
            }
            // View was not visible, set the resource right now.
            mFabs[i].setDataItem(dataItem, false /* animate */, 0);
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    android.R.anim.slide_in_left);
            animation.setAnimationListener(new AbstractAnimationListener<View>(mFabs[i]) {
                @Override
                public void onAnimationEnd(Animation animation) {
                    mData.setVisibility(View.VISIBLE);
                }
            });
            animation.setStartOffset(animationDelay);
            animationDelay += animationDelayDelta;
            mFabs[i].startAnimation(animation);
        }
        mInitialDataSet = false;
    }

    private void onFabClicked(int index) {
        Intent data = new Intent();
        data.putExtra(KEY_FAB_INDEX, index);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }

}

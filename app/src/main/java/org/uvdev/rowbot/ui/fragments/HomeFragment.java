package org.uvdev.rowbot.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.adapters.HomePageAdapter;
import org.uvdev.rowbot.ui.dialogs.FabListDialog;
import org.uvdev.rowbot.ui.widgets.FabActionView.FabDataItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends BasePageFragment implements Observer, View.OnClickListener {

    private static final int RC_FAB_LIST = 1000;
    private static final String STATE_SHOWING_DEFAULT_WORKOUTS = "showingDefaultWorkouts";
    private static final String TAG_FAB_LIST_DIALOG = "FabListDialog";

    private boolean mShowingDefaultWorkouts = true;
    private final ArrayList<FabDataItem> mDefaultWorkouts = new ArrayList<>(
            Arrays.asList(new FabDataItem[] {
                    new FabDataItem.Builder()
                            .setTitle("New Workout")
                            .setIconResId(R.drawable.ic_edit)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("Favorites")
                            .setIconResId(R.drawable.ic_star)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("10K")
                            .setIconResId(R.drawable.ic_10k)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("6K")
                            .setIconResId(R.drawable.ic_6k)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("5K")
                            .setIconResId(R.drawable.ic_5k)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("2K")
                            .setIconResId(R.drawable.ic_2k)
                            .build(),
            }));
    private final ArrayList<FabDataItem> mFavoriteWorkouts = new ArrayList<>(
            Arrays.asList(new FabDataItem[] {
                    new FabDataItem.Builder()
                            .setTitle("New Workout")
                            .setIconResId(R.drawable.ic_edit)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("Default Workouts")
                            .setIconResId(R.drawable.ic_reply)
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("3x20'")
                            .setIconResId(R.drawable.ic_star)
                            .setIconText("2")
                            .build(),
                    new FabDataItem.Builder()
                            .setTitle("6x2k")
                            .setIconResId(R.drawable.ic_star)
                            .setIconText("1")
                            .build(),
            }));

    private RecyclerView mRecyclerView;
    private HomePageAdapter mHomePageAdapter;

    private View mFab;
    private FabListDialog mFabListDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, null);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mHomePageAdapter = new HomePageAdapter(mParent);
        mRecyclerView.setAdapter(mHomePageAdapter);

        mFab = root.findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        if (savedInstanceState != null) {
            mShowingDefaultWorkouts = savedInstanceState.getBoolean(STATE_SHOWING_DEFAULT_WORKOUTS);
        }
        Fragment fragment = getChildFragmentManager().findFragmentByTag(TAG_FAB_LIST_DIALOG);
        if (fragment != null && fragment instanceof FabListDialog) {
            mFabListDialog = (FabListDialog) fragment;
            mFabListDialog.setTargetFragment(this, RC_FAB_LIST);
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SHOWING_DEFAULT_WORKOUTS, mShowingDefaultWorkouts);
    }

    @Override
    protected Boolean hasNavDrawer() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mParent.getSupportActionBar().setTitle("RowBot");
        RowBotActivity.CURRENT_PROFILE.addObserver(this);
    }

    @Override
    public String getPageTitle() {
        return "HOME";
    }

    @Override
    public void update(Observable observable, Object data) {
        mHomePageAdapter.updateProfile((Profile) data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                RotateAnimation animation = new RotateAnimation(0.0f, 90.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(250);
                mFab.startAnimation(animation);
                mFabListDialog = FabListDialog.newInstance(this, RC_FAB_LIST);
                mFabListDialog.setDataItems(mDefaultWorkouts);
                mFabListDialog.show(getChildFragmentManager(), TAG_FAB_LIST_DIALOG);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_FAB_LIST: {
                int fabIndex = data.getIntExtra(FabListDialog.KEY_FAB_INDEX, -1);
                onFabItemClicked(fabIndex);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onFabItemClicked(int index) {
        if (index == 0) {
            Toast.makeText(getContext(), "New Workout", Toast.LENGTH_LONG).show();
            return;
        }

        if (mShowingDefaultWorkouts) {
            switch (index) {
                case 1:
                    mFabListDialog.setDataItems(mFavoriteWorkouts);
                    mShowingDefaultWorkouts = false;
                    return;
                case 2:
                    Toast.makeText(getContext(), "Starting 10k", Toast.LENGTH_LONG).show();
                    return;
                case 3:
                    Toast.makeText(getContext(), "Starting 6k", Toast.LENGTH_LONG).show();
                    return;
                case 4:
                    Toast.makeText(getContext(), "Starting 5k", Toast.LENGTH_LONG).show();
                    return;
                case 5:
                    Toast.makeText(getContext(), "Starting 2k", Toast.LENGTH_LONG).show();
                    return;
            }
        } else {
            switch (index) {
                case 1:
                    mFabListDialog.setDataItems(mDefaultWorkouts);
                    mShowingDefaultWorkouts = true;
                    return;
                case 2:
                    Toast.makeText(getContext(), "Starting 3x20", Toast.LENGTH_LONG).show();
                    return;
                case 3:
                    Toast.makeText(getContext(), "Starting 6x2k", Toast.LENGTH_LONG).show();
                    return;
            }
        }
    }
}

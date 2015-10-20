package org.uvdev.rowbot.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.adapters.HomePageAdapter;

import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends BasePageFragment implements Observer {

    private RecyclerView mRecyclerView;
    private HomePageAdapter mHomePageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, null);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mHomePageAdapter = new HomePageAdapter(mParent);
        mRecyclerView.setAdapter(mHomePageAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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
}

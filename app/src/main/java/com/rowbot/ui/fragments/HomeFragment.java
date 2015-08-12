package com.rowbot.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rowbot.R;
import com.rowbot.api.RowBot;
import com.rowbot.ui.adapters.HomePageAdapter;

public class HomeFragment extends BasePageFragment {

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
        mHomePageAdapter.updateProfile(RowBot.getProfile(getActivity()));
    }

    @Override
    public String getPageTitle() {
        return "HOME";
    }
}

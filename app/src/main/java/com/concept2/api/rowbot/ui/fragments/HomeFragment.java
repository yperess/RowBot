package com.concept2.api.rowbot.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.concept2.api.rowbot.R;

public class HomeFragment extends BasePageFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, null);
        return root;
    }

    @Override
    public String getPageTitle() {
        return "HOME";
    }
}

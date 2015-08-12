package com.rowbot.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rowbot.R;

public class NewWorkoutFragment extends BasePageFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_workout_fragment, null);
        return root;
    }

    @Override
    public String getPageTitle() {
        return "NEW WORKOUT";
    }
}

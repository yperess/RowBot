package com.rowbot.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rowbot.R;
import com.rowbot.api.RowBot;
import com.rowbot.internal.PersonalRecordRef;
import com.rowbot.model.PersonalRecord;
import com.concept2.api.rowbot.profile.Profile;
import com.rowbot.ui.adapters.BasePrAdapter;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class ProfileDetailsFragment extends BaseFragment {

    private Profile mProfile;

    private ImageView mProfileImageView;
    private TextView mNameView;
    private TextView mTeamNameView;
    private TextView mSeasonMetersView;
    private TextView mLifetimeMetersView;
    private RecyclerView mPrListView;


    private PersonalRecord[] mRecords = new PersonalRecord[] {
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_500m,
                    100300, 38, new GregorianCalendar(2014, 04, 10)),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_1k,
                    0, 0, null),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_2k,
                    TimeUnit.MINUTES.toMillis(7) + 5600, 34,
                    new GregorianCalendar(2014, 01, 20)),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_5k,
                    TimeUnit.MINUTES.toMillis(19) + 17200, 31,
                    new GregorianCalendar(2013, 10, 2)),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_6k,
                    TimeUnit.MINUTES.toMillis(23) + 3700, 31,
                    new GregorianCalendar(2013, 10, 30)),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_DISTANCE, R.drawable.ic_10k,
                    0, 0, null),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_TIME, R.drawable.ic_30m, 7856, 26,
                    new GregorianCalendar(2014, 7, 14)),
            new PersonalRecordRef(PersonalRecord.TYPE_FIXED_TIME, R.drawable.ic_60m, 12572, 25,
                    new GregorianCalendar(2014, 8, 12)),
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_details_fragment, null);
        mProfileImageView = (ImageView) root.findViewById(R.id.profile_pic);
        mNameView = (TextView) root.findViewById(R.id.user_name);
        mTeamNameView = (TextView) root.findViewById(R.id.team_name);
        mSeasonMetersView = (TextView) root.findViewById(R.id.season_meters);
        mLifetimeMetersView = (TextView) root.findViewById(R.id.lifetime_meters);
        mPrListView = (RecyclerView) root.findViewById(R.id.pr_list);
        mPrListView.setAdapter(new BasePrAdapter(getActivity()));
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParent.getSupportActionBar().setTitle(R.string.profile_page_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mProfile = RowBot.getProfile(getActivity());
        mProfileImageView.setImageURI(mProfile.getImageUri());
        mNameView.setText(mProfile.getName());
        mTeamNameView.setText(mProfile.getTeamName());
        mSeasonMetersView.setText(getString(R.string.profile_season_meters,
                NumberFormat.getInstance().format(mProfile.getSeasonMeters())));
        mLifetimeMetersView.setText(getString(R.string.profile_lifetime_meters,
                NumberFormat.getInstance().format(mProfile.getLifetimeMeters())));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BasePrAdapter) mPrListView.getAdapter())
                        .addPersonalRecords(Arrays.asList(mRecords));
            }
        }, 1000);
    }
}

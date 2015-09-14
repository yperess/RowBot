package com.rowbot.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.concept2.api.common.Constants;
import com.rowbot.R;
public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "SettingsFragment";

    private TextView mUserName;
    private TextView mClubName;
    private CheckBox mSendData;

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        mUserName = (TextView) rootView.findViewById(R.id.user_name);
        mUserName.setOnClickListener(this);
//        setUserName(mParent.getUserName());

        mClubName = (TextView) rootView.findViewById(R.id.club_name);
        mClubName.setOnClickListener(this);
//        setClubName(mParent.getClubName());

        mSendData = (CheckBox) rootView.findViewById(R.id.send_data);
//        mSendData.setChecked(Constants.VERSION.isBeta() || mParent.isSharingData());
        mSendData.setEnabled(!Constants.VERSION.isBeta());
        mSendData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!Constants.VERSION.isBeta()) {
//                    mParent.setSharingData(isChecked);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.user_name) {
            showAlertDialogForName(true);
        } else if (v.getId() == R.id.club_name) {
            showAlertDialogForName(false);
        }
    }

    private void showAlertDialogForName(final boolean isUserName) {
//        final EditText input = new EditText(getActivity());
//        input.setHint(isUserName ? "User Name" : "Club Name");
//        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
//        if (isUserName && !TextUtils.isEmpty(mParent.getUserName())) {
//            input.setText(mParent.getUserName());
//        } else if (!isUserName && !TextUtils.isEmpty(mParent.getClubName())) {
//            input.setText(mParent.getClubName());
//        }
//
//        new AlertDialog.Builder(getActivity())
//                .setTitle(isUserName ? "Set User Name" : "Set Club Name")
//                .setView(input)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String name = input.getText().toString();
//                        if (isUserName) {
//                            mParent.setUserName(name);
//                            setUserName(name);
//                        } else {
//                            mParent.setClubName(name);
//                            setClubName(name);
//                        }
//                    }
//                })
//                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .show();
    }

    private void setUserName(String userName) {
        if (TextUtils.isEmpty(userName)) mUserName.setText(R.string.settings_name);
        else mUserName.setText(userName);
    }

    private void setClubName(String clubName) {
        if (TextUtils.isEmpty(clubName)) mClubName.setText(R.string.settings_club);
        else mClubName.setText(clubName);
    }
}

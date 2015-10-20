package org.uvdev.rowbot.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.uvdev.rowbot.concept2api.Concept2;
import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.concept2api.Result;
import org.uvdev.rowbot.concept2api.ResultCallback;
import org.uvdev.rowbot.concept2api.rowbot.RowBot;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.concept2api.rowbot.profile.ProfileCreator;
import org.uvdev.rowbot.concept2api.rowbot.profile.ProfileSettings;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.adapters.NavDrawerAdapter;

public class SettingsFragment extends BaseFragment implements View.OnClickListener,
        ResultCallback<RowBot.LoadProfilesResult>, DialogInterface.OnClickListener {

    public static final String TAG = "SettingsFragment";

    private ProfileCreator mProfileCreator;

    private View mErrorView;
    private View mContainer;
    private CheckBox mProvideUseStatisticsCheckBox;
    private CheckBox mApplyAgeAdjustmentCheckBox;
    private CheckBox mApplyWeightAdjustmentCheckBox;
    private CheckBox mApplyBoatAdjustmentCheckBox;
    private Spinner mBoatTypeSpinner;
    private Spinner mDataResolutionSpinner;
    private Button mDeleteProfileButton;

    @Override
    public void onResume() {
        super.onResume();
        mParent.setActionBarTitle(R.string.rowbot_frag_settings);
        Profile currentProfile = RowBotActivity.CURRENT_PROFILE.getValue();
        if (currentProfile == null) {
            mProfileCreator = null;
            mErrorView.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.GONE);
            return;
        }
        mProfileCreator = new ProfileCreator(currentProfile);
        mErrorView.setVisibility(View.GONE);
        ProfileSettings settings = mProfileCreator.getProfileSettings();
        mProvideUseStatisticsCheckBox.setChecked(settings.provideUseStatistics());
        mApplyAgeAdjustmentCheckBox.setChecked(settings.applyAgeAdjustment());
        mApplyWeightAdjustmentCheckBox.setChecked(settings.applyWeightAdjustment());
        mApplyBoatAdjustmentCheckBox.setChecked(settings.applyBoatAdjustment());
        mBoatTypeSpinner.setSelection(settings.getBoatType());
        mDataResolutionSpinner.setSelection(settings.getDataResolution());
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProfileCreator == null) {
            return;
        }
        // Save changes.
        mProfileCreator.getProfileSettingsCreator()
                .setProvideUseStatistics(mProvideUseStatisticsCheckBox.isChecked())
                .setApplyAgeAdjustment(mApplyAgeAdjustmentCheckBox.isChecked())
                .setApplyWeightAdjustment(mApplyWeightAdjustmentCheckBox.isChecked())
                .setApplyBoatAdjustment(mApplyBoatAdjustmentCheckBox.isChecked())
                .setBoatType(mBoatTypeSpinner.getSelectedItemPosition())
                .setDataResolution(mDataResolutionSpinner.getSelectedItemPosition());
        Concept2.RowBot.updateProfile(mParent, mProfileCreator).setResultCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        mErrorView = root.findViewById(R.id.error);
        mContainer = root.findViewById(R.id.container);
        mProvideUseStatisticsCheckBox = (CheckBox) root.findViewById(R.id.provide_statistics);
        mApplyAgeAdjustmentCheckBox = (CheckBox) root.findViewById(R.id.apply_age_adjustment);
        mApplyWeightAdjustmentCheckBox = (CheckBox) root.findViewById(R.id.apply_weight_adjustment);
        mApplyBoatAdjustmentCheckBox = (CheckBox) root.findViewById(R.id.apply_boat_adjustment);
        mBoatTypeSpinner = (Spinner) root.findViewById(R.id.boat_type);
        mDataResolutionSpinner = (Spinner) root.findViewById(R.id.data_resolution);
        mDeleteProfileButton = (Button) root.findViewById(R.id.delete);

        if (mParent.getRowBotApplication().getCurrentVersion().isBeta()) {
            mProvideUseStatisticsCheckBox.setChecked(true);
            mProvideUseStatisticsCheckBox.setEnabled(false);
        } else {
            mProvideUseStatisticsCheckBox.setEnabled(true);
        }
        mDeleteProfileButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResult(RowBot.LoadProfilesResult result) {
        if (result.getStatus() != Concept2StatusCodes.OK) {
            Toast.makeText(mParent, "Failed to save changes.", Toast.LENGTH_LONG).show();
        } else {
            RowBotActivity.CURRENT_PROFILE.setValue(result.getProfiles().get(0))
                    .notifyObservers();
            Toast.makeText(mParent, "Profile updated.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(mParent)
                .setTitle(R.string.settings_delete_dialog_title)
                .setMessage(getString(R.string.settings_delete_dialog_message,
                        mProfileCreator.getName()))
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Concept2.RowBot.deleteProfile(mParent, mProfileCreator.getProfileId()).setResultCallback(
                new ResultCallback<Result>() {
                    @Override
                    public void onResult(Result result) {
                        if (result.getStatus() == Concept2StatusCodes.OK) {
                            // Update list of profiles.
                            NavDrawerAdapter.deleteProfile(mProfileCreator.getProfileId());
                            mProfileCreator = null;
                        } else {
                            Toast.makeText(mParent, "Failed to delete profile!", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
        dialog.dismiss();
    }
}

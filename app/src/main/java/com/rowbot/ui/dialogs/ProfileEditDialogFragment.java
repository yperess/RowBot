package com.rowbot.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.concept2.api.Concept2;
import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.Result;
import com.concept2.api.ResultCallback;
import com.concept2.api.common.Constants;
import com.concept2.api.common.data.Version;
import com.concept2.api.rowbot.RowBot;
import com.concept2.api.rowbot.RowBot.LoadProfilesResult;
import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileCreator;
import com.concept2.api.rowbot.profile.ProfileSettings;
import com.concept2.api.rowbot.profile.ProfileSettingsCreator;
import com.rowbot.R;
import com.rowbot.model.RowBotActivity;
import com.rowbot.ui.adapters.NavDrawerAdapter;
import com.rowbot.ui.widgets.ImageRailView;
import com.rowbot.utils.StockImageUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class ProfileEditDialogFragment extends BaseDialogFragment
        implements View.OnClickListener, DialogInterface.OnShowListener,
        DatePickerDialog.OnDateSetListener {

    public static final String TAG = "ProfileEditDialog";

    private static final String ARG_IS_EDIT = "isEdit";
    private static final String ARG_CANCELABLE = "cancelable";

    public static ProfileEditDialogFragment createInstance(boolean isEdit, boolean cancelable) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_EDIT, isEdit);
        args.putBoolean(ARG_CANCELABLE, cancelable);
        ProfileEditDialogFragment dialogFragment = new ProfileEditDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private static final String STATE_PAGE_ID = "pageId";
    private static final String STATE_CREATOR = "profileCreator";

    private static final int PAGE_BASIC_INFO_ID = 0;
    private static final int PAGE_DETAILS_ID = 1;
    private static final int PAGE_SETTINGS_ID = 2;

    private final UpdateProfileListener mUpdateProfileListener =
            new UpdateProfileListener(false /* isCreate */);
    private final UpdateProfileListener mCreateProfileListener =
            new UpdateProfileListener(true /* isCreate */);

    private int mPageId;

    private Button mNegativeButton;
    private Button mPositiveButton;
    private View mBasicInfoPage;
    private View mDetailPage;
    private View mSettingsPage;
    private ImageRailView mImageRailView;
    private EditText mUserName;
    private EditText mTeamName;

    private EditText mBirthdayView;
    private EditText mWeightView;
    private Switch mWeightUnitsSwitch;
    private Spinner mGenderSpinner;

    private CheckBox mProvideUseStatisticsCheckBox;
    private CheckBox mApplyAgeAdjustmentCheckBox;
    private CheckBox mApplyWeightAdjustmentCheckBox;
    private CheckBox mApplyBoatAdjustmentCheckBox;
    private Spinner mBoatTypeSpinner;
    private Spinner mDataResolutionSpinner;

    private View mProgressBar;

    private ProfileCreator mProfileCreator;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean cancelable = getArguments().getBoolean(ARG_CANCELABLE, true);
        int titleResId = isEdit() ? R.string.profile_dialog_edit_title
                : R.string.profile_dialog_create_title;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(titleResId)
                .setView(R.layout.dialog_edit_profile)
                .setPositiveButton(R.string.next, null)
                .setNegativeButton(R.string.back, null)
                .setCancelable(cancelable);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mPageId = PAGE_BASIC_INFO_ID;
            if (isEdit()) {
                // Loading a profile to edit.
                mProfileCreator = new ProfileCreator(RowBotActivity.CURRENT_PROFILE.getValue());
            } else {
                // Creating a new profile.
                int selectedImage = new Random(System.currentTimeMillis())
                        .nextInt(StockImageUtils.STOCK_AVATAR_IMAGE_RES_IDS.length);
                mProfileCreator = new ProfileCreator()
                        .setImageId(selectedImage);
            }
        } else {
            mPageId = savedInstanceState.getInt(STATE_PAGE_ID);
            mProfileCreator = savedInstanceState.getParcelable(STATE_CREATOR);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        AlertDialog alertDialog = (AlertDialog) dialog;
        mNegativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        mPositiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        mBasicInfoPage = alertDialog.findViewById(R.id.basic_info_page);
        mDetailPage = alertDialog.findViewById(R.id.detail_page);
        mSettingsPage = alertDialog.findViewById(R.id.settings_page);
        mImageRailView = (ImageRailView) alertDialog.findViewById(R.id.image_rail);
        mUserName = (EditText) alertDialog.findViewById(R.id.user_name);
        mTeamName = (EditText) alertDialog.findViewById(R.id.team_name);
        mBirthdayView = (EditText) alertDialog.findViewById(R.id.birthday);
        mWeightView = (EditText) alertDialog.findViewById(R.id.weight);
        mWeightUnitsSwitch = (Switch) alertDialog.findViewById(R.id.weight_units);
        mGenderSpinner = (Spinner) alertDialog.findViewById(R.id.gender);
        mProvideUseStatisticsCheckBox =
                (CheckBox) alertDialog.findViewById(R.id.provide_statistics);
        mApplyAgeAdjustmentCheckBox =
                (CheckBox) alertDialog.findViewById(R.id.apply_age_adjustment);
        mApplyWeightAdjustmentCheckBox =
                (CheckBox) alertDialog.findViewById(R.id.apply_weight_adjustment);
        mApplyBoatAdjustmentCheckBox =
                (CheckBox) alertDialog.findViewById(R.id.apply_boat_adjustment);
        mBoatTypeSpinner = (Spinner) alertDialog.findViewById(R.id.boat_type);
        mDataResolutionSpinner = (Spinner) alertDialog.findViewById(R.id.data_resolution);
        mProgressBar = alertDialog.findViewById(R.id.progress);

        mImageRailView.setImages(StockImageUtils.STOCK_AVATAR_IMAGE_RES_IDS);

        mNegativeButton.setOnClickListener(this);
        mPositiveButton.setOnClickListener(this);
        setPageId(mPageId);

        mBirthdayView.setOnClickListener(this);
        if (mProfileCreator.getImageId() != -1) {
            mImageRailView.setSelectedImage(mProfileCreator.getImageId());
        }
        if (!TextUtils.isEmpty(mProfileCreator.getName())) {
            mUserName.setText(mProfileCreator.getName());
        }
        if (!TextUtils.isEmpty(mProfileCreator.getTeamName())) {
            mTeamName.setText(mProfileCreator.getTeamName());
        }
        if (mProfileCreator.getBirthday() != null) {
            setBirthdayDisplay(mProfileCreator.getBirthday());
        }
        if (mProfileCreator.getWeight() != 0.0f) {
            NumberFormat format = DecimalFormat.getInstance();
            format.setMaximumFractionDigits(1);
            mWeightView.setText(format.format(mProfileCreator.getWeight()));
        }
        mGenderSpinner.setSelection(mProfileCreator.getGender());
        ProfileSettings settings = mProfileCreator.getProfileSettings();
        if (Constants.VERSION.isBeta()) {
            mProvideUseStatisticsCheckBox.setChecked(true);
            mProvideUseStatisticsCheckBox.setEnabled(false);
        } else {
            mProvideUseStatisticsCheckBox.setChecked(settings.provideUseStatistics());
            mProvideUseStatisticsCheckBox.setEnabled(true);
        }
        mApplyAgeAdjustmentCheckBox.setChecked(settings.applyAgeAdjustment());
        mApplyWeightAdjustmentCheckBox.setChecked(settings.applyWeightAdjustment());
        mApplyBoatAdjustmentCheckBox.setChecked(settings.applyBoatAdjustment());
        mBoatTypeSpinner.setSelection(settings.getBoatType(), true /* animate */);
        mDataResolutionSpinner.setSelection(settings.getDataResolution(), true /* animate */);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PAGE_ID, mPageId);
        outState.putParcelable(STATE_CREATOR, mProfileCreator);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mNegativeButton.getId()) {
            setPageId(mPageId - 1);
            return;
        } else if (view.getId() == mPositiveButton.getId()) {
            if (mPageId == PAGE_SETTINGS_ID) {
                // Done creating profile.
                mProfileCreator.setName(mUserName.getText().toString())
                        .setImageId(mImageRailView.getSelectedImage())
                        .setTeamName(mTeamName.getText().toString())
                        .setGender(getGender())
                        .setWeight(getWeight());
                ProfileSettingsCreator settings = mProfileCreator.getProfileSettingsCreator();
                settings.setProvideUseStatistics(mProvideUseStatisticsCheckBox.isChecked())
                        .setApplyAgeAdjustment(mApplyAgeAdjustmentCheckBox.isChecked())
                        .setApplyWeightAdjustment(mApplyWeightAdjustmentCheckBox.isChecked())
                        .setApplyBoatAdjustment(mApplyBoatAdjustmentCheckBox.isChecked())
                        .setBoatType(mBoatTypeSpinner.getSelectedItemPosition())
                        .setDataResolution(mDataResolutionSpinner.getSelectedItemPosition());
                if (isEdit()) {
                    Concept2.RowBot.updateProfile(getActivity(), mProfileCreator)
                            .setResultCallback(mUpdateProfileListener);
                } else {
                    Concept2.RowBot.createNewProfile(getActivity(), mProfileCreator)
                            .setResultCallback(mCreateProfileListener);
                }
                // Show loading.
            } else {
                // Go to next page
                setPageId(mPageId + 1);
            }
            return;
        }
        switch (view.getId()) {
            case R.id.birthday: {
                Calendar birthday = mProfileCreator.getBirthday();
                if (birthday == null) {
                    birthday = new GregorianCalendar();
                }
                new DatePickerDialog(getActivity(), this, birthday.get(Calendar.YEAR),
                        birthday.get(Calendar.MONTH), birthday.get(Calendar.DAY_OF_MONTH)).show();
            }
        }
    }

    private void setPageId(int pageId) {
        mPageId = pageId;
        switch (mPageId) {
            case PAGE_BASIC_INFO_ID:
                mNegativeButton.setEnabled(false);
                mPositiveButton.setText(R.string.next);
                break;
            case PAGE_DETAILS_ID:
                mNegativeButton.setEnabled(true);
                mPositiveButton.setText(R.string.next);
                break;
            case PAGE_SETTINGS_ID:
                mNegativeButton.setEnabled(true);
                mPositiveButton.setText(isEdit() ? R.string.save : R.string.create);
                break;
        }
        mBasicInfoPage.setVisibility(mPageId == PAGE_BASIC_INFO_ID ? View.VISIBLE : View.GONE);
        mDetailPage.setVisibility(mPageId == PAGE_DETAILS_ID ? View.VISIBLE : View.GONE);
        mSettingsPage.setVisibility(mPageId == PAGE_SETTINGS_ID ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar birthday = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        mProfileCreator.setBirthday(birthday);
        setBirthdayDisplay(birthday);
    }

    private void setBirthdayDisplay(Calendar birthday) {
        mBirthdayView.setText(SimpleDateFormat.getDateInstance().format(birthday.getTime()));
    }

    private boolean isEdit() {
        return getArguments().getBoolean(ARG_IS_EDIT, false);
    }

    private float getWeight() {
        boolean isKg = mWeightUnitsSwitch.isChecked();
        float weight = 0.0f;
        try {
            weight = Float.parseFloat(mWeightView.getText().toString());
        } catch (NumberFormatException e) {}
        return isKg ? weight : (weight * 0.45359237f);
    }

    private int getGender() {
        switch (mGenderSpinner.getSelectedItemPosition()) {
            case 1:
                return Profile.Gender.MALE;
            case 2:
                return Profile.Gender.FEMALE;
            default:
                return Profile.Gender.NONE;
        }
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private final class UpdateProfileListener implements ResultCallback<LoadProfilesResult> {

        private final boolean mIsCreate;
        public UpdateProfileListener(boolean isCreate) {
            mIsCreate = isCreate;
        }

        @Override
        public void onResult(LoadProfilesResult result) {
            if (result.getStatus() == Concept2StatusCodes.OK) {
                // Update the current profile.
                if (mIsCreate) {
                    NavDrawerAdapter.addProfile(result.getProfiles().get(0));
                } else {
                    NavDrawerAdapter.updateProfile(result.getProfiles().get(0));
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Failed to create profile", Toast.LENGTH_LONG).show();
            }
        }
    }
}

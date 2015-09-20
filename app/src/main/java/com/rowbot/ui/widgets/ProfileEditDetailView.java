package com.rowbot.ui.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileCreator;
import com.rowbot.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProfileEditDetailView extends LinearLayout implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText mBirthdayView;
    private EditText mWeightView;
    private Switch mWeightUnitsSwitch;
    private Spinner mGenderSpinner;

    private Calendar mBirthday;
    public ProfileEditDetailView(Context context) {
        super(context);
        init();
    }

    public ProfileEditDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileEditDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.profile_edit_details_view, this);
        mBirthdayView = (EditText) root.findViewById(R.id.birthday);
        mWeightView = (EditText) root.findViewById(R.id.weight);
        mWeightUnitsSwitch = (Switch) root.findViewById(R.id.weight_units);
        mGenderSpinner = (Spinner) root.findViewById(R.id.gender);

        mBirthdayView.setOnClickListener(this);
    }

    public void setProfile(Profile profile) {
        setBirthdayDisplay(profile.getBirthday());
        if (profile.getWeight() != 0.0f) {
            NumberFormat format = DecimalFormat.getInstance();
            format.setMaximumFractionDigits(1);
            mWeightView.setText(format.format(profile.getWeight()));
        }
        mGenderSpinner.setSelection(profile.getGender());
    }

    public void updateProfileCreator(ProfileCreator creator) {
        creator.setBirthday(mBirthday)
                .setGender(getGender())
                .setWeight(getWeight());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthday: {
                Calendar birthday = mBirthday == null ? new GregorianCalendar() : mBirthday;
                new DatePickerDialog(getContext(), this, birthday.get(Calendar.YEAR),
                        birthday.get(Calendar.MONTH), birthday.get(Calendar.DAY_OF_MONTH)).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setBirthdayDisplay(new GregorianCalendar(year, monthOfYear, dayOfMonth));
    }

    private void setBirthdayDisplay(Calendar birthday) {
        if (birthday != null) {
            mBirthday = birthday;
            mBirthdayView.setText(SimpleDateFormat.getDateInstance().format(birthday.getTime()));
        }
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
}

package com.rowbot.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.concept2.api.rowbot.profile.Profile;
import com.concept2.api.rowbot.profile.ProfileCreator;
import com.rowbot.R;
import com.rowbot.utils.StockImageUtils;

public class ProfileEditMainView extends LinearLayout {

    private ImageRailView mImageRailView;
    private EditText mUserName;
    private EditText mTeamName;

    public ProfileEditMainView(Context context) {
        super(context);
        init();
    }

    public ProfileEditMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileEditMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.profile_edit_main_view, this);
        mImageRailView = (ImageRailView) root.findViewById(R.id.image_rail);
        mUserName = (EditText) root.findViewById(R.id.user_name);
        mTeamName = (EditText) root.findViewById(R.id.team_name);

        mImageRailView.setImages(StockImageUtils.STOCK_AVATAR_IMAGE_RES_IDS);
    }

    public void setProfile(Profile profile) {
        mImageRailView.setSelectedImage(profile.getImageId());
        mUserName.setText(profile.getName());
        mTeamName.setText(profile.getTeamName());
    }

    public void updateProfileCreator(ProfileCreator creator) {
        creator.setImageId(mImageRailView.getSelectedImage())
                .setName(mUserName.getText().toString())
                .setTeamName(mTeamName.getText().toString());
    }
}

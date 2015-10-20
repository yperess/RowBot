package org.uvdev.rowbot.ui.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import org.uvdev.rowbot.R;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.utils.StockImageUtils;

import java.text.NumberFormat;

public class ProfileCardView extends CardView {

    private ImageView mProfileImageView;
    private TextView mUserNameView;
    private TextView mTeamNameView;
    private TextView mSeasonMetersView;
    private TextView mLifetimeMetersView;

    public ProfileCardView(Context context) {
        super(context);
        init();
    }

    public ProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mProfileImageView = (ImageView) findViewById(R.id.profile_pic);
        mUserNameView = (TextView) findViewById(R.id.user_name);
        mTeamNameView = (TextView) findViewById(R.id.team_name);
        mSeasonMetersView = (TextView) findViewById(R.id.season_meters);
        mLifetimeMetersView = (TextView) findViewById(R.id.lifetime_meters);
    }

    public void setProfile(Profile profile) {
        mProfileImageView.setImageResource(StockImageUtils.getStockImageResId(profile));
        mUserNameView.setText(profile.getName());
        mTeamNameView.setText(profile.getTeamName());
        mSeasonMetersView.setText(getContext().getString(R.string.profile_season_meters,
                NumberFormat.getInstance().format(profile.getSeasonMeters())));
        mLifetimeMetersView.setText(getContext().getString(R.string.profile_lifetime_meters,
                NumberFormat.getInstance().format(profile.getLifetimeMeters())));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                return true;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }
}

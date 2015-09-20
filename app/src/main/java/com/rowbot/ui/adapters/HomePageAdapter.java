package com.rowbot.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.concept2.api.rowbot.logbook.Workout;
import com.concept2.api.rowbot.profile.Profile;
import com.rowbot.MainActivity;
import com.rowbot.R;
import com.rowbot.model.PersonalRecord;
import com.rowbot.ui.fragments.ProfileDetailsFragment;
import com.rowbot.utils.StockImageUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class HomePageAdapter extends RecyclerView.Adapter<ViewHolder> implements
        View.OnClickListener {

    private static final String TAG = "HomePageAdapter";

    private static final int PROFILE_SUMMARY_CARD_POSITION = 0;
    private static final int PR_SUMMARY_CARD_POSITION = 1;

    private static final long WEEK_IN_MILLIS = TimeUnit.DAYS.toMillis(7);

    private final MainActivity mActivity;

    private Profile mProfile;
    private ArrayList<PersonalRecord> mPersonalRecords = new ArrayList<>();
    private ArrayList<ArrayList<Workout>> mWorkoutSummaryCards = new ArrayList<>();
    private boolean mShowSeeMoreWorkouts = false;

    public HomePageAdapter(MainActivity activity) {
        mActivity = activity;
    }

    public void updateProfile(Profile profile) {
        mProfile = profile;
        notifyDataSetChanged();
    }

    public void updatePersonalRecords(Collection<PersonalRecord> personalRecords) {
        mPersonalRecords.clear();
        mPersonalRecords.addAll(personalRecords);
        notifyItemChanged(PR_SUMMARY_CARD_POSITION);
    }

    /**
     * Add a workout summary card (assuming chronological order). The date range must be at most 1
     * month.
     *
     * @param workouts
     */
    public void addWorkoutSummaryCard(ArrayList<Workout> workouts) {
        mWorkoutSummaryCards.add(workouts);
        notifyItemInserted(1 + mWorkoutSummaryCards.size());
    }

    public void showSeeMoreWorkouts(boolean showSeeMoreWorkouts) {
        if (mShowSeeMoreWorkouts != showSeeMoreWorkouts) {
            mShowSeeMoreWorkouts = showSeeMoreWorkouts;
            if (showSeeMoreWorkouts) {
                notifyItemInserted(getItemCount() - 1);
            } else {
                notifyItemRemoved(getItemCount());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.id.home_null_state_card: {
                View view = LayoutInflater.from(mActivity)
                        .inflate(R.layout.home_null_state_card, parent, false);
                view.setId(R.id.home_null_state_card);
                return new BaseViewHolder(view);
            }
            case R.id.profile_summary_card: {
                View view = LayoutInflater.from(mActivity)
                        .inflate(R.layout.profile_card, parent, false);
                view.setId(R.id.profile_summary_card);
                view.setOnClickListener(this);
                return new ProfileSummaryViewHolder(view);
            }
            case R.id.pr_summary_card: {
                View view = LayoutInflater.from(mActivity)
                        .inflate(R.layout.pr_summary_card, parent, false);
                return new PrSummaryViewHolder(view);
            }
            case R.id.workout_summary_card: {
                return new WorkoutSummaryViewHolder(null);
            }
            case R.id.more_workout_history_card: {
                return new MoreWorkoutHistoryViewHolder(null);
            }
            default:
                Log.w(TAG, "Unknown view type " + viewType);
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ProfileSummaryViewHolder) {
            ((ProfileSummaryViewHolder) holder).setProfile(mProfile);
        } else if (holder instanceof PrSummaryViewHolder) {
            ((PrSummaryViewHolder) holder).setPersonalRecords(mPersonalRecords);
        } else if (holder instanceof WorkoutSummaryViewHolder) {

        } else if (holder instanceof MoreWorkoutHistoryViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        // Profile summary, PR summary, Workout history, see more.
        return mProfile == null ? 1
                : 2 + mWorkoutSummaryCards.size() + (mShowSeeMoreWorkouts ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (mProfile == null) {
            return R.id.home_null_state_card;
        }
        switch (position){
            case PROFILE_SUMMARY_CARD_POSITION:
                return R.id.profile_summary_card;
            case PR_SUMMARY_CARD_POSITION:
                return R.id.pr_summary_card;
            default:
                return (position == getItemCount() - 1 && mShowSeeMoreWorkouts)
                        ? R.id.more_workout_history_card
                        : R.id.workout_summary_card;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_summary_card:
                mActivity.showFragment(new ProfileDetailsFragment(), false /* hasNavDrawer */);
                break;
        }
    }

    public class BaseViewHolder extends ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public final class ProfileSummaryViewHolder extends ViewHolder implements View.OnClickListener {

        private final ImageView mProfileImageView;
        private final TextView mUserNameView;
        private final TextView mTeamNameView;
        private final TextView mSeasonMetersView;
        private final TextView mLifetimeMetersView;

        public ProfileSummaryViewHolder(View root) {
            super(root);
            mProfileImageView = (ImageView) root.findViewById(R.id.profile_pic);
            mUserNameView = (TextView) root.findViewById(R.id.user_name);
            mTeamNameView = (TextView) root.findViewById(R.id.team_name);
            mSeasonMetersView = (TextView) root.findViewById(R.id.season_meters);
            mLifetimeMetersView = (TextView) root.findViewById(R.id.lifetime_meters);
            root.setOnClickListener(this);
        }

        public void setProfile(Profile profile) {
            if (profile == null) return;
            mProfileImageView.setImageResource(StockImageUtils.getStockImageResId(profile));
            mUserNameView.setText(profile.getName());
            mTeamNameView.setText(profile.getTeamName());
            mSeasonMetersView.setText(mActivity.getString(R.string.profile_season_meters,
                    NumberFormat.getInstance().format(profile.getSeasonMeters())));
            mLifetimeMetersView.setText(mActivity.getString(R.string.profile_lifetime_meters,
                    NumberFormat.getInstance().format(profile.getLifetimeMeters())));
        }

        @Override
        public void onClick(View view) {
            mActivity.showFragment(new ProfileDetailsFragment(), false /* hasNavDrawer */);
        }
    }

    public final class PrSummaryViewHolder extends ViewHolder {
        public PrSummaryViewHolder(View root) {
            super(root);
        }

        public void setPersonalRecords(ArrayList<PersonalRecord> personalRecords) {

        }
    }

    public final class WorkoutSummaryViewHolder extends ViewHolder {
        public WorkoutSummaryViewHolder(View root) {
            super(root);
        }
    }

    public final class MoreWorkoutHistoryViewHolder extends ViewHolder {
        public MoreWorkoutHistoryViewHolder(View root) {
            super(root);
        }
    }
}

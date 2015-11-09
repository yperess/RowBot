package org.uvdev.rowbot.ui.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.concept2.api.common.Constants;
import org.uvdev.rowbot.concept2api.rowbot.profile.Profile;
import org.uvdev.rowbot.MainActivity;
import org.uvdev.rowbot.R;
import org.uvdev.rowbot.model.RowBotActivity;
import org.uvdev.rowbot.ui.dialogs.ProfileEditDialogFragment;
import org.uvdev.rowbot.ui.fragments.DebugFragment;
import org.uvdev.rowbot.ui.fragments.HelpAndFeedbackFragment;
import org.uvdev.rowbot.ui.fragments.SettingsFragment;
import org.uvdev.rowbot.utils.StockImageUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.BaseViewHolder>
        implements Observer {

    private static final String TAG = "NavDrawerAdapter";
    private static final boolean DBG = false;

    private static final Object sInstanceLock = new Object();
    private static NavDrawerAdapter sInstance;
    private static boolean sNavigationMode = true;
    private static ArrayList<LineItem> sItems = new ArrayList<>();
    private static ArrayList<Profile> sProfiles = new ArrayList<>();

    // For now this will always be 0 since it's the home button and we don't show the menu for any
    // other screens.
    private static final int sSelectedItem = 0;

    public static NavDrawerAdapter getInstance(MainActivity context) {
        synchronized (sInstanceLock) {
            if (sInstance == null || !sInstance.mContext.equals(context)) {
                sInstance = new NavDrawerAdapter(context);
            }
        }
        return sInstance;
    }

    public static void setProfiles(List<Profile> profiles) {
        sProfiles.clear();
        sProfiles.addAll(profiles);
        RowBotActivity.CURRENT_PROFILE.setValue(sProfiles.get(0))
                .notifyObservers();
        synchronized (sInstanceLock) {
            if (sInstance != null) {
                sInstance.notifyDataSetChanged();
            }
        }
    }

    public static void addProfile(Profile profile) {
        sProfiles.add(profile);
        selectProfile(profile.getProfileId());
    }

    public static void deleteProfile(String profileId) {
        Iterator<Profile> iterator = sProfiles.iterator();
        while (iterator.hasNext()) {
            Profile profile = iterator.next();
            if (profileId.equals(profile.getProfileId())) {
                iterator.remove();
                break;
            }
        }

        if (sProfiles.isEmpty()) {
            // Force user to create a new profile.
            RowBotActivity.CURRENT_PROFILE.setValue(null).notifyObservers();
            synchronized (sInstanceLock) {
                if (sInstance != null) {
                    sInstance.mContext.showHome();
                    sInstance.mContext.showCreateProfileDialog();
                }
            }
        } else {
            selectProfile(sProfiles.get(0).getProfileId());
        }
    }

    public static void updateProfile(Profile profile) {
        for (int i = 0, size = sProfiles.size(); i < size; ++i) {
            if (sProfiles.get(i).getProfileId().equals(profile.getProfileId())) {
                sProfiles.set(i, profile);
                if (i == 0) {
                    RowBotActivity.CURRENT_PROFILE.setValue(profile).notifyObservers();
                } else {
                    synchronized (sInstanceLock) {
                        sInstance.notifyItemChanged(i);
                    }
                }
                break;
            }
        }
    }

    public static void selectProfile(String profileId) {
        if (sProfiles.isEmpty()) {
            return;
        }
        // Search for the profile.
        Iterator<Profile> iterator = sProfiles.iterator();
        while (iterator.hasNext()) {
            Profile profile = iterator.next();
            if (profileId.equals(profile.getProfileId())) {
                // Profile was found, update the current profile.
                iterator.remove();
                sProfiles.add(0, profile);
                RowBotActivity.CURRENT_PROFILE.setValue(profile)
                        .notifyObservers();
                sNavigationMode = true;
                synchronized (sInstanceLock) {
                    if (sInstance != null) {
                        sInstance.mContext.closeDrawers();
                        sInstance.mContext.editSharedPreferences()
                                .putString(Constants.SHARED_PREF_SELECTED_PROFILE_IDS,
                                        getProfileIdsAsCsv());
                        sInstance.notifyDataSetChanged();
                    }
                }
                break;
            }
        }
    }

    private static String getProfileIdsAsCsv() {
        if (sProfiles.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = sProfiles.size(); i < size; ++i) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(sProfiles.get(i).getProfileId());
        }
        return builder.toString();
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_MENU_ITEM = 1;
    private static final int TYPE_PROFILE = 2;
    private static final int TYPE_NEW_PROFILE = 3;

    private MainActivity mContext;

    private final int mSelectedColor;
    private final int mDefaultColor;
    private final int mSelectedBackgroundColor;

    private NavDrawerAdapter(MainActivity context) {
        mContext = context;
        RowBotActivity.CURRENT_PROFILE.addObserver(this);
        sItems.clear();
        sItems.add(new LineItem(R.string.rowbot_nav_drawer_home, 0 /* icon */, false));
        sItems.add(new LineItem(R.string.rowbot_nav_drawer_debug, 0 /* icon */, false) {
            @Override
            public void run() {
                mContext.showFragment(new DebugFragment());
            }
        });
        sItems.add(new LineItem(R.string.rowbot_nav_drawer_settings,
                R.drawable.ic_action_settings, true) {
            @Override
            public void run() {
                mContext.showFragment(new SettingsFragment());
            }
        });
        sItems.add(new LineItem(R.string.rowbot_nav_drawer_help_and_feedback,
                R.drawable.ic_action_help, false) {
            @Override
            public void run() {
                mContext.showFragment(new HelpAndFeedbackFragment());
            }
        });

        Resources res = context.getResources();
        mSelectedColor = res.getColor(R.color.primary);
        mDefaultColor = res.getColor(android.R.color.primary_text_light);
        mSelectedBackgroundColor = res.getColor(R.color.rowbot_nav_selected_background);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.navdrawer_header, parent, false);
                return new NavigationHeaderViewHolder(this, view);
            }
            case TYPE_MENU_ITEM: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.navdrawer_item, parent, false);
                return new NavigationItemViewHolder(this, view);
            }
            case TYPE_PROFILE: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.navdrawer_profile, parent, false);
                return new ProfileViewHolder(this, view);
            }
            case TYPE_NEW_PROFILE: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.navdrawer_new_profile, parent, false);
                return new NewProfileViewHolder(this, view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return 1 + (sNavigationMode ? sItems.size() : sProfiles.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (sNavigationMode) {
            // Home, Debug, Divider/Settings, Help & Feedback
            return TYPE_MENU_ITEM;
        } else {
            return position == getItemCount() - 1 ? TYPE_NEW_PROFILE : TYPE_PROFILE;
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && data instanceof Profile) {
            // Update the profile object, then select it.
            Profile profile = (Profile) data;
            for (int i = 0, size = sProfiles.size(); i < size; ++i) {
                if (sProfiles.get(i).getProfileId().equals(profile.getProfileId())) {
                    sProfiles.set(i, profile);
                    break;
                }
            }
            selectProfile(((Profile) data).getProfileId());
        }
    }

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        protected final NavDrawerAdapter mAdapter;
        public BaseViewHolder(NavDrawerAdapter adapter, View itemView) {
            super(itemView);
            mAdapter = adapter;
        }

        abstract protected void onBind(int position);
    }

    private static class NavigationHeaderViewHolder extends BaseViewHolder implements
            View.OnClickListener {

        private final ImageView mProfileImage;
        private final View mProfileSelector;
        private final TextView mName;
        private final TextView mTeamName;

        public NavigationHeaderViewHolder(NavDrawerAdapter adapter, View itemView) {
            super(adapter, itemView);
            mProfileImage = (ImageView) itemView.findViewById(R.id.profile_pic);
            mProfileSelector = itemView.findViewById(R.id.profile_selector);
            mName = (TextView) itemView.findViewById(R.id.user_name);
            mTeamName = (TextView) itemView.findViewById(R.id.team_name);

            itemView.setOnClickListener(this);
            mProfileSelector.setOnClickListener(this);
        }

        @Override
        protected void onBind(int position) {
            if (sProfiles.isEmpty()) {
                return;
            }
            Profile profile = sProfiles.get(0);
            mProfileImage.setImageResource(StockImageUtils.getStockImageResId(profile));
            mName.setText(profile.getName());
            mTeamName.setText(profile.getTeamName());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.profile_selector && sNavigationMode) {
                sNavigationMode = false;
                mAdapter.notifyDataSetChanged();
            } else if (!sNavigationMode) {
                sNavigationMode = true;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private static class NavigationItemViewHolder extends BaseViewHolder implements
            View.OnClickListener {

        private final View mDivider;
        private final ImageView mIcon;
        private final TextView mLabel;

        public NavigationItemViewHolder(NavDrawerAdapter adapter, View itemView) {
            super(adapter, itemView);
            itemView.setOnClickListener(this);
            mDivider = itemView.findViewById(R.id.divider);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mLabel = (TextView) itemView.findViewById(R.id.label);
        }

        @Override
        protected void onBind(int position) {
            LineItem item = sItems.get(position - 1);
            boolean isSelected = sNavigationMode && position -1 == sSelectedItem;

            mLabel.setTypeface(null, isSelected ? Typeface.BOLD : Typeface.NORMAL);
            mLabel.setTextColor(isSelected ? mAdapter.mSelectedColor : mAdapter.mDefaultColor);
            mIcon.setColorFilter(isSelected ? mAdapter.mSelectedColor : Color.TRANSPARENT);
            itemView.setBackgroundColor(isSelected ? mAdapter.mSelectedBackgroundColor
                    : Color.WHITE);

            if (item.iconResource != 0) {
                mIcon.setImageResource(item.iconResource);
                mIcon.setVisibility(View.VISIBLE);
            } else {
                mIcon.setVisibility(View.GONE);
            }
            mDivider.setVisibility(item.hasTopDivider ? View.VISIBLE : View.GONE);
            mLabel.setText(item.labelResource);
        }

        @Override
        public void onClick(View v) {
            mAdapter.sItems.get(getAdapterPosition() - 1).run();
        }
    }

    private static final class ProfileViewHolder extends BaseViewHolder implements
            View.OnClickListener {

        private final ImageView mProfileImage;
        private final TextView mUserName;

        public ProfileViewHolder(NavDrawerAdapter adapter, View itemView) {
            super(adapter, itemView);
            mProfileImage = (ImageView) itemView.findViewById(R.id.profile_pic);
            mUserName = (TextView) itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind(int position) {
            Profile profile = mAdapter.sProfiles.get(position);
            mProfileImage.setImageResource(StockImageUtils.getStockImageResId(profile));
            mUserName.setText(profile.getName());
        }

        @Override
        public void onClick(View v) {
            Profile profile = mAdapter.sProfiles.get(getAdapterPosition());
            NavDrawerAdapter.selectProfile(profile.getProfileId());
        }
    }

    private static final class NewProfileViewHolder extends BaseViewHolder implements
            View.OnClickListener {
        public NewProfileViewHolder(NavDrawerAdapter adapter, View itemView) {
            super(adapter, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBind(int position) {

        }

        @Override
        public void onClick(View v) {
            ProfileEditDialogFragment.createInstance(false /* isEdit */, true /* cancelable */)
                    .show(mAdapter.mContext.getSupportFragmentManager(),
                            ProfileEditDialogFragment.TAG);
        }
    }

    private class LineItem implements Runnable {
        public final int labelResource;
        public final int iconResource;
        public final boolean hasTopDivider;

        public LineItem(int labelResource, int iconResource, boolean hasTopDivider) {
            this.labelResource = labelResource;
            this.iconResource = iconResource;
            this.hasTopDivider = hasTopDivider;
        }

        @Override
        public void run() {}
    }
}

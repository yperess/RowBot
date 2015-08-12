package com.rowbot.ui.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.rowbot.MainActivity;
import com.rowbot.R;
import com.rowbot.ui.fragments.DebugFragment;
import com.rowbot.ui.fragments.HelpAndFeedbackFragment;
import com.rowbot.ui.fragments.SettingsFragment;

import java.util.ArrayList;

public class NavDrawerAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "NavDrawerAdapter";
    private static final boolean DBG = false;

    private MainActivity mContext;
    private ArrayList<LineItem> mItems;
    private int mSelectedIndex = -1;

    private final int mSelectedColor;
    private final int mDefaultColor;
    private final int mSelectedBackgroundColor;

    public NavDrawerAdapter(MainActivity context) {
        mContext = context;
        mItems = new ArrayList<>();
        mItems.add(new LineItem(R.string.rowbot_nav_drawer_home, 0 /* icon */, false));
        mItems.add(new LineItem(R.string.rowbot_nav_drawer_debug, 0 /* icon */, false));
        mItems.add(new LineItem(R.string.rowbot_nav_drawer_settings, R.drawable.ic_action_settings,
                true));
        mItems.add(new LineItem(R.string.rowbot_nav_drawer_help_and_feedback,
                R.drawable.ic_action_help, false));

        Resources res = context.getResources();
        mSelectedColor = res.getColor(R.color.rowbot_theme_primary);
        mDefaultColor = res.getColor(android.R.color.primary_text_light);
        mSelectedBackgroundColor = res.getColor(R.color.rowbot_nav_selected_background);
    }

    public void setSelected(int index) {
//        if (mSelectedIndex == index) return;

        FragmentManager fragmentManager = mContext.getSupportFragmentManager();

        Fragment fragment;
        switch (index) {
            case 1:
                fragment = new DebugFragment();
                break;
            case 2:
                fragment = new SettingsFragment();
                break;
            case 3:
                fragment = new HelpAndFeedbackFragment();
                break;
            default:
                return;
        }
//        mSelectedIndex = index;
        mContext.showFragment(fragment, false);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public LineItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.navdrawer_item, parent,
                    false /* attachToRoot */);
            convertView.setTag(new ItemViewHolder(convertView));
        }
        final ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        viewHolder.populateViews(getItem(position));

        // Highlight the selected item.
        boolean isSelected = position == mSelectedIndex;
        TextView labelText = (TextView) convertView.findViewById(R.id.label);
        labelText.setTypeface(null, isSelected ? Typeface.BOLD : Typeface.NORMAL);
        labelText.setTextColor(isSelected ? mSelectedColor : mDefaultColor);
        ((ImageView) convertView.findViewById(R.id.icon)).setColorFilter(
                isSelected ? mSelectedColor : Color.TRANSPARENT);
        convertView.findViewById(R.id.content_line).setBackgroundColor(isSelected
                ? mSelectedBackgroundColor : Color.WHITE);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Reset the back stack to the home screen. Navigating back from the home screen will
     * exit the app.
     */
    private void resetBackStack() {
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.popBackStack("DEBUG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.popBackStack("SETTINGS", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.popBackStack("HELP", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private static final class ItemViewHolder {

        private View mDivider;
        private ImageView mIcon;
        private TextView mLabel;

        public ItemViewHolder(View view) {
            mDivider = view.findViewById(R.id.divider);
            mIcon = (ImageView) view.findViewById(R.id.icon);
            mLabel = (TextView) view.findViewById(R.id.label);
        }

        public void populateViews(LineItem item) {
            if (item.iconResource != 0) {
                mIcon.setImageResource(item.iconResource);
                mIcon.setVisibility(View.VISIBLE);
            } else {
                mIcon.setVisibility(View.GONE);
            }
            mDivider.setVisibility(item.hasTopDivider ? View.VISIBLE : View.GONE);
            mLabel.setText(item.labelText);
        }
    }

    private final class LineItem {
        public final String labelText;
        public final int iconResource;
        public final boolean hasTopDivider;

        public LineItem(int labelResource, int iconResource, boolean hasTopDivider) {
            this.labelText = mContext.getString(labelResource);
            this.iconResource = iconResource;
            this.hasTopDivider = hasTopDivider;
        }
    }
}

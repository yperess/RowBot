package org.uvdev.rowbot.ui.adapters;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.uvdev.rowbot.R;

import java.util.ArrayList;

public class HelpAndFeedbackAdapter implements ListAdapter {

    private Context mContext;
    private ArrayList<String> mActions;
    private DataSetObserver mObserver;

    public HelpAndFeedbackAdapter(Context context, ArrayList<String> actions) {
        mContext = context;
        mActions = actions;
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
    public void registerDataSetObserver(DataSetObserver observer) {
        mObserver = observer;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mObserver = null;
    }

    @Override
    public int getCount() {
        return mActions.size();
    }

    @Override
    public String getItem(int position) {
        return mActions.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.release_notes_item, parent,
                    false /* attachToRoot */);
            convertView.setTag(mActions.get(position));
        }
        String label = (String) convertView.getTag();
        ((TextView) convertView.findViewById(R.id.label)).setText(label);
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
        return mActions.isEmpty();
    }
}

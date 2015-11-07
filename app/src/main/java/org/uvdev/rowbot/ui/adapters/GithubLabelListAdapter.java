package org.uvdev.rowbot.ui.adapters;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.alorma.github.sdk.bean.dto.response.Label;
import org.uvdev.rowbot.R;

import java.util.ArrayList;
import java.util.List;

public class GithubLabelListAdapter implements ListAdapter {

    private final ArrayList<Label> mLabels = new ArrayList<>();

    public void setLabels(List<Label> labels) {
        mLabels.clear();
        mLabels.addAll(labels);
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

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mLabels.size();
    }

    @Override
    public Label getItem(int position) {
        return mLabels.get(position);
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
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.github_label_view,
                    parent, false);
        }

        Label label = getItem(position);
        ((TextView) view.findViewById(R.id.name)).setText(label.name);
        ((ImageView) view.findViewById(R.id.color))
                .setColorFilter(Color.parseColor("#" + label.color));
        return view;
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
        return mLabels.isEmpty();
    }
}

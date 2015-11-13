package org.uvdev.rowbot.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.uvdev.rowbot.R;

import java.util.ArrayList;
import java.util.Collection;

public class HomeFabListAdapter extends RecyclerView.Adapter<HomeFabListAdapter.ViewHolder> {

    public interface FabListOnClickListener {
        void onFabClicked(int position);
    }

    private final ArrayList<DataItem> mDataItems = new ArrayList<>();
    private FabListOnClickListener mListener;

    public void setDataItems(Collection<DataItem> dataItems) {
        mDataItems.clear();
        mDataItems.addAll(dataItems);
        notifyDataSetChanged();
    }

    public void setFabListOnClickListener(FabListOnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fab_action_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDataItems.size();
    }

    public static final class DataItem {
        public final String title;
        public final int titleResId;
        public final int iconResId;

        public DataItem(String title, int iconResId) {
            this.title = title;
            this.titleResId = 0;
            this.iconResId = iconResId;
        }

        public DataItem(int titleResId, int iconResId) {
            this.title = null;
            this.titleResId = titleResId;
            this.iconResId = iconResId;
        }
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int mDataPosition;
        TextView mDescription;
        ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mDescription = (TextView) itemView.findViewById(R.id.description);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mDataPosition = position;
            DataItem dataItem = mDataItems.get(position);
            if (dataItem.titleResId > 0) {
                mDescription.setText(dataItem.titleResId);
            } else {
                mDescription.setText(dataItem.title);
            }
            mIcon.setImageResource(dataItem.iconResId);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onFabClicked(mDataPosition);
            }
        }
    }
}

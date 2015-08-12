package com.rowbot.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rowbot.R;
import com.rowbot.model.PersonalRecord;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class BasePrAdapter extends RecyclerView.Adapter<BasePrAdapter.PersonalRecordViewHolder> {
    private final Context mContext;
    private final ArrayList<PersonalRecord> mPersonalRecords = new ArrayList<>();
    private int mLastPosition = -1;

    public BasePrAdapter(Context context) {
        mContext = context;
    }

    public void addPersonalRecords(Collection<PersonalRecord> records) {
        int pos = mPersonalRecords.size();
        mPersonalRecords.addAll(records);
        for (int i = 0, size = records.size(); i < size; ++i) {
            notifyItemInserted(pos + i);
        }
//        notifyDataSetChanged();
    }

    @Override
    public PersonalRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonalRecordViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.pr_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PersonalRecordViewHolder holder, int position) {
        holder.onBind(mPersonalRecords.get(position));
        if (position > mLastPosition) {
            // Add animation
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mPersonalRecords.size();
    }

    public final class PersonalRecordViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIconView;
        private final TextView mValueView;
        private final TextView mDateView;

        public PersonalRecordViewHolder(View root) {
            super(root);
            mIconView = (ImageView) root.findViewById(R.id.icon);
            mValueView = (TextView) root.findViewById(R.id.value);
            mDateView = (TextView) root.findViewById(R.id.date);
        }

        public void onBind(PersonalRecord record) {
            String value;
            if (record.getValue() == 0.0 || record.getDate() == null) {
                value = null;
            } else if (record.getRecordType() == PersonalRecord.TYPE_FIXED_DISTANCE) {
                String pattern = mContext.getString(
                        record.getValue() >= TimeUnit.HOURS.toMillis(1)
                                ? R.string.pr_item_hour_time : R.string.pr_item_minute_time);
                DateFormat format = new SimpleDateFormat(pattern);
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                value = format.format(record.getValue());
            } else {
                value = NumberFormat.getInstance().format(record.getValue()) + "m";
            }
            String date;
            if (value == null) {
                value = date = "------";
            } else {
                value = mContext.getString(R.string.pr_item_value, value, record.getStrokeRate());
                date = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(
                        record.getDate().getTime());
            }
            mIconView.setImageResource(record.getIconResId());
            mValueView.setText(value);
            mDateView.setText(date);
        }
    }
}

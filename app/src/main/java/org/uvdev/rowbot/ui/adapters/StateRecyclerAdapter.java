package org.uvdev.rowbot.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.uvdev.rowbot.concept2api.utils.Preconditions;

public abstract class StateRecyclerAdapter<V extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<V> {

    private static final String TAG = "StateRecycler";

    public static final int STATE_OK = Activity.RESULT_OK;

    private final View mContainer;
    private int mState = Integer.MIN_VALUE;

    public StateRecyclerAdapter(View container) {
        mContainer = Preconditions.assertNotNull(container);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        mState = state;
        notifyDataSetChanged();
    }

    public int getState() {
        return mState;
    }

    @Override
    public final void onBindViewHolder(V holder, int position) {
        onBindViewHolder(holder, mState, position);
        if (holder instanceof StateViewHolder && ((StateViewHolder) holder).fillViewPort()) {
            // Holder should be the same size as the container.
            int height = mContainer.getHeight();
            Log.d(TAG, "Resizing view to " + height);
            holder.itemView.getLayoutParams().height = height;
        }
    }

    @Override
    public final int getItemViewType(int position) {
        return getItemViewType(mState, position);
    }

    @Override
    public final int getItemCount() {
        return getItemCount(mState);
    }

    protected int getItemViewType(int state, int position) {
        return 0;
    }

    public abstract void onBindViewHolder(V holder, int state, int position);

    public abstract int getItemCount(int state);

    public interface StateViewHolder {
        boolean fillViewPort();
    }
}

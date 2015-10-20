package org.uvdev.rowbot.concept2api;

import java.util.ArrayList;

public abstract class DataBufferRef<R> implements Result, Releaseable {

    protected final DataHolder mHolder;
    protected final ArrayList<R> mResults;

    public DataBufferRef(DataHolder holder) {
        mHolder = holder;
        mResults = new ArrayList<>(mHolder.getCount());
        for (int i = 0, size = mHolder.getCount(); i < size; ++i) {
            mResults.add(getItem(mHolder, i));
        }
    }

    abstract protected R getItem(DataHolder holder, int row);

    @Override
    public int getStatus() {
        return mHolder.getStatus();
    }

    @Override
    public void release() {
        mHolder.release();
    }
}

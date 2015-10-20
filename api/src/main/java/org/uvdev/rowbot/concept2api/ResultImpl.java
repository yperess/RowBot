package org.uvdev.rowbot.concept2api;

public class ResultImpl implements Result {

    protected final int mStatus;

    public ResultImpl(int status) {
        mStatus = status;
    }
    @Override
    public int getStatus() {
        return mStatus;
    }
}

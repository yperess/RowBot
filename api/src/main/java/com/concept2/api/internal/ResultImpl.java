package com.concept2.api.internal;

import com.concept2.api.Result;

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

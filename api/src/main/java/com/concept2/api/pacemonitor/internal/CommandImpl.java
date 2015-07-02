package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.ResultCallback;
import com.concept2.api.pacemonitor.CommandBatch;
import com.concept2.api.pacemonitor.PaceMonitorResult;

public final class CommandImpl<R extends PaceMonitorResult> implements CommandBatch.Command<R> {

    private final int mCommandId;
    private final Bundle mArgs;
    private final ResultCallback<R> mCallback;

    public CommandImpl(int commandId, ResultCallback<R> callback) {
        this(commandId, null /* args */, callback);
    }

    public CommandImpl(int commandId, Bundle args, ResultCallback<R> callback) {
        mCommandId = commandId;
        mArgs = args;
        mCallback = callback;
    }

    @Override
    public ResultCallback<R> getResultCallback() {
        return mCallback;
    }
}

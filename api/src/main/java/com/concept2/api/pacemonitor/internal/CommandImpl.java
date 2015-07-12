package com.concept2.api.pacemonitor.internal;

import android.os.Bundle;

import com.concept2.api.ResultCallback;
import com.concept2.api.pacemonitor.CommandBuilder;
import com.concept2.api.pacemonitor.PaceMonitorResult;
import com.concept2.api.utils.Objects;

import java.util.Arrays;

public final class CommandImpl<R extends PaceMonitorResult> implements CommandBuilder.Command<R> {

    private final byte mCommandId;
    private final byte[] mData;
    private final boolean mIsCustomCommand;
    private final ResultCallback<R> mCallback;

    private final byte[] mCommandBytes;

    public CommandImpl(byte commandId, ResultCallback<R> callback) {
        this(commandId, false /* isCustomCommand */, null /* data */, callback);
    }

    public CommandImpl(byte commandId, byte[] data, ResultCallback<R> callback) {
        this(commandId, false /* isCustomCommand */, data, callback);
    }

    public CommandImpl(byte commandId, boolean isCustomCommand, ResultCallback<R> callback) {
        this(commandId, isCustomCommand, null /* args */, callback);
    }

    public CommandImpl(byte commandId, boolean isCustomCommand, byte[] data,
            ResultCallback<R> callback) {
        mCommandId = commandId;
        mIsCustomCommand = isCustomCommand;
        mData = data;
        mCallback = callback;
        if (mData == null || mData.length == 0) {
            mCommandBytes = new byte[] { mCommandId };
        } else {
            mCommandBytes = new byte[mData.length + 2];
            mCommandBytes[0] = mCommandId;
            mCommandBytes[1] = (byte) mData.length;
            System.arraycopy(mData, 0, mCommandBytes, 2, mData.length);
        }
    }

    @Override
    public ResultCallback<R> getResultCallback() {
        return mCallback;
    }

    public boolean isCustomCommand() {
        return mIsCustomCommand;
    }

    public byte getCommand() {
        return mCommandId;
    }

    public byte[] getData() {
        return mData;
    }

    public byte[] getCommandBytes() {
        return mCommandBytes;
    }

    @Override
    public String toString() {
        return Objects.stringBuilder()
                .addVal("CommandBytes", Objects.toString(mCommandBytes))
                .toString();
    }
}

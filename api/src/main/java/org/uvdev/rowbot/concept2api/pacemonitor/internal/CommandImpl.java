package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import android.content.ContentValues;

import org.uvdev.rowbot.concept2api.ResultCallback;
import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.CommandBuilder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitorResult;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitorStatus;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

import java.nio.ByteBuffer;

public abstract class CommandImpl<R extends PaceMonitorResult> implements
        CommandBuilder.Command<R> {

    protected final byte mCommandId;
    protected final byte[] mData;
    protected final boolean mIsCustomCommand;
    protected final ResultCallback<R> mCallback;

    protected final byte[] mCommandBytes;

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
        mData = (data == null || data.length == 0) ? null : data;
        mCallback = callback;
        int commandTotalByteLength = (mIsCustomCommand ? 2 : 0) /* Custom command prefix */
                + 1 /* commandId */
                + (mData == null ? 0 : mData.length + 1) /* data + size byte */;
        ByteBuffer buffer = ByteBuffer.allocate(commandTotalByteLength);
        if (mIsCustomCommand) {
            buffer.put((byte) 0x1A);
            buffer.put((byte) (((mData == null) ? 0 : (mData.length + 1)) + 1));
        }
        buffer.put(mCommandId);
        if (mData != null) {
            buffer.put((byte) mData.length);
            buffer.put(mData);
        }
        mCommandBytes = buffer.array();
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

    public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
        ContentValues values = new ContentValues();
        values.put(PaceMonitorColumnContract.FRAME_COUNT, status.getFrameCount());
        values.put(PaceMonitorColumnContract.PREV_FRAME_STATUS, status.getPrevFrameStatus());
        values.put(PaceMonitorColumnContract.SLAVE_STATUS, status.getSlaveStatus());
        return values;
    }

    abstract public R getResult(DataHolder data, int row);

    @Override
    public String toString() {
        return Objects.stringBuilder()
                .addVal("CommandBytes", Objects.toString(mCommandBytes))
                .toString();
    }
}

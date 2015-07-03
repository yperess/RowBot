package com.concept2.api.service.broker.pacemonitor;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.pacemonitor.PaceMonitorStatus;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.utils.Objects;

public final class PaceMonitorStatusImpl implements PaceMonitorStatus {

    /**
     * @param status The status to translate.
     * @return The string representation of the status code. Null if status is invalid.
     */
    public static String getPrevFrameStatusString(int status) {
        switch (status) {
            case PrevFrameStatus.OK: return "OK";
            case PrevFrameStatus.REJECTED: return "Rejected";
            case PrevFrameStatus.BAD: return "Bad";
            case PrevFrameStatus.NOT_READY: return "NotReady";
            default: return null;
        }
    }

    /**
     * @param status The status to translate.
     * @return The string representation of the status code. Null if status is invalid.
     */
    public static String getSlaveStatusString(int status) {
        switch (status) {
            case SlaveStatus.ERROR: return "Error";
            case SlaveStatus.READY: return "Ready";
            case SlaveStatus.IDLE: return "Idle";
            case SlaveStatus.HAVE_ID: return "HaveId";
            case SlaveStatus.PAUSED: return "Paused";
            case SlaveStatus.FINISHED: return "Finished";
            case SlaveStatus.MANUAL: return "Manual";
            case SlaveStatus.OFFLINE: return "Offline";
            default: return null;
        }
    }

    public final int mFrameCount;
    public final int mPrevFrameStatus;
    public final int mSlaveStatus;

    public PaceMonitorStatusImpl(byte val) {
        mFrameCount = (val & 0x80) >> 7;
        mPrevFrameStatus = (val & 0x30) >> 4;
        mSlaveStatus = (val & 0x0F);
    }

    public PaceMonitorStatusImpl(int frameCount, int prevFrameStatus, int slaveStatus) {
        mFrameCount = frameCount;
        mPrevFrameStatus = prevFrameStatus;
        mSlaveStatus = slaveStatus;
    }

    @Override
    public int getFrameCount() {
        return mFrameCount;
    }

    @Override
    public int getPrevFrameStatus() {
        return mPrevFrameStatus;
    }

    @Override
    public int getSlaveStatus() {
        return mSlaveStatus;
    }

    /**
     * Create a {@link ContentValues} representation of this object.
     *
     * @return The raw data as a {@link ContentValues}.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PaceMonitorColumnContract.FRAME_COUNT, mFrameCount);
        values.put(PaceMonitorColumnContract.PREV_FRAME_STATUS, mPrevFrameStatus);
        values.put(PaceMonitorColumnContract.SLAVE_STATUS, mSlaveStatus);
        return values;
    }

    @Override
    public String toString() {
        return Objects.stringBuilder()
                .addVal("FrameCount", mFrameCount)
                .addVal("PrevFrameStatus", getPrevFrameStatusString(mPrevFrameStatus))
                .addVal("SlaveStatus", getSlaveStatusString(mSlaveStatus))
                .toString();
    }
}

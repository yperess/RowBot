package com.concept2.api.service.broker.pacemonitor;

import android.os.Parcel;
import android.os.Parcelable;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.pacemonitor.PaceMonitorStatus;
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

    public int toConcept2StatusCode() {
        switch (mPrevFrameStatus) {
            case PrevFrameStatus.REJECTED:
            case PrevFrameStatus.NOT_READY:
                return Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR;
            case PrevFrameStatus.BAD:
                return Concept2StatusCodes.PACE_MONITOR_DATA_ERROR;
        }
        // mPrevFrameStatus must be PrevFrameStatus.OK to be here.
        switch (mSlaveStatus) {
            case SlaveStatus.ERROR:
                return Concept2StatusCodes.PACE_MONITOR_INTERNAL_ERROR;
            case SlaveStatus.OFFLINE:
                return Concept2StatusCodes.PACE_MONITOR_INVALID_REQUEST;

        }
        // mPrevFrameStatus == PrevFrameStatus.OK
        // mSlaveStatus = SlaveStatus.READY | SlaveStatus.IDLE | SlaveStatus.HAVE_ID
        //         | SlaveStatus.IN_USE | SlaveStatus.PAUSED | SlaveStatus.FINISHED
        //         | SlaveStatus.MANUAL
        return Concept2StatusCodes.OK;
    }

    @Override
    public String toString() {
        return Objects.stringBuilder()
                .addVal("FrameCount", mFrameCount)
                .addVal("PrevFrameStatus", getPrevFrameStatusString(mPrevFrameStatus))
                .addVal("SlaveStatus", getSlaveStatusString(mSlaveStatus))
                .toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mFrameCount);
        dest.writeInt(mPrevFrameStatus);
        dest.writeInt(mSlaveStatus);
    }

    public static final Parcelable.Creator<PaceMonitorStatusImpl> CREATOR =
            new Creator<PaceMonitorStatusImpl>() {
                @Override
                public PaceMonitorStatusImpl createFromParcel(Parcel source) {
                    return new PaceMonitorStatusImpl(source.readInt() /* frameCount */,
                            source.readInt() /* prevFrameStatus */,
                            source.readInt() /* slaveStatus */);
                }

                @Override
                public PaceMonitorStatusImpl[] newArray(int size) {
                    return new PaceMonitorStatusImpl[size];
                }
            };
}

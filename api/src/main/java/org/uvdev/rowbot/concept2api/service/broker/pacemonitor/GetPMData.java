package org.uvdev.rowbot.concept2api.service.broker.pacemonitor;

import android.content.Context;
import android.util.Log;

import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.common.Constants;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitorStatus;
import org.uvdev.rowbot.concept2api.utils.Objects;
import org.uvdev.rowbot.concept2api.utils.Preconditions;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class GetPMData {

    private static final String TAG = "GetPMData";
    private static final boolean DBG = false | Constants.DBG;

    private final Engine mEngine;
    private final Context mContext;
    private final ReportId mReportId;
    private final byte mCommand;
    private final byte[] mCommandData;
    private final boolean mValidate;
    private final byte mValidationLength;

    private GetPMData(Builder builder) {
        mEngine = builder.mEngine;
        mContext = builder.mContext;
        mReportId = builder.mReportId;
        mCommand = builder.mCommand;
        mCommandData = builder.mData;
        mValidate = builder.mValidationEnabled;
        mValidationLength = builder.mValidationLength;
    }

    public DataResult getData() {
        try {
            validateConnection();
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor not connected", e);
            return new DataResult(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        }

        DataResult result = getPMData();
        if (mValidate) {
            result = validateDataResult(result);
        }
        return result;
    }

    private void validateConnection() throws Engine.Concept2EngineConnectionException {
        if (!mEngine.isConnected()) {
            mEngine.start(mContext);
        }
    }

    private GetPMData.DataResult getPMData() {
        byte[] bytes;
        try {
            bytes = mEngine.getPMData(getCommandBytes());
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor connection error", e);
            return new GetPMData.DataResult(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        } catch (Csafe.CsafeExtractException|Csafe.DestuffResult.DestuffException e) {
            Log.e(TAG, "Pace monitor communication error", e);
            return new GetPMData.DataResult(Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR);
        }
        if (bytes == null || bytes.length == 0) {
            return new GetPMData.DataResult(Concept2StatusCodes.INTERNAL_ERROR);
        }

        return new GetPMData.DataResult(bytes);
    }

    private byte[] getCommandBytes() {
        int numBytes = 1 + (mCommandData == null ? 0 : mCommandData.length);
        byte[] bytes = new byte[numBytes];
        bytes[0] = mCommand;
        if (bytes.length > 1) {
            System.arraycopy(mCommandData, 0, bytes, 1, mCommandData.length);
        }
        return bytes;
    }

    private GetPMData.DataResult validateDataResult(GetPMData.DataResult dataResult) {
        if (!dataResult.isSuccess()) {
            return dataResult;
        }
        // Check status, id, and response length.
        if (dataResult.getId() != mCommand || dataResult.getData() == null
                || dataResult.getData().length != mValidationLength) {
            return new GetPMData.DataResult(Concept2StatusCodes.PACE_MONITOR_DATA_ERROR);
        }
        return dataResult;
    }

    public static DataResult executeCommand(Context context, Engine engine, byte[] command) {
        byte[] bytes;
        try {
            if (!engine.isConnected()) {
                engine.start(context);
            }
            bytes = engine.getPMData(command);
        } catch (Csafe.DestuffResult.DestuffException|Csafe.CsafeExtractException e) {
            Log.e(TAG, "Pace monitor communication error", e);
            return new GetPMData.DataResult(Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR);
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor connection error", e);
            return new GetPMData.DataResult(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        }
        if (bytes == null || bytes.length == 0) {
            return new GetPMData.DataResult(Concept2StatusCodes.INTERNAL_ERROR);
        }

        return new GetPMData.DataResult(bytes);
    }

    public static ValueResult executeCommandRaw(Context context, Engine engine, byte[] command) {
        byte[] bytes;
        try {
            if (!engine.isConnected()) {
                engine.start(context);
            }
            bytes = engine.getPMData(command);
        } catch (Csafe.DestuffResult.DestuffException|Csafe.CsafeExtractException e) {
            Log.e(TAG, "Pace monitor communication error", e);
            return new ValueResult(Concept2StatusCodes.PACE_MONITOR_COMMUNICATION_ERROR);
        } catch (Engine.Concept2EngineConnectionException e) {
            Log.e(TAG, "Pace monitor connection error", e);
            return new ValueResult(Concept2StatusCodes.PACE_MONITOR_NOT_FOUND);
        }
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new ValueResult(bytes);
    }

    public static byte[] convertCommandBytes(byte[] bytes) {
        byte checkSum = Csafe.checksum(bytes);
        byte[] stuffed = Csafe.stuff(bytes);
        if (stuffed == null) {
            return null;
        }
        bytes = Csafe.create(stuffed, stuffed.length, checkSum);
        return bytes;
    }

    public static final class ValueResult {
        private final int mStatusCode;
        private final PaceMonitorStatusImpl mPaceMonitorStatus;
        private final ByteBuffer mData;

        public ValueResult(byte[] bytes) {
            Preconditions.assertNotNull(bytes);
            Preconditions.assertTrue(bytes.length > 0);
            mStatusCode = Concept2StatusCodes.OK;
            mData = ByteBuffer.wrap(bytes);
            mPaceMonitorStatus = new PaceMonitorStatusImpl(mData.get());
        }

        public ValueResult(int statusCode) {
            mStatusCode = statusCode;
            mPaceMonitorStatus = null;
            mData = null;
        }

        public int getStatusCode() {
            return mStatusCode;
        }

        public boolean isSuccess() {
            return mStatusCode == Concept2StatusCodes.OK;
        }

        public PaceMonitorStatus getPaceMonitorStatus() {
            return mPaceMonitorStatus;
        }

        public ByteBuffer getData() {
            return mData;
        }
    }
    public static final class DataResult {
        private final int mStatusCode;
        private final PaceMonitorStatusImpl mStatus;
        private final byte mId;
        public final byte[] mData;

        public DataResult(byte[] bytes) {
            Preconditions.assertNotNull(bytes);
            Preconditions.assertTrue(bytes.length > 0);
            mStatus = new PaceMonitorStatusImpl(bytes[0]);
            int statusCode = Concept2StatusCodes.OK;
            if (bytes.length > 1) {
                Preconditions.assertTrue(bytes.length >= 3);
                mId = bytes[1];
                int length = bytes[2];
                if (bytes.length - 3 != length) {
                    statusCode = Concept2StatusCodes.PACE_MONITOR_DATA_ERROR;
                }
                if (length != 0) {
                    mData = new byte[length];
                    System.arraycopy(bytes, 3, mData, 0, length);
                } else {
                    mData = null;
                }
            } else {
                mId = 0;
                mData = null;
            }
            mStatusCode = statusCode;
        }

        public DataResult(int statusCode) {
            mStatusCode = statusCode;
            mStatus = null;
            mId = 0;
            mData = null;
        }

        public int getStatusCode() {
            return mStatusCode;
        }

        public PaceMonitorStatusImpl getStatus() {
            return mStatus;
        }

        public byte getId() {
            return mId;
        }

        public byte[] getData() {
            return mData;
        }

        public boolean isSuccess() {
            return mStatusCode == Concept2StatusCodes.OK;
        }

        @Override
        public String toString() {
            return Objects.stringBuilder()
                    .addVal("StatusCode", mStatusCode)
                    .addVal("CommandStatus", mStatus)
                    .addVal("Data", Arrays.toString(mData))
                    .toString();
        }
    }

    public static final class Builder {
        private final Context mContext;
        private final Engine mEngine;
        private final byte mCommand;
        private final ReportId mReportId;

        private byte[] mData;
        private boolean mValidationEnabled;
        private byte mValidationLength;

        public Builder(Context context, Engine engine, byte command) {
            mContext = context;
            mEngine = engine;
            mCommand = command;
            mReportId = mCommand >= 0x80 ? ReportId.SMALL : ReportId.LARGE;
        }

        public Builder setData(byte[] data) {
            mData = data;
            return this;
        }

        public Builder setValidation(byte length) {
            mValidationEnabled = true;
            mValidationLength = length;
            return this;
        }

        public GetPMData build() {
            return new GetPMData(this);
        }
    }
}

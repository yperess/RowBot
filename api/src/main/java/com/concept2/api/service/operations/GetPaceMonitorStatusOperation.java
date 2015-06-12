package com.concept2.api.service.operations;

import android.content.Context;
import android.util.Log;

import com.concept2.api.common.Constants;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.internal.GetStatusResultRef;
import com.concept2.api.service.broker.DataBroker;

/**
 * Operation to get the status of a Pace Monitor.
 */
public class GetPaceMonitorStatusOperation extends BaseDataOperation {

    private static final String TAG = "GetPMStatusOp";
    private static final boolean DBG = false | Constants.DBG;

    /** The pending result to report the data to when done. */
    private final PendingResultImpl<PaceMonitor.GetStatusResult> mPendingResult;

    /**
     * @param pendingResult The pending result to report the data to when done.
     */
    public GetPaceMonitorStatusOperation(PendingResultImpl<PaceMonitor.GetStatusResult> pendingResult) {
        mPendingResult = pendingResult;
    }

    @Override
    protected DataHolder getData(DataBroker dataBroker, Context context) {
        if (DBG) Log.d(TAG, "getData()");
        return dataBroker.getPaceMonitorStatus(context);
    }

    @Override
    protected void onResult(DataHolder data) {
        if (DBG) Log.d(TAG, "onResult(" + data.toString() + ")");
        mPendingResult.setResult(new GetStatusResultRef(data));
    }
}

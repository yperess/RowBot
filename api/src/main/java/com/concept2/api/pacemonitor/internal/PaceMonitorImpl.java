package com.concept2.api.pacemonitor.internal;

import android.content.Context;
import android.util.Log;

import com.concept2.api.PendingResult;
import com.concept2.api.common.Constants;
import com.concept2.api.internal.PendingResultImpl;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.service.Concept2AsyncTaskService;

/**
 * Implementation of the {@link PaceMonitor} APIs.
 */
public class PaceMonitorImpl implements PaceMonitor {

    /**
     * {@link PendingResult} returned by {@link #getStatus(Context)}.
     */
    private static final class GetStatusPendingResult extends PendingResultImpl<GetStatusResult> {

        @Override
        public GetStatusResult getFailedResult(final int statusCode) {
            return new GetStatusResult() {
                @Override
                public int getPaceMonitorStatus() {
                    return Status.WAITING_FOR_CONNECTION;
                }

                @Override
                public int getStatus() {
                    return statusCode;
                }
            };
        }
    }

    @Override
    public PendingResult<GetStatusResult> getStatus(Context context) {
        GetStatusPendingResult pendingResult = new GetStatusPendingResult();
        Concept2AsyncTaskService.getPaceMonitorStatus(context, pendingResult);
        return pendingResult;
    }
}

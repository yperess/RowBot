package com.concept2.api.pacemonitor.internal;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor;
import com.concept2.api.pacemonitor.PaceMonitorResult;

import java.util.ArrayList;
import java.util.List;

public class BatchResultRef implements PaceMonitor.BatchResult {

    private final int mStatus;
    private final ArrayList<PaceMonitorResult> mResults;

    public BatchResultRef(ArrayList<PaceMonitorResult> results) {
        mStatus = Concept2StatusCodes.OK;
        mResults = results;
    }
    public BatchResultRef(int status) {
        mStatus = status;
        mResults = null;
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public List<PaceMonitorResult> getResults() {
        return mResults;
    }
}

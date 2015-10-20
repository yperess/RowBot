package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.Concept2StatusCodes;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitorResult;
import org.uvdev.rowbot.concept2api.utils.Objects;

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

    @Override
    public String toString() {
        Objects.ObjectsStringBuilder builder = Objects.stringBuilder()
                .addVal("Status", getStatus());
        if (mResults != null) {
            for (int i = 0; i < mResults.size(); ++i) {
                builder.addVal(""+i, mResults.get(i));
            }
        }
        return builder.toString();
    }
}

package com.concept2.api.pacemonitor.internal;

import android.content.ContentValues;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.CreateBatchCommandResult;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.utils.Objects;

public class CreateCommandBatchResultRef extends DataHolder implements
        CreateBatchCommandResult {

    private static String COMMAND_BATCH_ID = PaceMonitorColumnContract.COMMAND_BATCH_ID;

    public CreateCommandBatchResultRef(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public int getBatchId() {
        return getInt(0 /* row */, COMMAND_BATCH_ID, -1);
    }

    @Override
    public String toString() {
        return Objects.stringBuilder()
                .addVal("Status", getStatus())
                .addVal("BatchId", getBatchId())
                .toString();
    }
}

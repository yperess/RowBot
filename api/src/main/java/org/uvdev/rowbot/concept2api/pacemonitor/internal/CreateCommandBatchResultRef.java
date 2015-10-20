package org.uvdev.rowbot.concept2api.pacemonitor.internal;

import org.uvdev.rowbot.concept2api.DataHolder;
import org.uvdev.rowbot.concept2api.pacemonitor.PaceMonitor.CreateBatchCommandResult;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import org.uvdev.rowbot.concept2api.utils.Objects;

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

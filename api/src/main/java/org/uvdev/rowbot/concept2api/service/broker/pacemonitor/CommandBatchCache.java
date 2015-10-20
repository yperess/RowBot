package org.uvdev.rowbot.concept2api.service.broker.pacemonitor;

import android.util.SparseArray;

import java.util.HashMap;

public class CommandBatchCache {

    private static final CommandBatchCache sInstance = new CommandBatchCache();

    public static CommandBatchCache getInstance() {
        return sInstance;
    }

    private final HashMap<CommandBatch, Integer> mCommandBatchMap = new HashMap<>();
    private final SparseArray<CommandBatch> mCommandBatchArray = new SparseArray<>();
    private int mNextId = 0;

    private CommandBatchCache() {}

    public synchronized int saveCommandBatch(CommandBatch batch) {
        if (mCommandBatchMap.containsKey(batch)) {
            return mCommandBatchMap.get(batch);
        }
        int id = mNextId++;
        mCommandBatchMap.put(batch, id);
        mCommandBatchArray.put(id, batch);
        return id;
    }

    public synchronized CommandBatch findCommandBatch(int id) {
        return mCommandBatchArray.get(id);
    }
}

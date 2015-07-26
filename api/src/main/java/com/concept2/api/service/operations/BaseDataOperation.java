package com.concept2.api.service.operations;

import android.content.Context;
import android.util.Log;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.service.broker.DataBroker;

/**
 * Base operation used to get a {@link DataHolder}.
 */
public abstract class BaseDataOperation extends BaseOperation {

    /** The data holder to get from the broker. */
    private DataHolder mDataHolder;

    @Override
    protected int execute(DataBroker dataBroker, Context context) {
        mDataHolder = getData(dataBroker, context);
        return mDataHolder == null ? Concept2StatusCodes.INTERNAL_ERROR : mDataHolder.getStatus();
    }

    /**
     * This method essentially replaces {@link #execute(DataBroker, Context)} for this operation.
     * The status reported in the {@link DataHolder} is used as the return value in the execute
     * method.
     *
     * @param dataBroker The {@link DataBroker} to execute the command.
     * @param context The calling context.
     * @return {@link DataHolder} with the resulting data.
     */
    abstract protected DataHolder getData(DataBroker dataBroker, Context context);

    @Override
    protected void onResult(int status) {
        onResult(mDataHolder == null ? DataHolder.empty(status) : mDataHolder);
    }

    /**
     * Replacement method for {@link #onResult(int)} for this operation.
     *
     * @param data The {@link DataHolder} returned from {@link #getData(DataBroker, Context)}.
     */
    abstract protected void onResult(DataHolder data);
}

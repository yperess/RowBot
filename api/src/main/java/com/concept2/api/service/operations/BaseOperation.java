package com.concept2.api.service.operations;

import android.content.Context;
import android.util.Log;

import com.concept2.api.Concept2StatusCodes;
import com.concept2.api.service.broker.DataBroker;

public abstract class BaseOperation {

    private static final String TAG = "BaseOperation";

    public final void run(DataBroker dataBroker, Context context) {
        int status = Concept2StatusCodes.INTERNAL_ERROR;
        try {
            status = execute(dataBroker, context);
        } catch (Exception | Error e) {
            Log.e(TAG, "Exception thrown during execute.", e);
            status = Concept2StatusCodes.INTERNAL_ERROR;
        } finally {
            onResult(status);
        }
    }

    protected abstract int execute(DataBroker dataBroker, Context context);

    protected abstract void onResult(int status);
}

package com.concept2.api.impl;

import android.content.Context;
import android.os.SystemClock;

import com.concept2.api.commands.Command;
import com.concept2.api.constants.ReportId;
import com.concept2.api.impl.connection.Authentication;
import com.concept2.api.impl.connection.Engine;
import com.concept2.api.impl.connection.USBEngine;
import com.concept2.api.model.VirtualPaceMonitorApi;

public class VirtualPaceMonitorImpl implements VirtualPaceMonitorApi {

    private static final String TAG = "VPM";
    private static final boolean DBG = true;

    private Engine mDataEngine;
    private boolean mConnectionAuthenticated;

    public VirtualPaceMonitorImpl() {
        mDataEngine = new USBEngine();
        mConnectionAuthenticated = false;
    }

    @Override
    public boolean start(Context context) {
        return start(context, 0L);
    }

    @Override
    public synchronized boolean start(Context context, long timeout) {
        mConnectionAuthenticated = false;
        if (!mDataEngine.start(context)) {
            return false;
        }
        authenticatePaceMonitor(timeout);
        return mConnectionAuthenticated;
    }

    @Override
    public synchronized void stop() {
        mDataEngine.stop();
        mConnectionAuthenticated = false;
    }

    @Override
    public synchronized byte[] executeCommandBytes(ReportId reportId, byte[] commandBytes)
            throws ConnectionException {
        verifyConnection();
        return mDataEngine.getPMData(null /* handler */, reportId, commandBytes);
    }

    @Override
    public synchronized boolean isConnected() {
        return mDataEngine != null && mDataEngine.isConnected() && mConnectionAuthenticated;
    }

    private void verifyConnection() throws ConnectionException {
        if (!isConnected()) {
            throw new ConnectionException("Pace Monitor connection not detected.");
        }
    }

    private void authenticatePaceMonitor(long timeout) {
        final long timeoutExpire = SystemClock.elapsedRealtime() + timeout;
        mConnectionAuthenticated = false;
        try {
            while (!mConnectionAuthenticated
                    && (timeout <= 0L || SystemClock.elapsedRealtime() < timeoutExpire)) {
                byte[] returnData = Command.GET_HARDWARE_ADDRESS.execute(mDataEngine,
                        ReportId.SMALL);
                int hardwareAddress = ((returnData[5] & 0xff) << 24)
                        | ((returnData[6] & 0xff) << 16)
                        | ((returnData[7] & 0xff) << 8)
                        | (returnData[8] & 0xff);
                byte[] hardwareAddressBytes = new byte[4];
                System.arraycopy(returnData, 5, hardwareAddressBytes, 0, 4);

                Authentication authenticate = new Authentication();
                int serialNumber = hardwareAddress;

                int[] authenticationPassword = authenticate.getPassword(serialNumber);
                byte[] authenticationPasswordBytes = new byte[8];
                for (int i = 0; i < 4; ++i) {
                    authenticationPasswordBytes[i] = (byte)(authenticationPassword[0] & 0xFF);
                    authenticationPasswordBytes[i+4] = (byte)(authenticationPassword[1] & 0xFF);
                    authenticationPassword[0] = authenticationPassword[0] >>> 8;
                    authenticationPassword[1] = authenticationPassword[1] >>> 8;
                }
                Command authCommand = new Command(
                        new Command(Command.SET_AUT_PASSWORD, hardwareAddressBytes),
                        authenticationPasswordBytes);

                returnData = authCommand.execute(mDataEngine, ReportId.SMALL);
                int value = (returnData[5] & 0xff);
                if (value == 1) {
                    mConnectionAuthenticated = true;
                }
            }
        } catch (Exception e) {}
    }
}

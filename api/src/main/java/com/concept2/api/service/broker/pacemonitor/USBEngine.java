package com.concept2.api.service.broker.pacemonitor;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class USBEngine implements Engine {

    private static final String TAG = "USBEngine";
    private static final boolean DBG = true;

    private static final int CONCEPT2_VENDOR_ID = 6052;
    private static final int MAX_BUFFER_SIZE_OUT = 127;
    private static final int MAX_BUFFER_SIZE_IN = 128;

    private UsbRequest mInReq, mOutReq;
    private UsbEndpoint mEndpointIn, mEndpointOut;
    private UsbInterface mUsbInterface;
    private UsbDeviceConnection mDeviceConnection;
    private boolean mIsRunning = false;

    @Override
    public synchronized void start(Context context) throws Concept2EngineConnectionException {
        UsbManager usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        HashMap<String, UsbDevice> devmap = usbManager.getDeviceList();

        if (DBG) Log.d(TAG, devmap.size() + " usb devices found");
        for (UsbDevice dev : devmap.values()) {
            if (dev.getVendorId() == CONCEPT2_VENDOR_ID) {
                if (DBG) Log.d(TAG, dev.getDeviceName() + " " + dev.getDeviceId() + " "
                        + dev.getVendorId() + " " + dev.getProductId());
                mDeviceConnection = usbManager.openDevice(dev);

                if (mDeviceConnection == null) {
                    Log.e(TAG, "connection is null");
                    throw new Concept2EngineConnectionException();
                }

                mUsbInterface = dev.getInterface(0);

                if (DBG) Log.d(TAG, "Interface Count: " + dev.getInterfaceCount());
                if (!mDeviceConnection.claimInterface(mUsbInterface, true)) {
                    Log.e(TAG, "could not claim interface!");
                    mDeviceConnection.close();
                    break;
                }
                if (DBG) {
                    Log.d(TAG, "claimed interface " + mUsbInterface.getEndpointCount());
                    for (int i = 0; i < mUsbInterface.getEndpointCount(); i++) {
                        UsbEndpoint ep = mUsbInterface.getEndpoint(i);
                        Log.d(TAG, "endpoint type " + ep.getType() + " endpoint dir "
                                + ep.getDirection() + " endpoint addr "
                                + String.format("0x%02X", ep.getAddress()));
                    }
                }

                mEndpointIn = mUsbInterface.getEndpoint(0);
                mEndpointOut = mUsbInterface.getEndpoint(1);

                mInReq = new UsbRequest();
                mInReq.initialize(mDeviceConnection, mEndpointIn);
                mOutReq = new UsbRequest();
                mOutReq.initialize(mDeviceConnection, mEndpointOut);
                mIsRunning = true;
                return;
            }
        }
        throw new Concept2EngineConnectionException();
    }

    @Override
    public synchronized byte[] getPMData(ReportId reportId, byte[] command)
            throws Concept2EngineConnectionException, Csafe.CsafeExtractException, Csafe.DestuffResult.DestuffException {
        byte[] content = command;
        byte checksum = Csafe.checksum(content);
        byte[] stuffed = Csafe.stuff(content);
        byte[] buffer = Csafe.create(reportId.getValue(), stuffed, stuffed.length, checksum);
        byte[] returnData;

        ByteBuffer bufferOut = ByteBuffer.wrap(buffer);
        ByteBuffer bufferIn = ByteBuffer.wrap(new byte[MAX_BUFFER_SIZE_IN]);

        bufferOut.position(0);
        bufferIn.position(0);

        if (mDeviceConnection == null || !mIsRunning) {
            throw new Concept2EngineConnectionException();
        }

        // Send the request.
        mOutReq.queue(bufferOut, MAX_BUFFER_SIZE_OUT);
        bufferOut.position(0);

        if (DBG) Log.d(TAG, "out request " + toString(bufferOut.array()));

        // Initialize the buffer and wait for response.
        mInReq.queue(bufferIn, MAX_BUFFER_SIZE_IN);
        UsbRequest req;
        do {
            req = mDeviceConnection.requestWait();
            if (req == null) {
                Log.e(TAG, "Could not get an inbound request from USB.");
                mIsRunning = false;
                throw new Concept2EngineConnectionException();
            }
            // Keep waiting for an inbound request.
        } while (req.getEndpoint().getDirection() != UsbConstants.USB_DIR_IN);


        if (req.getEndpoint().getDirection() == UsbConstants.USB_DIR_IN) {
            // In request
            bufferIn.position(0);
            if (DBG) {
                Log.d(TAG, "in response " + toString(bufferIn.array()));
                bufferIn.position(0);
            }

            byte[] extract = Csafe.extract(bufferIn.array());
            Csafe.DestuffResult destuff = Csafe.destuff(extract);
            returnData = destuff.data;
        } else {
            // Response didn't include an 'in' direction, bail.
            throw new Concept2EngineConnectionException();
        }

        if (DBG) Log.d(TAG, "data: " + toString(returnData));
        return returnData;
    }

    @Override
    public synchronized void stop() {
        try {
            if (mInReq != null) mInReq.cancel();
            if (mOutReq != null) mOutReq.cancel();

            if (mDeviceConnection != null) {
                mDeviceConnection.releaseInterface(mUsbInterface);
                if (DBG) Log.d(TAG, "released interface");
                mDeviceConnection.close();
                if (DBG) Log.d(TAG, "closed connection");
            }
        } catch (Exception e) {
            Log.e(TAG, "could not unregister receiver, already unregistered");
        } finally {
            mIsRunning = false;
        }
        if (DBG) Log.d(TAG, "Task ended");
    }

    @Override
    public synchronized boolean isConnected() {
        return mIsRunning;
    }

    private String toString(byte[] bytes) {
        String byteString = "";
        for (byte b : bytes) {
            byteString += String.format("%02X ", b & 0xFF);
            if (b == (byte)0xf2) break;
        }
        return byteString;
    }
}

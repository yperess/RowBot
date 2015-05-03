package com.concept2.api.impl.connection;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.concept2.api.constants.ReportId;

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

    /**
     * Start the USB engine by scanning for any connections to a Concept2 Pace Monitor identified by
     * a vendor id ({@link #CONCEPT2_VENDOR_ID}).
     *
     * @param context The connecting context.
     * @return True upon a successful connection.
     */
    @Override
    public synchronized boolean start(Context context) {
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
                    return false;
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
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized byte[] getPMData(Handler handler, ReportId reportId, byte[] command) {
        try {
            byte[] content = command;
            byte checksum = Csafe.checksum(content);
            byte[] stuffed = Csafe.stuff(content);
            byte[] buffer = Csafe.create(reportId.getValue(), stuffed, stuffed.length, checksum);
            byte[] returnData;

            ByteBuffer bufferOut = ByteBuffer.wrap(buffer);
            ByteBuffer bufferIn = ByteBuffer.wrap(new byte[MAX_BUFFER_SIZE_IN]);

            bufferOut.position(0);
            bufferIn.position(0);

            // Send the request.
            if (mDeviceConnection == null || !mIsRunning) return null;
            mOutReq.queue(bufferOut, MAX_BUFFER_SIZE_OUT);
            bufferOut.position(0);

            if (DBG) Log.d(TAG, "out request " + toString(bufferOut.array()));

            // Initialize the buffer and wait for response.
            mInReq.queue(bufferIn, MAX_BUFFER_SIZE_IN);
            UsbRequest req = mDeviceConnection.requestWait();
            while (req.getEndpoint().getDirection() == UsbConstants.USB_DIR_OUT) {
                // Request executed was outbound. Wait for inbound request.
                req = mDeviceConnection.requestWait();
            }

            if (req == null) {
                Log.e(TAG, "Could not get an inbound request from USB.");
                return null;
            }

            if (req.getEndpoint().getDirection() == UsbConstants.USB_DIR_IN) {
                // In request
                bufferIn.position(0);
                if (DBG) {
                    Log.d(TAG, "in response " + toString(bufferIn.array()));
                    bufferIn.position(0);
                }

                byte[] extract = Csafe.extract(bufferIn.array());
                if (extract == null) {
                    Log.e(TAG, "error in Csafe extract");
                    return null;
                }
                Pair<byte[], Byte> destuff = Csafe.destuff(extract);

                if (!Csafe.verify(destuff.first, destuff.second)) {
                    Log.e(TAG, "verification failed " + toString(extract));
                    return null;
                } else {
                    returnData = destuff.first;
                }
            } else {
                // Response didn't include an 'in' direction, bail.
                return null;
            }

            if (handler != null) {
                // Send data to handler.
                Bundle bundle = new Bundle();
                bundle.putByteArray(EXTRA_RESPONSE_VALUE, returnData);
                Message message = handler.obtainMessage(0);
                message.setData(bundle);
                handler.sendMessage(message);
                return null;
            } else {
                return returnData;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception" + e);
        } catch (Error e) {
            Log.e(TAG, "Error in USBEngine");
        }
        return null;
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
            byteString += String.format("%02X ", b);
            if (b== (byte)0xf2) break;
        }
        return byteString;
    }
}

package com.concept2.api.service.broker.pacemonitor;

import android.util.Log;

/**
 * Csafe utilities used to perform byte manipulation needed to communicate with the Concept2 Pace
 * Monitor.
 */
public class Csafe {

    private static final String TAG = "Csafe";
    private static final boolean DBG = false;

    /** Maximum byte buffer size. */
    private static final int MAX_BUFFER = 128;

    /**
     * Get the checksum of a byte array.
     *
     * @param data The data to compute the checksum for.
     * @return The checksum.
     */
    public static byte checksum(byte[] data){
        byte checksum = 0x00;
        for (int i = 0; i < data.length; ++i) {
            checksum ^= data[i];
        }
        return checksum;
    }

    /**
     * Stuff the data into a format as expected by the pace monitor. This means escaping special
     * bytes used to start/stop data frames.
     *
     * @param data The data to stuff.
     * @return The stuffed data of len {@link #MAX_BUFFER}-1 or null on error.
     */
    public static byte[] stuff(byte[] data){
        byte[] newData = new byte[MAX_BUFFER-4];//max size
        int idx = 0;
        for (int i = 0; i < data.length; ++i) {
            if ((data[i] & 0xFC) == 0xF0) {
                newData[idx++] = (byte) 0xF3;
                newData[idx++] = (byte) (0x03 & data[i]);
            } else {
                newData[idx++] = data[i];
            }
            if (idx >= newData.length) {
                Log.e(TAG, "too much data to stuff");
                return null;
            }
        }
        byte[] stuffed = new byte[idx];
        System.arraycopy(newData, 0, stuffed, 0, idx);
        return stuffed;
    }

    /**
     * Wrap a give byte array into a frame as expected by the pace monitor.
     * TEST 01 F1 94 94 F2
     *
     * @param data The stuffed csafe frame contents
     * @param len Length of data as a subset of the data bytes.
     * @param chk The checksum of the unstuffed frame contents
     * @return The csafe frame wraped with checksum and start/stop or null on failure
     */
    public static byte[] create(byte reportID, byte[] data, int len, byte chk){
        if (data.length < len) {
            Log.e(TAG, "data.length is smaller than len");
            return null;
        }
        if (len >= MAX_BUFFER - 4) {
            Log.e(TAG, "Data too long! Max " + (MAX_BUFFER - 4) + "!");
            return null;
        }

        byte[] newData = new byte[MAX_BUFFER-1];//max 63
        newData[0] = reportID;
        newData[1] = (byte) 0xF1;
        for (int i = 0; i < len; ++i) {
            newData[i+2] = data[i];
        }
        newData[len+2] = chk;
        newData[len+3] = (byte) 0xF2;
        return newData;
    }

    /**
     * Exception thrown if data could not be extracted from the frame.
     */
    public static final class CsafeExtractException extends Exception {}

    /**
     * Extracts bytes from a frame by removing extra bit stuffing done by the pace monitor.
     *
     * @param frame The byte frame returned by the pace monitor.
     * @return An array of data bytes returned by the pace monitor.
     * @throws CsafeExtractException If data could not be extracted.
     */
    public static byte[] extract(byte[] frame) throws CsafeExtractException {
        byte[] newData = new byte[MAX_BUFFER];
        int idx = 0;
        boolean start = false;
        for (int i = 0; i < frame.length; ++i) {
            if (start) {
                if (i+1 < frame.length) {
                    if ((frame[i+1] & 0xFF) == 0xF2) {
                        newData[idx++] = frame[i];
                        Log.d(TAG, "End of frame reached at position " + i);
                        break;
                    } else {
                        newData[idx++] = frame[i];
                    }
                }
            }
            if ((frame[i]&0xFF) == 0xF1) {
                start = true;
            }
        }
        if (!start) {
            throw new CsafeExtractException();
        }
        byte[] strip = new byte[idx];
        System.arraycopy(newData, 0, strip, 0, idx);
        return strip;
    }

    /**
     * Result returned by the {@link #destuff(byte[])} operation.
     */
    public static final class DestuffResult {

        /** Exception thrown if the frame data could not be destuffed. */
        public static final class DestuffException extends Exception {}

        /** The destuffed data. */
        public final byte[] data;

        /** Checksum of the destuffed data. */
        public final byte checksum;

        /**
         * Create a destuff result with a destuffed byte array and checksum.
         *
         * @param data The destuffed byte array.
         * @param checksum The checksum of the destuffed data.
         */
        private DestuffResult(byte[] data, byte checksum) {
            this.data = data;
            this.checksum = checksum;
        }

        /**
         * Verify that the destuffed data is valid.
         *
         * @return This result if the result is valid.
         * @throws DestuffException If the destuffed data is not valid.
         */
        public DestuffResult verify() throws DestuffException {
            byte localCheck = 0;
            for (int i = 0; i < data.length; ++i) {
                localCheck ^= data[i];
            }
            if (localCheck != checksum) {
                throw new DestuffException();
            }
            return this;
        }
    }

    /**
     * Destuff data returned from the pace monitor.
     *
     * @param data The data to destuff.
     * @return {@link DestuffResult} with the destuffed data and checksum.
     * @throws DestuffResult.DestuffException If the data could not be destuffed.
     */
    public static DestuffResult destuff(byte[] data) throws DestuffResult.DestuffException {
        byte[] newData = new byte[MAX_BUFFER];
        int idx = 0;
        for (int i = 0; i < data.length; ++i) {
            if (data[i] == (byte)0xF3) {
                newData[idx++] = (byte) (0xF0 | data[++i]);
            } else {
                newData[idx++] = data[i];
            }
        }

        byte[] destuffed = new byte[idx];
        System.arraycopy(newData, 0, destuffed, 0, idx);

        byte check = destuffed[destuffed.length-1];
        byte[] returnData = new byte[destuffed.length-1];
        System.arraycopy(destuffed, 0, returnData, 0, destuffed.length-1);
        return new DestuffResult(returnData, check).verify();
    }
}

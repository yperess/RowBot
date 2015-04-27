package com.concept2.api.commands;

import android.os.Handler;

import com.concept2.api.constants.ReportId;
import com.concept2.api.impl.connection.Engine;
import com.concept2.api.utils.Preconditions;

import java.util.Arrays;

public class Command {

    public static final Command GET_PM_CONFIG = new Command((byte)0x7E);
    public static final Command GET_HARDWARE_ADDRESS = new Command(GET_PM_CONFIG, (byte)0x01,
            (byte)0x82);

    public static final Command SET_PM_CONFIG = new Command((byte)0x76);
    public static final Command SET_AUT_PASSWORD = new Command(SET_PM_CONFIG, (byte)0x0e,
            (byte)0x1a, (byte)0x0c);

    private final byte[] mBytes;

    public Command(byte... bytes) {
        Preconditions.assertNotNull(bytes);
        Preconditions.assertTrue(bytes.length != 0);
        mBytes = bytes;
    }

    public Command(Command command, byte... bytes) {
        Preconditions.assertNotNull(bytes);
        Preconditions.assertTrue(bytes.length != 0);
        mBytes = Arrays.copyOf(command.mBytes, command.mBytes.length + bytes.length);
        System.arraycopy(bytes, 0, mBytes, command.mBytes.length, bytes.length);
    }

    public byte[] execute(Engine engine, ReportId reportId) {
        return engine.getPMData(null /* handler */, reportId, mBytes);
    }

    public void execute(Engine engine, Handler handler, ReportId reportId) {
        Preconditions.assertNotNull(handler);
        engine.getPMData(handler, reportId, mBytes);
    }
}

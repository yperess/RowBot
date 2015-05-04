package com.concept2.api.pacemonitor.commands;

public enum ReportId {
    SMALL((byte) 0x01), MEDIUM((byte) 0x04), LARGE((byte) 0x02);

    private final byte mValue;

    private ReportId(byte value) {
        mValue = value;
    }

    public byte getValue() {
        return mValue;
    }
}

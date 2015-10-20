package org.uvdev.rowbot.concept2api.service.broker.pacemonitor;

/**
 * Types of reports that can be sent to the pace monitor.
 */
public enum ReportId {
    SMALL((byte) 0x01), MEDIUM((byte) 0x04), LARGE((byte) 0x02);

    /** Byte value of the report. */
    private final byte mValue;

    /**
     * @param value The byte value of the report.
     */
    ReportId(byte value) {
        mValue = value;
    }

    /**
     * @return The byte value of the report.
     */
    public byte getValue() {
        return mValue;
    }
}

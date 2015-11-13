package org.uvdev.rowbot.common.utils;

import android.os.Parcel;

public final class ParcelableUtils {

    public static void writeString(Parcel parcel, String string) {
        parcel.writeByte((byte) (string != null ? 1 : 0));
        parcel.writeString(string);
    }

    public static String readString(Parcel parcel) {
        boolean isPresent = parcel.readByte() == 1;
        return isPresent ? parcel.readString() : null;
    }

    private ParcelableUtils() {}
}
